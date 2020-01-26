/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.js150411.coloris.sprites;

import static rs.ac.bg.etf.js150411.coloris.sprites.BoxNode.colors;

public class StickElement extends Sprite
{
    private BoxNode top,middle,bottom;

    public BoxNode getTop()
    {
        return top;
    }

    public void setTop(BoxNode top)
    {
        this.top = top;
    }

    public BoxNode getMiddle()
    {
        return middle;
    }

    public void setMiddle(BoxNode middle)
    {
        this.middle = middle;
    }

    public BoxNode getBottom()
    {
        return bottom;
    }

    public void setBottom(BoxNode bottom)
    {
        this.bottom = bottom;
    }

    public StickElement()
    {
        top = new BoxNode();
        middle = new BoxNode();
        bottom = new BoxNode();
        while(top.getColor().equals(middle.getColor()))
        {
            top.setColor(BoxNode.colors[(int) (Math.random() * BoxNode.colors.length)]);
        }
        middle.setTranslateY(BoxNode.SIZE_Y);
        bottom.setTranslateY(2*BoxNode.SIZE_Y);
        getChildren().addAll(top,middle,bottom);
    }
    
    public void RotateElementsUP()
    {
        top.setTranslateY(2*BoxNode.SIZE_Y);
        middle.setTranslateY(0);
        bottom.setTranslateY(BoxNode.SIZE_Y);
        BoxNode temp;
        temp=top;
        top=middle;
        middle=bottom;
        bottom=temp;
    }
    
    public void RotateElementsDown()
    {
        top.setTranslateY(BoxNode.SIZE_Y);
        middle.setTranslateY(2*BoxNode.SIZE_Y);
        bottom.setTranslateY(0);
        BoxNode temp;
        temp=bottom;
        bottom = middle;
        middle = top;
        top= temp;
    }
    

    @Override
    public void update()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
