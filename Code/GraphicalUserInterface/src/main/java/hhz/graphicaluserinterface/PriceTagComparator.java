/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlite.DBController;

/**
 *
 * @author Mustafa
 */
public class PriceTagComparator {

    public static final float PASS_THROUGH_PROBABILITY = (float) 60.00;
    private static final String PRICE_TAGS_TABLE = "price_tags";
    private static final String PRICE_TAGS_ROW = "";
    private static final int ROW_AMOUNT = 6;

    public static List<String> getProductNameAndPriceFromDb() {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        List<String> dataFromDB = dbc.handleSpecificRowsGetDB(PRICE_TAGS_TABLE, "shelf_id", "row_id", "place_id", "product_name1", "product_name2", "price");

        return dataFromDB;
    }

    public static List<String> getPriceTagsFromDB() {

        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        List<String> dataFromDB = dbc.handleAnyRowsGetDB(PRICE_TAGS_ROW, ROW_AMOUNT, PRICE_TAGS_TABLE);

        return dataFromDB;
    }

    public static ArrayList getProductTagsFromJson(String path) {
        ArrayList<String> productTagResult = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String productTagName = new String();
        try {
            //parse file from given path
            Object obj = parser.parse(new FileReader(path));
            //get JSON object from file
            JSONObject generalJsonObject = (JSONObject) obj;
            //extract results from general JSON object
            JSONArray predictions = (JSONArray) generalJsonObject.get("predictions");

            for (int j = predictions.size() - 1; j >= 0; j--) {
                JSONObject loopObject = (JSONObject) predictions.get(j);
                double probability = (double) loopObject.get("probability");
                if (probability * 100f >= PASS_THROUGH_PROBABILITY) {
                    //searching for tagName of product
                    productTagResult.add((String) loopObject.get("tagName"));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PriceTagComparator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PriceTagComparator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PriceTagComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productTagResult;
    }

    public static ArrayList getpriceTagsFromJson(String path) {

        JSONParser parser = new JSONParser();
        ArrayList<String> priceTagResult = new ArrayList<>();
        String productName1 = new String();
        String productName2 = new String();
        String productPrice = new String();
        Long productPriceboundaryBoxArea = 0l;
        Long productName1Y = Long.MAX_VALUE;
        Long productName2Y = Long.MAX_VALUE;

        try {

            //parse file from given path
            Object obj = parser.parse(new FileReader(path));

            //get JSON object from file
            JSONArray generalJsonArray = (JSONArray) obj;
            for (int i = 0; i < generalJsonArray.size(); i++) {
                JSONObject generalJsonObject = (JSONObject) generalJsonArray.get(i);

                //extract results from general JSON object
                JSONObject recognitionResult = (JSONObject) generalJsonObject.get("recognitionResult");
                JSONArray lines = (JSONArray) recognitionResult.get("lines");

                //outter loop for price tag groups
                while (lines.size() > 0) {
                    JSONObject jObject = (JSONObject) lines.get(0);
                    Long loopID = (Long) jObject.get("id");
                    //inner loop for iteration of the boundary boxes of a price tag group
                    for (int j = lines.size() - 1; j >= 0; j--) {
                        JSONObject loopObject = (JSONObject) lines.get(j);
                        Long correlationID = (Long) loopObject.get("id");
                        if (Objects.equals(correlationID, loopID)) {
                            try {
                                //searching for price of product
                                String text = (String) loopObject.get("text");
                                double potentialPrice = Double.parseDouble(text);
                                JSONArray boundaryBox = (JSONArray) loopObject.get("boundingBox");
                                //if format is plausible check for area of boundary box
                                Long boundaryBoxLeftBottomY = (Long) boundaryBox.get(1);
                                Long boundaryBoxLeftTopY = (Long) boundaryBox.get(7);
                                Long boundaryBoxLeftBottomX = (Long) boundaryBox.get(0);
                                Long boundaryBoxRightBottomX = (Long) boundaryBox.get(2);
                                Long boundaryBoxArea = (boundaryBoxLeftTopY - boundaryBoxLeftBottomY) * (boundaryBoxRightBottomX - boundaryBoxLeftBottomX);
                                //if area is bigger than previous result override
                                if (boundaryBoxArea > productPriceboundaryBoxArea) {
                                    productPriceboundaryBoxArea = boundaryBoxArea;
                                    productPrice = String.valueOf(potentialPrice);
                                }
                            } catch (NumberFormatException e) {
                            }
                            //search for highest boundary box to save as productName1
                            String text = (String) loopObject.get("text");
                            JSONArray boundaryBox = (JSONArray) loopObject.get("boundingBox");
                            Long boundaryBoxLeftBottomY = (Long) boundaryBox.get(1);
                            if (boundaryBoxLeftBottomY < productName1Y) {
                                productName2Y = productName1Y;
                                productName1Y = boundaryBoxLeftBottomY;
                                productName2 = productName1;
                                productName1 = text;
                            } //search for second highest boundary box to save as productName2
                            else if (boundaryBoxLeftBottomY < productName2Y) {
                                productName2Y = boundaryBoxLeftBottomY;
                                productName2 = text;
                            }
                            lines.remove(j);
                        }
                    }

                    //check if product has two names
                    DBController dbc = DBController.getInstance();
                    dbc.initDBConnection();
                    boolean bool = dbc.handleCheckSecondName(productName1);
                    if (bool) {
                        productName2 = "";
                    }

                    productPriceboundaryBoxArea = 0l;
                    productName1Y = Long.MAX_VALUE;
                    productName2Y = Long.MAX_VALUE;
                    priceTagResult.add(productName1);
                    priceTagResult.add(productName2);
                    priceTagResult.add(productPrice);
                }
            }
        } catch (IOException | ParseException e) {
        }
        return priceTagResult;
    }

    public Map<String, String> priceTagComparator(String path) {
        List<String> dbPriceTagData = getProductNameAndPriceFromDb();
        for (String ans : dbPriceTagData) {
            System.out.println(ans);
        }
        System.out.println("\n\n\n\n");
        ArrayList jsonPriceTagData = getpriceTagsFromJson(path);
        for (int i = 0; i < jsonPriceTagData.size(); i++) {
            System.out.println(jsonPriceTagData.get(i));
        }
        return null;
    }

   /* public static void main(String args[]) {
        PriceTagComparator obj = new PriceTagComparator();
        ArrayList<String> resultProductRecog = obj.getProductTagsFromJson("C:\\Users\\Valerij\\Desktop\\Projekt 2\\OCR\\0_1_3\\IMG_3795_.json");
        for (String str : resultProductRecog) {
            System.out.println(str);
        }
    }*/
}
