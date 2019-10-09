/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coloris_rg1.main;

import color_rg1.sprites.Background;
import color_rg1.sprites.BoxNode;
import color_rg1.sprites.GameSpace;
import color_rg1.sprites.StickElement;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class Coloris_RG1 extends Application 
{
    
    public static final double WINDOW_WIDTH = 600;
    public static final double WINDOW_HEIGHT = 800;
    public static long GAME_SPEED = 400_000_000;
    public int gameScore = 0;
    public int scoreIncremet = 100;
    public int highScore = 32000;
    
    private static Group root;
    private static Text score;
    private static Text hScore;
    private AnimationTimer timer;
    
    private ArrayList<ArrayList<BoxNode>> stateMatrix; 
    
    private int [] rowPositions;
    private int currentPositionX;
    private int currentPositionY;
    private int currentRow;
    private StickElement currentNode;
    private StickElement nextNode;
    private boolean isStickActive=true;
    private boolean isStickCreated;
    @Override
    public void start(Stage primaryStage) 
    {
        root = new Group();
        score = new Text();
        score.setTranslateX(WINDOW_WIDTH/2+ 50);
        score.setTranslateY(WINDOW_HEIGHT/2);
        score.setFill(Color.DARKBLUE);
        String sc = "SCORE:\n0" + gameScore;
        score.setText(sc);
        score.setFont(Font.font("Ariel", FontWeight.BOLD, 50));
        
        hScore = new Text();
        hScore.setTranslateX(WINDOW_WIDTH/2+ 50);
        hScore.setTranslateY(WINDOW_HEIGHT*3/4);
        hScore.setFill(Color.RED);
        String hSc = "HIGH SCORE:\n0" + highScore;
        hScore.setText(hSc);
        hScore.setFont(Font.font("Ariel", FontWeight.BOLD, 30));
        
        final ImageView selectedImage = new ImageView();   
        ImageView imageView = new ImageView();
        Image image= new Image("file:Coloris.png");
        imageView.setImage(image);
        rowPositions = new int[7];
        stateMatrix = new ArrayList<ArrayList<BoxNode>>();
        for(int i=0; i<7; i++)
        {
            rowPositions[i]= (int) WINDOW_HEIGHT-142;
            stateMatrix.add(new ArrayList<BoxNode>());
        }
        nextNode = new StickElement();
        Background bg = new Background(WINDOW_WIDTH, WINDOW_HEIGHT);
        GameSpace gs = new GameSpace(WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().addAll(bg,gs,score,hScore,nextNode,imageView);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, d->OnKeyPressed(d));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, d->OnKeyRelased(d));
        primaryStage.setTitle("Coloris Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        timer = new AnimationTimer() 
        {
        private long lastUpdate = 0 ;
            @Override
            public void handle(long now) 
            {
                 if (now - lastUpdate >= GAME_SPEED) {
                    update(root);
                    lastUpdate = now ;
                 }
            }
        };
        timer.start();
    }
    public void OnKeyRelased(KeyEvent e)
    {
        if(e.getCode() == KeyCode.SHIFT)
        {
            GAME_SPEED = 400_000_000;
        }
    }
    public void OnKeyPressed(KeyEvent e)
    {
        switch(e.getCode())
        {
            case UP:
                currentNode.RotateElementsUP();
                break;
                
            case DOWN:
                currentNode.RotateElementsDown();
                break;
                
            case LEFT:
                if(currentPositionX != 0){
                    if(currentPositionY < rowPositions[currentRow-1])
                    {
                        currentPositionX-=BoxNode.SIZE_X;
                        currentRow-=1;
                        currentNode.setTranslateX(currentPositionX);
                    }
                }
                break;
            case RIGHT:
                if(currentPositionX != 6*BoxNode.SIZE_X)
                {
                    if(currentPositionY < rowPositions[currentRow+1])
                    {
                        currentPositionX+=BoxNode.SIZE_X;
                        currentRow+=1;
                        currentNode.setTranslateX(currentPositionX);
                    }
                }
                break;
            case SHIFT:
                GAME_SPEED = 100_000_000;
                break;
               
        }
    }
    public void update(Group g)
    { 
        if(isStickActive)
        {
            isStickActive=true;
            if(!isStickCreated)
            {
                isStickCreated=true;
                currentNode = nextNode;
                nextNode = new StickElement();
                nextNode.setTranslateX(WINDOW_WIDTH/2+50);
                nextNode.setTranslateY(100);
                currentRow=(int)(Math.random() * rowPositions.length);
                currentPositionX =currentRow*BoxNode.SIZE_X;
                currentPositionY=(int) WINDOW_HEIGHT-100-15*BoxNode.SIZE_Y;
                currentNode.setTranslateX(currentPositionX);
                currentNode.setTranslateY(currentPositionY);
                g.getChildren().addAll(nextNode);
            }
        
        
            currentPositionY+=BoxNode.SIZE_Y;
            currentNode.setTranslateY(currentPositionY);
        
            if(currentPositionY==rowPositions[currentRow])
            {
                isStickCreated=false;
                rowPositions[currentRow]-=3*BoxNode.SIZE_Y;
                AddRowToTheMatrix(currentNode);
                UpdateRowPoints();
                if(stateMatrix.get(currentRow).size()>3)
                {
                    UpdateRowPoints();
                }
                if(rowPositions[currentRow] == (int) WINDOW_HEIGHT-142 - BoxNode.SIZE_Y*15)
                {
                    isStickActive=false;
                }
            }
        }
    }
    private void AddRowToTheMatrix(StickElement cNode)
    {
        stateMatrix.get(currentRow).add(cNode.getBottom());
        stateMatrix.get(currentRow).add(cNode.getMiddle());
        stateMatrix.get(currentRow).add(cNode.getTop());
    }
    private void UpdateRowPoints()
    {        
        int countSame=0;
        boolean removedBox=false;
        Color previousColor = stateMatrix.get(currentRow).get(0).getColor();
        for(int i=1; i< stateMatrix.get(currentRow).size();i++)
        {
            BoxNode _currentNode;
            _currentNode = stateMatrix.get(currentRow).get(i);
            if(_currentNode.getColor().equals(previousColor))
            {
                countSame++;
            }
            else
            {
                countSame=0;
            }
            previousColor=_currentNode.getColor();
            if(countSame>=2 && !removedBox)
            {
              //  if((stateMatrix.get(currentRow).get(i+1)!= null) && (!stateMatrix.get(currentRow).get(i+1).getColor().equals(stateMatrix.get(currentRow).get(i).getColor())))
             //   {
                    for(int j=countSame; j>=0; j--)
                    {
                        stateMatrix.get(currentRow).get(i-j).getChildren().remove(stateMatrix.get(currentRow).get(i-j).getBody());
                    }
                    for(int j=countSame; j>=0; j--)
                    {
                        stateMatrix.get(currentRow).remove(i-countSame);
                    }
                    gameScore+=scoreIncremet*(countSame+1);
                    String sc = "SCORE:\n0" + gameScore;
                    score.setText(sc);
                    i=i-countSame;
                    removedBox=true;
                    rowPositions[currentRow]+=BoxNode.SIZE_Y*(countSame+1);
             //   }
            }
            if(removedBox)
            {
                Translate translate = new Translate();
                translate.setY(BoxNode.SIZE_Y*3);
                stateMatrix.get(currentRow).get(i).getTransforms().addAll(translate);
            }
        }
      
    }
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
