/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package color_rg1.sprites;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class GameSpace extends Group
{ 
       public GameSpace(double width, double height){
        Rectangle background = new Rectangle(0, 100, width/2, height-100);
        Rectangle floor = new Rectangle(0,height-15, width/2,15);
        floor.setFill(Color.GRAY);
        background.setFill(Color.DARKBLUE);
        background.setStroke(Color.WHITE);
        getChildren().addAll(background,floor);
    }
}
