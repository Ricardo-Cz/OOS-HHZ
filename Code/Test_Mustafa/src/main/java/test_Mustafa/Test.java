package test_Mustafa;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Test {
    
    public static void main(String[] args) {
        
        //start method with path to JSON file
        ArrayList priceTagResult = priceTagAnalysis("./src/main/java/recources/test.json");
        for (int i = 0; i < priceTagResult.size(); i++) {
            System.err.println(priceTagResult.get(i));
        }
    }
    
    public static ArrayList priceTagAnalysis (String path){
        
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
            JSONObject generalJsonObject = (JSONObject) obj;
            
            //extract results from general JSON object
            JSONObject recognitionResult = (JSONObject) generalJsonObject.get("recognitionResult");
            JSONArray lines = (JSONArray) recognitionResult.get("lines");

            //outter loop for price tag groups
            while (lines.size()>0) { 
                JSONObject jObject = (JSONObject) lines.get(0);
                Long loopID = (Long) jObject.get("id");
                //inner loop for iteration of the boundary boxes of a price tag group
                for (int j = lines.size()-1; j >= 0; j--) {
                    JSONObject loopObject = (JSONObject) lines.get(j);
                    Long correlationID = (Long) loopObject.get("id");
                    if (Objects.equals(correlationID, loopID)) {
                        try {
                            //searchin for price of product
                            String text = (String) loopObject.get("text");
                            double potentialPrice = Double.parseDouble(text);
                            JSONArray boundaryBox = (JSONArray) loopObject.get("boundingBox");
                            //if format is plausible check for area of boundary box
                            Long boundaryBoxLeftBottomY = (Long) boundaryBox.get(1);
                            Long boundaryBoxLeftTopY = (Long) boundaryBox.get(7);
                            Long boundaryBoxLeftBottomX = (Long) boundaryBox.get(0);
                            Long boundaryBoxRightBottomX = (Long) boundaryBox.get(2);
                            Long boundaryBoxArea = (boundaryBoxLeftTopY-boundaryBoxLeftBottomY)*(boundaryBoxRightBottomX-boundaryBoxLeftBottomX);
                            //if area is bigger than previous result override
                            if (boundaryBoxArea > productPriceboundaryBoxArea) {
                                productPriceboundaryBoxArea = boundaryBoxArea;
                                productPrice = String.valueOf(potentialPrice);
                            }
                        }
                        catch (NumberFormatException e) {
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
                        } 
                        //search for second highest boundary box to save as productName2
                        else if (boundaryBoxLeftBottomY < productName2Y) {
                            productName2Y = boundaryBoxLeftBottomY;
                            productName2 = text;
                        }
                        lines.remove(j);
                    }
                }
                productPriceboundaryBoxArea = 0l;
                productName1Y = Long.MAX_VALUE;
                productName2Y = Long.MAX_VALUE;
                priceTagResult.add(productName1);
                priceTagResult.add(productName2);
                priceTagResult.add(productPrice);
            }
        }
        catch (IOException | ParseException e) {
        }
        return priceTagResult;
    } 
}
