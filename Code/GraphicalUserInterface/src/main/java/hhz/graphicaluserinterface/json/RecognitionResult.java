/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface.json;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valerij
 */
public class RecognitionResult
{
    private List<Lines> lines;

     public List<Lines> getLines ()
    {
        return lines;
    }
     
     public void setLines (List<Lines> lines)
    {
        this.lines = lines;
    }
    @Override
    public String toString()
    {
        return "Class RecognitionResult [lines = "+lines+"]";
    }
}
