/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Valerij
 */

//load and convert images to binary from directory
public class FileHelperClass {
    public static final int SUB_IMAGE_Y_VALUE = 0;//2200;
    public static Map<String, byte[]> getImages(String rootPath) throws IOException {

        ArrayList<File> files = getPaths(new File(rootPath),
                new ArrayList<File>());

        if (files == null) {
            return null;
        }
        
        byte[] fileContent = null;
        Map<String, byte[]> imageMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {

            if (getFileExtension(files.get(i)).toLowerCase().equals("jpg")) {
                fileContent = getSubBytesFromImage(files.get(i));
               // fileContent = Files.readAllBytes(files.get(i).toPath());
                imageMap.put(files.get(i).getCanonicalPath(), fileContent);
            }
        }
       
        //print(files);
        return imageMap;
    }
    private static byte[] getSubBytesFromImage(File file){
        byte[] byteArray = null;
        try {
            BufferedImage img = ImageIO.read(file);
            BufferedImage newImg = img.getSubimage(0, SUB_IMAGE_Y_VALUE, img.getWidth(), img.getHeight() - SUB_IMAGE_Y_VALUE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImg, "jpg", baos );
            baos.flush();
            byteArray = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(FileHelperClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArray;
    }
    //Recursive directory lookup
    public static ArrayList<File> getPaths(File file, ArrayList<File> list) {
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
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static long getFileCreationEpoch(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.creationTime().toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

    public static String getFileCreationTime(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.creationTime().toString();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }
    
    public static File[] sortPathByFolder(String directoryPath) {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles();
        Arrays.sort(files, (File f1, File f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            } else if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            } else {
                return f1.compareTo(f2);
            }
        });
        return files;
    }

    public static void sortFilesByDateCreated(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f2, File f1) {
                long l1 = FileHelperClass.getFileCreationEpoch(f1);
                long l2 = FileHelperClass.getFileCreationEpoch(f2);
                return Long.valueOf(l1).compareTo(l2);
            }
        });
    }
    public List<String> ChangeFileExtensionToDotJson(List<String> filePaths){
        List<String> newFilePathList = new ArrayList();
        for(String str: filePaths){
        if (str.lastIndexOf(".") != -1 && str.lastIndexOf(".") != 0) {
            str = str.substring(0, str.lastIndexOf(".")) + ".json";
            newFilePathList.add(str);
       }
      }
        return newFilePathList;
    }
    String setPostfixToPathName(String filePath){
         if (filePath.lastIndexOf(".") != -1 && filePath.lastIndexOf(".") != 0) {
             String extension = filePath.substring(filePath.lastIndexOf("."), filePath.length());
             filePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_" + extension;
         }
         return filePath;
    }
    void WriteJsonToFile(String json, String filePath) {

        if (filePath.lastIndexOf(".") != -1 && filePath.lastIndexOf(".") != 0) {
            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".json";

            try {
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                System.out.println("Json erfolgreich im Verzeichnis: " + filePath + " abgespeichert.");

            } catch (IOException ex) {
                Logger.getLogger(FileHelperClass.class.getName()).log(Level.SEVERE, null, ex);
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

    List<String> sortPathsByTheFolderAndCreationTime(String directoryPath) throws IOException {
        //sort by directory
        File[] files = sortPathByFolder(directoryPath);
        //fill the orderedlist as Path object
        List<File[]> orderedFileListByDirAndCreationTime = new ArrayList();
        for (int i = 0; i < files.length; i++) {
            if (FileHelperClass.getFileExtension(files[i]).equals("")) {
                orderedFileListByDirAndCreationTime.add(files[i].listFiles());
                
            }
        }
        if (!orderedFileListByDirAndCreationTime.isEmpty()) {
            for (int i = 0; i < orderedFileListByDirAndCreationTime.size(); i++) {
                sortFilesByDateCreated(orderedFileListByDirAndCreationTime.get(i));
            }
            List<String> resultList = new ArrayList();
            for (File[] f : orderedFileListByDirAndCreationTime) {
                for (File file : f) {
                    if (FileHelperClass.getFileExtension(file).toLowerCase().equals("jpg")) {
                        resultList.add(file.getAbsolutePath());
                    }
                }
            }
            return resultList;
        } else {
            return null;
        }
    }

    public static String getDirectoryName(String path) {
        if (path.lastIndexOf("\\") != -1 && path.lastIndexOf("\\") != 0) {
            return path.substring(path.lastIndexOf("\\") - 4, path.lastIndexOf("\\"));
        } else {
            return "";
        }
    }
}
