/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.util.List;
import sqlite.DBController;

/**
 *
 * @author Valerij
 */
public class PriceTagComparator {
    private static final String PRICE_TAGS_TABLE = "price_tags";
    private static final String PRICE_TAGS_ROW = "";
    private static final int ROW_AMOUNT = 13;
    
    public static void getPriceTagsFromDB(){
        
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        List<String> getDataFromDB = dbc.handleAnyRowsGetDB(PRICE_TAGS_ROW, ROW_AMOUNT, PRICE_TAGS_TABLE);
        for(String s : getDataFromDB){
        System.out.println(s);
        }
    }
    
}
