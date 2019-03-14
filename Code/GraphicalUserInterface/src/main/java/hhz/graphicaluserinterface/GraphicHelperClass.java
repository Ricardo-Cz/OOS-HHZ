/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import hhz.graphicaluserinterface.json.BoundingBoxObject;
import hhz.graphicaluserinterface.json.Lines;
import hhz.graphicaluserinterface.json2.MainObject;
import hhz.graphicaluserinterface.json2.Predictions;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Valerij
 */

public class GraphicHelperClass extends JFrame {

    public static final double scaleFactor = 0.2;
    Polygon P;

    //Constructor
    public GraphicHelperClass() {
    }

    public static int[] ListToIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        Iterator<Integer> iter = list.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            ret[i] = iter.next();
        }
        return ret;
    }

    public static List<Integer> ExtractLineBoundingBox(ArrayList<BoundingBoxObject> bbo) {

        List<Integer> coordinats = new ArrayList();
        for (BoundingBoxObject b : bbo) {
            for (Lines line : b.getRecognitionResult().getLines()) {
                for (Integer boundingBox : line.getBoundingBox()) {
                    coordinats.add(boundingBox);
                }
            }
        }
        System.out.println(coordinats);
        return coordinats;
    }
    public static List<Double> ExtractCvBoundingBox(MainObject obj) {
        List<Double> coordinats = new ArrayList();
        for (Predictions p : obj.getPredictions()) {
            float probability = Float.parseFloat(p.getProbability()) * 100f;
            if (probability >= PriceTagComparator.PASS_THROUGH_PROBABILITY) {
                coordinats.add(Double.parseDouble(p.getBoundingBox().getLeft()));
                coordinats.add(Double.parseDouble(p.getBoundingBox().getTop()));
                coordinats.add(Double.parseDouble(p.getBoundingBox().getWidth()));
                coordinats.add(Double.parseDouble(p.getBoundingBox().getHeight()));
            }
        }
        return coordinats;
    }
    public static List<Integer> ExtractTheXCoordinates(List<Integer> wholeList) {
        List<Integer> xCoordinates = new ArrayList<Integer>();
        for (int i = 0; i < wholeList.size(); i++) {
            if ((i % 2) == 0 || i == 0) {
                xCoordinates.add((int) (wholeList.get(i) * Home.newScaleFactorWidth));
            }
        }
        System.out.println(xCoordinates);
        return xCoordinates;
    }

    public static List<Integer> ExtractTheYCoordinates(List<Integer> wholeList) {
        List<Integer> yCoordinates = new ArrayList<Integer>();
        for (int i = 0; i < wholeList.size(); i++) {
            if ((i % 2) == 1) {
                // yCoordinates.add((int)((wholeList.get(i) + FileHelperClass.SUB_IMAGE_Y_VALUE) / Home.newScaleFactorHeight));
                yCoordinates.add((int) ((wholeList.get(i)) * Home.newScaleFactorHeight));
            }
        }
        System.out.println(yCoordinates);
        return yCoordinates;
    }

    public static String ExtractLineText(ArrayList<BoundingBoxObject> bbo) {
        String text = "";
        int counter = 1;
        for (BoundingBoxObject b : bbo) {
            for (Lines line : b.getRecognitionResult().getLines()) {
                text += counter + ") " + line.getText() + "\n";
                counter++;
            }
        }
        System.out.println(text);
        return text;
    }

    public static String ExtractCvLineText(MainObject obj) {
        String text = "";
        int counter = 1;
        DecimalFormat df = new DecimalFormat("0.00");
        for (Predictions p : obj.getPredictions()) {
            float probability = Float.parseFloat(p.getProbability()) * 100f;
            if (probability >= PriceTagComparator.PASS_THROUGH_PROBABILITY) {
                text += counter + ") " + p.getTagName() + " [" + df.format(probability) + "%]" + "\n";
                counter++;
            }
        }
        return text;
    }

    public static ArrayList<BoundingBoxObject> getInitializedBoundingBoxObject(String jsonPath) {
        Gson gson = new Gson();
        JSONArray json = FileHelperClass.ReadJsonFromFile(jsonPath);
        // return gson.fromJson(json.toString(), BoundingBoxObject.class);
        java.lang.reflect.Type listType = new TypeToken<List<BoundingBoxObject>>() {
        }.getType();
        return gson.fromJson(json.toString(), listType);
    }

    public static MainObject getInitializedMainObject(String jsonPath) {
        Gson gson = new Gson();
        JSONObject json = FileHelperClass.ReadJsonObjectFromFile(jsonPath);
        // java.lang.reflect.Type listType = new TypeToken<List<MainObject>>(){}.getType();
        return gson.fromJson(json.toString(), MainObject.class);
    }
}
