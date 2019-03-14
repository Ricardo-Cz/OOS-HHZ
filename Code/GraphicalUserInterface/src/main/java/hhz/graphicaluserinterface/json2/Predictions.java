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
public class Predictions
{
    private BoundingBox boundingBox;

    private String tagId;

    private String probability;

    private String tagName;

    public BoundingBox getBoundingBox ()
    {
        return boundingBox;
    }

    public void setBoundingBox (BoundingBox boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public String getTagId ()
    {
        return tagId;
    }

    public void setTagId (String tagId)
    {
        this.tagId = tagId;
    }

    public String getProbability ()
    {
        return probability;
    }

    public void setProbability (String probability)
    {
        this.probability = probability;
    }

    public String getTagName ()
    {
        return tagName;
    }

    public void setTagName (String tagName)
    {
        this.tagName = tagName;
    }

    @Override
    public String toString()
    {
        return "Predictions [boundingBox = "+boundingBox+", tagId = "+tagId+", probability = "+probability+", tagName = "+tagName+"]";
    }
}
