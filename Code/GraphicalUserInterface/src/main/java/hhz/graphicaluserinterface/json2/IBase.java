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
public class IBase
{
    private String iMinDaysInFirstWeek;

    public String getIMinDaysInFirstWeek ()
    {
        return iMinDaysInFirstWeek;
    }

    public void setIMinDaysInFirstWeek (String iMinDaysInFirstWeek)
    {
        this.iMinDaysInFirstWeek = iMinDaysInFirstWeek;
    }

    @Override
    public String toString()
    {
        return "IBase [iMinDaysInFirstWeek = "+iMinDaysInFirstWeek+"]";
    }
}
