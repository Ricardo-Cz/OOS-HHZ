/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.ocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Valerij
 */
//load and convert images to binary from directory
public class GraphicHelper {

    public Map<String, byte[]> getImages(String rootPath) throws IOException {

        ArrayList<File> files = getPaths(new File(rootPath),
                new ArrayList<File>());

        if (files == null) {
            return null;
        }
        byte[] fileContent = null;
        Map<String, byte[]> imageMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {

            if (getFileExtension(files.get(i)).equals("jpg") ||getFileExtension(files.get(i)).equals("JPG") ) {
                System.out.println(files.get(i).getCanonicalPath());
                fileContent = Files.readAllBytes(files.get(i).toPath());
                imageMap.put(files.get(i).getCanonicalPath(), fileContent);
            }
        }
       
        //print(files);
        return imageMap;
    }

    //Recursive directory lookup
    private static ArrayList<File> getPaths(File file, ArrayList<File> list) {
        if (file == null || list == null || !file.isDirectory()) {
            return null;
        }
        File[] fileArr = file.listFiles();
        for (File f : fileArr) {
            if (f.isDirectory()) {
                getPaths(f, list);
            }
            list.add(f);
        }
        return list;
    }

    private static void print(ArrayList<File> files) {
        if (files == null) {
            return;
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                System.out.println(files.get(i).getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get Extension from File
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    void WriteJsonToFile(JSONObject json, String filePath) {

        if (filePath.lastIndexOf(".") != -1 && filePath.lastIndexOf(".") != 0) {
            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".json";

            try {
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                System.out.println("Json erfolgreich im Verzeichnis: " + filePath + " abgespeichert.");

            } catch (IOException ex) {
                Logger.getLogger(GraphicHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    static JSONObject ReadJsonFromFile(String filePath){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try{
        Object obj = parser.parse(new FileReader(filePath));
        
            jsonObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) { 
            e.printStackTrace();
        }
        return jsonObject;
    }
}
