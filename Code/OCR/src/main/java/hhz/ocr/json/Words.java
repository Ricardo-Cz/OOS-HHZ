/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.ocr.json;
import java.util.List;
/**
 *
 * @author Valerij
 */
public class Words
{
    private String confidence;

    private String text;

    private Integer[] boundingBox;

    public String getConfidence ()
    {
        return confidence;
    }

    public void setConfidence (String confidence)
    {
        this.confidence = confidence;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public Integer[] getBoundingBox ()
    {
        return boundingBox;
    }

    public void setBoundingBox (Integer[] boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    @Override
    public String toString()
    {
        return "Class Words [Confidence = "+confidence+", text = "+text+", boundingBox = "+boundingBox+"]";
    }
}