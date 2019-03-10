/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

import hhz.graphicaluserinterface.MessagingBotAPI;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *
 * @author Ricardo
 */
public class Test {
    public static void main(String[] args) {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
         
        dbc.handleUpdateDB2("status_name",0,0,0,"Fehlplatzierung");
        //String settingTimeFromDB = dbc.handleGetDB2("product_name1",0,1,1);
        //System.out.println(settingTimeFromDB);
    }
}
