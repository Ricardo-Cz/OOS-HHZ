/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.ImagePrediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.Prediction;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
import org.json.simple.JSONArray;
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
    private static final float PASS_THROUGH_PROBABILITY = (float) 70.00;

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

    public static Map<String, List<byte[]>> getSubBytesPriceTagsFromImage(Map<String, ImagePrediction> mapPrediction) {

        byte[] byteArray = null;
        Map<String, List<byte[]>> mapWithPriceTags = new HashMap<>();
        for (String path : mapPrediction.keySet()) {
            //Get picture
            BufferedImage bimg = null;
            try {
                bimg = ImageIO.read(new File(path));
            } catch (IOException ex) {
                Logger.getLogger(FileHelperClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            int width = bimg.getWidth(); //width of the whole picture in px
            int height = bimg.getHeight();  //height of the whole picture in px
            System.out.println(width);
            List<byte[]> byteArrayList = new ArrayList<>();
            for (Prediction prediction : mapPrediction.get(path).predictions()) {
                //prepare for OCR
//                double distance_left = 0.01;
//                double distance_top = 0.01;
//                double distance_width = 0.03;
//                double distance_height = 0.03;
//                int priceBox_left_abs = 0, priceBox_top_abs = 0, priceBox_width_abs = 0, priceBox_height_abs = 0;
//                double priceBox_left_rel = 0, priceBox_top_rel = 0;
                if (prediction.probability() * 100.0f >= PASS_THROUGH_PROBABILITY) {

                    //prepare for OCR
                    System.out.println("Bild zuschneiden:");
                    double priceBox_left_rel = prediction.boundingBox().left() - 0.06;
                    if (priceBox_left_rel < 0) {
                        priceBox_left_rel = 0;
                    }
                    System.out.println(priceBox_left_rel);

                    double priceBox_top_rel = prediction.boundingBox().top() - 0.04;
                    if (priceBox_top_rel < 0) {
                        priceBox_top_rel = 0;
                    }
                    System.out.println(priceBox_top_rel);
                    double priceBox_width_rel = prediction.boundingBox().width() + 0.08;
                    if ((priceBox_left_rel + priceBox_width_rel) > 1) {
                        priceBox_width_rel = 1 - priceBox_left_rel - 0.001;
                    }
                    System.out.println(priceBox_width_rel);
                    double priceBox_height_rel = prediction.boundingBox().height() + 0.10;
                    if ((priceBox_top_rel + priceBox_height_rel) > 1) {
                        priceBox_height_rel = 1 - priceBox_top_rel - 0.001;
                    }
                    System.out.println(priceBox_height_rel);

                    int priceBox_left_abs = (int) (width * priceBox_left_rel);
                    int priceBox_top_abs = (int) (height * priceBox_top_rel);
                    int priceBox_width_abs = (int) (priceBox_width_rel * width);
                    int priceBox_height_abs = (int) (priceBox_height_rel * height);

//                    do {
//                        if ((priceBox_left_abs + priceBox_width_abs) > width) {
//                            distance_width -= 0.005;
//                        }
//                        if ((priceBox_top_abs + priceBox_height_abs > height)) {
//                            distance_height -= 0.005;
//                        }
//                        do {
//                            priceBox_left_rel = prediction.boundingBox().left() - distance_left;
//                            if (priceBox_left_rel < 0) {
//                                distance_left -= 0.002;
//                            }
//                        } while (priceBox_left_rel < 0);
//                        do {
//                            priceBox_top_rel = prediction.boundingBox().top() - distance_top;
//                            if (priceBox_top_rel < 0) {
//                                distance_top -= 0.002;
//                            }
//                        } while (priceBox_top_rel < 0);
//
//                        double priceBox_width_rel = prediction.boundingBox().width() + distance_width;
//                        double priceBox_height_rel = prediction.boundingBox().height() + distance_height;
//
//                        priceBox_left_abs = (int) (width * priceBox_left_rel);
//                        priceBox_top_abs = (int) (height * priceBox_top_rel);
//                        priceBox_width_abs = (int) (priceBox_width_rel * width);
//                        priceBox_height_abs = (int) (priceBox_height_rel * height);
//                    } while ((priceBox_left_abs + priceBox_width_abs) > width || (priceBox_top_abs + priceBox_height_abs > height));
                    try {

                        BufferedImage newImg = bimg.getSubimage(priceBox_left_abs, priceBox_top_abs, priceBox_width_abs, priceBox_height_abs);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(newImg, "jpg", baos);
                        baos.flush();
                        byteArray = baos.toByteArray();
                        byteArrayList.add(byteArray);

                        //OutputStream outputStream = new FileOutputStream("C:/OOS_KL/test.jpg");
                        File fittedImage = new File("src/main/resources/ocr/" + Home.gl_shelf_id + "_" + Home.gl_row_id + "_" + Home.gl_place_id);
                        if(!fittedImage.exists()){
                            fittedImage.mkdirs();
                        }
                        //fittedImage.createNewFile(); // if file already exists will do nothing 
                        //FileOutputStream oFile = new FileOutputStream(fittedImage, false);
                        OutputStream outputStream = new FileOutputStream("src/main/resources/ocr/" + Home.gl_shelf_id + "_" + Home.gl_row_id + "_" + Home.gl_place_id +"/1.jpg");

                        baos.writeTo(outputStream);

                        baos.close();

                    } catch (IOException ex) {
                        Logger.getLogger(FileHelperClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (!byteArrayList.isEmpty()) {
                mapWithPriceTags.put(path, byteArrayList);
            }
        }
        return mapWithPriceTags;
    }

    private static byte[] getSubBytesFromImage(File file) {
        byte[] byteArray = null;
        try {
            BufferedImage img = ImageIO.read(file);
            BufferedImage newImg = img.getSubimage(0, SUB_IMAGE_Y_VALUE, img.getWidth(), img.getHeight() - SUB_IMAGE_Y_VALUE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImg, "jpg", baos);
            baos.flush();
            byteArray = baos.toByteArray();
            baos.close();

        } catch (IOException ex) {
            Logger.getLogger(FileHelperClass.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class
            );
            return attr.creationTime().toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

    public static String
            getFileCreationTime(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class
            );
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

    public static String[] getKey() {
        String[] keys = {"dummy", "dummy"};
        try {
            FileReader fr = new FileReader("C:/OOS_KL/PriceTagRecognitionAPI.txt");
            BufferedReader br = new BufferedReader(fr);
            keys[0] = br.readLine();
            keys[1] = br.readLine();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }

    public List<String> ChangeFileExtensionToDotJson(List<String> filePaths) {
        List<String> newFilePathList = new ArrayList();
        for (String str : filePaths) {
            if (str.lastIndexOf(".") != -1 && str.lastIndexOf(".") != 0) {
                str = str.substring(0, str.lastIndexOf(".")) + ".json";
                newFilePathList.add(str);
            }
        }
        return newFilePathList;
    }

    String setPostfixToPathName(String filePath) {
        if (filePath.lastIndexOf(".") != -1 && filePath.lastIndexOf(".") != 0) {
            String extension = filePath.substring(filePath.lastIndexOf("."), filePath.length());
            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_" + extension;
        }
        return filePath;
    }

    void WriteJsonToFile(JSONArray json, String filePath) {

        if (filePath.lastIndexOf(".") != -1 && filePath.lastIndexOf(".") != 0) {
            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".json";

            try {
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                System.out.println("Json erfolgreich im Verzeichnis: " + filePath + " abgespeichert.");

            } catch (IOException ex) {
                Logger.getLogger(FileHelperClass.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
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
                Logger.getLogger(FileHelperClass.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static JSONArray ReadJsonFromFile(String filePath) {
        JSONParser parser = new JSONParser();
        // JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            Object obj = parser.parse(new FileReader(filePath));
            jsonArray = (JSONArray) obj;
            // jsonObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    static JSONObject ReadJsonObjectFromFile(String filePath) {
        JSONParser parser = new JSONParser();
        // JSONObject jsonObject = null;
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject)obj;
            // jsonObject = (JSONObject) obj;
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

    void WriteJsonToFile(org.json.JSONArray json, String filePath) {
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
}
