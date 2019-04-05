/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author Valerij
 */

public class WebServiceLED {
    public static final Map<String,String> MAP;
    static{
        Hashtable<String,String> tmp = 
            new Hashtable<String,String>();
        //Erste Reihe
        tmp.put("006","bio_spaghetti");
        tmp.put("005","k-classic_spaghetti");
        tmp.put("A","k-classic_spaghetti_1");
        tmp.put("004","bio-penne_rigate");
        tmp.put("003","k-classic_vollkornbutterkeks");
        tmp.put("002","k-classic_butterkeks");
        tmp.put("001","k-classic_Schokoriegel_Mischung");
        tmp.put("000","k-classic_Spritzgebaeck-Kokos");
        //Zweite Reihe
        tmp.put("018","barilla_gemelli");
        tmp.put("017","barilla_tortiglioni");
        tmp.put("016","barilla_spaghetti_no5");
        tmp.put("015","barilla_toskana_kraeuter");
        tmp.put("014","knorr_tomato_al_gusto");
        tmp.put("013","thomy_les_sauces_hollandaise");
        tmp.put("012","haribo_tropi_frutti");
        tmp.put("011","haribo_goldbaer_minis");
        tmp.put("010","haribo_goldbaeren");
       
//        tmp.put("A","lasagne_gruen");
//        tmp.put("A","lasagne_gelb");
//        tmp.put("A","gaggli_frischeier_spaetzle");
//        tmp.put("A","ritter_sport_schokowuerfel");
//        tmp.put("A","werthers_original");
//        tmp.put("A","hanuta_minis");
//        tmp.put("A","kinder_schoko_bons");
//        tmp.put("A","bandnudeln_mit_ei");
//        tmp.put("A","korkenzieher_mit_ei");
//        tmp.put("A","tortelloni_tomate_mozzarella");
//        tmp.put("A","stapelchips_chilli");
//        tmp.put("A","knabber_mischung_8-fach_sortiert");
//        tmp.put("A","tortilla_chips_salz");
        
        MAP = Collections.unmodifiableMap(tmp);
    }
    
    public static void webserviceCall(String json, String rowAndPlaceId){
        try {
            JSONObject jsonObject = sendGet();
            String href = mapping(jsonObject, rowAndPlaceId);
            if(href != null) {
            sentPost(json, href);
            }
        } catch (Exception ex) {
            Logger.getLogger(WebServiceLED.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static void sentPost(String json, String href) throws Exception {
    String query_url = "http://10.0.101.20:4000" +href+ "/leds";
            
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
            int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
            
            in.close();
            
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    private static final String USER_AGENT = "Mozilla/5.0";
    private static JSONObject sendGet() throws Exception {

		String url = "http://10.0.101.20:4000/api/v1/groups";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
                
//		BufferedReader in = new BufferedReader(
//		        new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
               // read the response
            InputStream in = new BufferedInputStream(con.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println(result);
            JSONObject myResponse = new JSONObject(result);
            in.close();
		//print result
//		System.out.println(response.toString());
                return myResponse;

	}
   private static String mapping(JSONObject obj, String rowAndPlaceId){
       
       String name = MAP.get(rowAndPlaceId);
       JSONArray groups = (JSONArray) obj.get("groups");
       for (int i = 0; i <= groups.length(); i++) {
          JSONObject loopObject = (JSONObject) groups.get(i);
           String productName = (String) loopObject.get("name");
              if (productName.equals(name)) {
                  JSONObject child = (JSONObject) loopObject.get("self");
                  return (String) child.get("href");
              }
       }
       return null;
   }
   
   
   
   
//    public static void shelfLedMapping(String status, int row_id, int place_id){
//        if(row_id == 0){
//            webserviceCall(status,(place_id-7) *(-1));
//        }
//        if(row_id == 1){
//           webserviceCall(status,place_id + 9); 
//        }
//        
//    }

//    public static void webserviceCall(String json, int id){
//        String query_url = "http://10.0.101.20:4000/api/v1/groups/" + id +"/leds";
//        try {
//            URL url = new URL(query_url);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            OutputStream os = conn.getOutputStream();
//            os.write(json.getBytes("UTF-8"));
//            os.close();
//            // read the response
//            InputStream in = new BufferedInputStream(conn.getInputStream());
//            String result = IOUtils.toString(in, "UTF-8");
//            System.out.println(result);
//            JSONObject myResponse = new JSONObject(result);
//            System.out.println("jsonrpc- " + myResponse.getString("jsonrpc"));
//            in.close();
//            
//            conn.disconnect();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
}
