/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.js150411.coloris.main;

import javafx.scene.shape.Box;
import org.jetbrains.annotations.NotNull;
import rs.ac.bg.etf.js150411.coloris.sprites.Background;
import rs.ac.bg.etf.js150411.coloris.sprites.BoxNode;
import rs.ac.bg.etf.js150411.coloris.sprites.GameSpace;
import rs.ac.bg.etf.js150411.coloris.sprites.StickElement;

import java.util.ArrayList;
import java.util.Iterator;

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
 * @author Admin
 */
public class Coloris_RG1 extends Application {

    public static final double WINDOW_WIDTH = 600;
    public static final double WINDOW_HEIGHT = 800;
    public static long GAME_SPEED = 400_000_000;
    public int gameScore = 0;
    public int scoreIncremet = 100;
    public int highScore = 32000;
    public boolean isGameOwer = false;

    private static Group root;
    private static Text score;
    private static Text hScore;
    private AnimationTimer timer;

    private ArrayList<ArrayList<BoxNode>> stateMatrix;

    private int[] rowPositions;
    private int currentPositionX;
    private int currentPositionY;
    private int currentRow;
    private StickElement currentNode;
    private StickElement nextNode;
    private boolean isStickActive = true;
    private boolean isStickCreated;
    private boolean lockInteraction = false;

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        score = new Text();
        score.setTranslateX(WINDOW_WIDTH / 2 + 50);
        score.setTranslateY(WINDOW_HEIGHT / 2);
        score.setFill(Color.BLUEVIOLET);
        String sc = "SCORE:\n0" + gameScore;
        score.setText(sc);
        score.setFont(Font.font("Impact", FontWeight.BOLD, 50));

        hScore = new Text();
        hScore.setTranslateX(WINDOW_WIDTH / 2 + 50);
        hScore.setTranslateY(WINDOW_HEIGHT * 3 / 4);
        hScore.setFill(Color.RED);
        String hSc = "HIGH SCORE:\n0" + highScore;
        hScore.setText(hSc);
        hScore.setFont(Font.font("Impact", FontWeight.BOLD, 30));

        final ImageView selectedImage = new ImageView();
        ImageView imageView = new ImageView();
        Image image = new Image("file:Coloris.png");
        imageView.setImage(image);
        rowPositions = new int[7];
        stateMatrix = new ArrayList<ArrayList<BoxNode>>();
        for (int i = 0; i < 7; i++) {
            rowPositions[i] = (int) WINDOW_HEIGHT - 142;
            stateMatrix.add(new ArrayList<BoxNode>());
        }
        nextNode = new StickElement();
        Background bg = new Background(WINDOW_WIDTH, WINDOW_HEIGHT);
        GameSpace gs = new GameSpace(WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().addAll(bg, gs, score, hScore, nextNode, imageView);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, d -> OnKeyPressed(d));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, d -> OnKeyRelased(d));
        primaryStage.setTitle("Coloris Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= GAME_SPEED) {
                    update(root);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    public void OnKeyRelased(@NotNull KeyEvent e) {
        if (e.getCode() == KeyCode.SHIFT) {
            GAME_SPEED = 400_000_000;
        }
    }

    public void OnKeyPressed(@NotNull KeyEvent e) {
        if (!isGameOwer && !lockInteraction) {
            switch (e.getCode()) {
                case UP:
                    currentNode.RotateElementsUP();
                    break;

                case DOWN:
                    currentNode.RotateElementsDown();
                    break;

                case LEFT:
                    if (currentPositionX != 0) {
                        if (currentPositionY < rowPositions[currentRow - 1]) {
                            currentPositionX -= BoxNode.SIZE_X;
                            currentRow -= 1;
                            currentNode.setTranslateX(currentPositionX);
                        }
                    }
                    break;
                case RIGHT:
                    if (currentPositionX != 6 * BoxNode.SIZE_X) {
                        if (currentPositionY < rowPositions[currentRow + 1]) {
                            currentPositionX += BoxNode.SIZE_X;
                            currentRow += 1;
                            currentNode.setTranslateX(currentPositionX);
                        }
                    }
                    break;
                case SHIFT:
                    GAME_SPEED = 100_000_000;
                    break;

            }
        }
    }

    public void update(Group g) {
        if (!isGameOwer) {
            if (!isStickCreated) {
                isStickCreated = true;
                currentNode = nextNode;
                nextNode = new StickElement();
                nextNode.setTranslateX(WINDOW_WIDTH / 2 + 50);
                nextNode.setTranslateY(100);
                currentRow = (int) (Math.random() * rowPositions.length);
                currentPositionX = currentRow * BoxNode.SIZE_X;
                currentPositionY = (int) WINDOW_HEIGHT - 100 - 15 * BoxNode.SIZE_Y;
                currentNode.setTranslateX(currentPositionX);
                currentNode.setTranslateY(currentPositionY);
                g.getChildren().addAll(nextNode);
                lockInteraction = false;
            }
            currentPositionY += BoxNode.SIZE_Y;
            currentNode.setTranslateY(currentPositionY);

            if (currentPositionY == rowPositions[currentRow]) {
                lockInteraction = true;
                isStickCreated = false;
                rowPositions[currentRow] -= 3 * BoxNode.SIZE_Y;
                AddRowToTheMatrix(currentNode);

                boolean calculated = UpdateScore();
                while(calculated) {
                    calculated = UpdateScore();
                }
                //End Game Check
                if (rowPositions[currentRow] == (int) WINDOW_HEIGHT - 142 - BoxNode.SIZE_Y * 15) {
                    isGameOwer = true;
                }
            }
        }
    }

    private void AddRowToTheMatrix(@NotNull StickElement cNode) {
        stateMatrix.get(currentRow).add(cNode.getBottom());
        stateMatrix.get(currentRow).add(cNode.getMiddle());
        stateMatrix.get(currentRow).add(cNode.getTop());
    }

    private boolean UpdateScore() {
        boolean result = false;
        Color current_color;
        int countColor;
        int beginColorLoc;
        int numberOfBoxes;
        int current_row = -1;
        for (var row : stateMatrix) {
            Iterator<BoxNode> it = row.iterator();
            current_color = null;
            countColor = 0;
            beginColorLoc = 0;
            numberOfBoxes = 0;
            current_row++;
            while (it.hasNext()) {
                var box = it.next();
                if (current_color == null) {
                    current_color = box.getColor();
                    countColor = 1;
                    beginColorLoc = numberOfBoxes;
                    numberOfBoxes++;
                    continue;
                }
                if (current_color == box.getColor()) {
                    countColor++;
                    numberOfBoxes++;
                } else {
                    if (countColor >= 3) {
                        gameScore += scoreIncremet * (countColor);
                        String sc = "SCORE:\n0" + gameScore;
                        score.setText(sc);
                        for (int i = beginColorLoc; i < numberOfBoxes; i++) {
                            row.get(i).getChildren().remove(row.get(i).getBody());
                        }
                        for (int i = numberOfBoxes; i < row.size(); i++) {
                            Translate translate = new Translate();
                            translate.setY(BoxNode.SIZE_Y * countColor);
                            row.get(i).getTransforms().addAll(translate);
                        }
                        for (int i = beginColorLoc; i < numberOfBoxes; i++) {
                            row.remove(beginColorLoc);
                        }
                        rowPositions[current_row] += countColor * BoxNode.SIZE_Y;
                        result = true;
                        break;
                    }
                    countColor = 1;
                    current_color = box.getColor();
                    beginColorLoc = numberOfBoxes;
                    numberOfBoxes++;
                }
            }
        }
        for (int i = 0; i < 15; i++) {
            current_color = null;
            countColor = 0;
            beginColorLoc = 0;
            numberOfBoxes = 0;
            int rowNumber = -1;
            boolean foundSequence = false;
            for (var row : stateMatrix) {
                rowNumber++;
                if (row.size() <= i) {
                    current_color = null;
                    if (foundSequence) {
                        removeHorizontal(countColor, beginColorLoc, rowNumber, i);
                        result = true;
                        countColor = 0;
                        foundSequence = false;
                    }
                    continue;
                }
                var box = row.get(i);
                if (current_color == null) {
                    current_color = box.getColor();
                    countColor = 1;
                    beginColorLoc = rowNumber;
                    numberOfBoxes++;
                    continue;
                }
                if (current_color == box.getColor()) {
                    countColor++;
                    numberOfBoxes++;
                    if (countColor >= 3) {
                        foundSequence = true;
                        if (rowNumber == 6) {
                            removeHorizontal(countColor, beginColorLoc, rowNumber + 1, i);
                            result = true;
                        }
                    }
                } else {
                    if (countColor >= 3) {
                        removeHorizontal(countColor, beginColorLoc, rowNumber, i);
                        result = true;
                    }
                    countColor = 1;
                    current_color = box.getColor();
                    beginColorLoc = rowNumber;
                    numberOfBoxes++;
                }

            }
        }
        return result;
    }

    private void removeHorizontal(int countColor, int beginColorLoc, int rowNumber, int i) {
        gameScore += scoreIncremet * (countColor);
        String sc = "SCORE:\n0" + gameScore;
        score.setText(sc);
        for (int j = beginColorLoc; j < rowNumber; j++) {
            stateMatrix.get(j).get(i).getChildren().remove(stateMatrix.get(j).get(i).getBody());
            rowPositions[j] += BoxNode.SIZE_Y;
        }
        for (int j = beginColorLoc; j < rowNumber; j++) {
            for (int c = i; c < stateMatrix.get(j).size(); c++) {
                Translate translate = new Translate();
                translate.setY(BoxNode.SIZE_Y);
                stateMatrix.get(j).get(c).getTransforms().addAll(translate);
            }
        }
        for (int j = beginColorLoc; j < rowNumber; j++) {
            stateMatrix.get(j).remove(i);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
