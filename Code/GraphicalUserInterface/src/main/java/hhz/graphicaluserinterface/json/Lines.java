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
public class Lines
{
     private Integer[] boundingBox;

    private Words[] words;

    private String text;

    public Integer[] getBoundingBox ()
    {
        return boundingBox;
    }

    public void setBoundingBox (Integer[] boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public Words[] getWords ()
    {
        return words;
    }

    public void setWords (Words[] words)
    {
        this.words = words;
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
        return "Class Lines [text = "+text+", boundingBox = "+boundingBox+", words = "+words+"]";
    }
}
