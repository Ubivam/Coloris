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
public class BoxNode extends Group
{
    public static final int SIZE_X = (int)coloris_rg1.main.Coloris_RG1.WINDOW_WIDTH/14;
    public static final int SIZE_Y = (int)coloris_rg1.main.Coloris_RG1.WINDOW_WIDTH/14;
    public static final Color[] colors = {Color.WHITE, Color.PURPLE, Color.RED, Color.YELLOW, Color.CYAN};
    protected Color color;
    protected Rectangle body;

    public Rectangle getBody()
    {
        return body;
    }

    public void setBody(Rectangle body)
    {
        this.body = body;
    }
    
    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
        body.setFill(color);
    }
    public BoxNode()
    {
        color = colors[(int) (Math.random() * colors.length)];
        body = new Rectangle(0,0,SIZE_X,SIZE_Y);
        body.setFill(color);
        body.setStroke(Color.BLACK);
        getChildren().addAll(body);
    }
    
}
