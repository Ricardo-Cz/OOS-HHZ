/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 *
 * @author Valerij
 */
public class WebServiceLED {
    public static void shelfLedMapping(String status, int row_id, int place_id){
        if(row_id == 0){
            webserviceCall(status,place_id);
        }
        if(row_id == 1){
           webserviceCall(status,place_id + 9); 
        }
        
    }

    public static void webserviceCall(String json, int id){
        String query_url = "http://10.0.101.20:4000/api/v1/groups/" + id +"/leds";
        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println(result);
            JSONObject myResponse = new JSONObject(result);
            System.out.println("jsonrpc- " + myResponse.getString("jsonrpc"));
            in.close();
            
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
