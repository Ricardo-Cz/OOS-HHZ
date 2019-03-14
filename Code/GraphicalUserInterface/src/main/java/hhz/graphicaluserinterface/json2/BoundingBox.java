/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface.json2;

/**
 *
 * @author Valerij
 */
public class BoundingBox
{
    private String top;

    private String left;

    private String width;

    private String height;

    public String getTop ()
    {
        return top;
    }

    public void setTop (String top)
    {
        this.top = top;
    }

    public String getLeft ()
    {
        return left;
    }

    public void setLeft (String left)
    {
        this.left = left;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "BoundingBox [top = "+top+", left = "+left+", width = "+width+", height = "+height+"]";
    }
}
