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
public class Created
{
    private IChronology iChronology;

    private String iMillis;

    public IChronology getIChronology ()
    {
        return iChronology;
    }

    public void setIChronology (IChronology iChronology)
    {
        this.iChronology = iChronology;
    }

    public String getIMillis ()
    {
        return iMillis;
    }

    public void setIMillis (String iMillis)
    {
        this.iMillis = iMillis;
    }

    @Override
    public String toString()
    {
        return "Created [iChronology = "+iChronology+", iMillis = "+iMillis+"]";
    }
}
