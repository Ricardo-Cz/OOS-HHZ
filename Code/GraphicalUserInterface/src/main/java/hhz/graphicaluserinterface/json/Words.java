/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface.json;
import java.util.List;
/**
 *
 * @author Valerij
 */
public class Words
{
    private String[] boundingBox;

    private String text;

    public String[] getBoundingBox ()
    {
        return boundingBox;
    }

    public void setBoundingBox (String[] boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "Class Words [text = "+text+", boundingBox = "+boundingBox+"]";
    }
}