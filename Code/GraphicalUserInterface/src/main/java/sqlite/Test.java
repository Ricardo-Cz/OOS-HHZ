/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

/**
 *
 * @author Ricardo
 */
public class Test {
    public static void main(String[] args) {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        String settingTimeFromDB = dbc.handleGetDB2("product_name1",0,1,1);
        System.out.println(settingTimeFromDB);
    }
}
