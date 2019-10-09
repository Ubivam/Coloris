/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package color_rg1.sprites;


import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class Background extends Group
{
    public Background(double width, double height){
        Rectangle background = new Rectangle(0, 0, width + 10, height + 10);
        background.setFill(Color.BLACK);
        getChildren().add(background);
    }
}
