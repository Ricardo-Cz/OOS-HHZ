/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface.json;
/**
 *
 * @author Valerij
 */
public class BoundingBoxObject
{
    private RecognitionResult recognitionResult;

    private String status;

    public RecognitionResult getRecognitionResult ()
    {
        return recognitionResult;
    }

    public void setRecognitionResult (RecognitionResult recognitionResult)
    {
        this.recognitionResult = recognitionResult;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "Class BoundingBoxObject [recognitionResult = "+recognitionResult+", status = "+status+"]";
    }
}
