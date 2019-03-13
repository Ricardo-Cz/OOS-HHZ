/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Classifier;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Domain;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.DomainType;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.ImageFileCreateBatch;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.ImageFileCreateEntry;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Iteration;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Project;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Region;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.TrainingApi;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.Trainings;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.CustomVisionTrainingManager;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.ImagePrediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.Prediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.PredictionEndpoint;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.CustomVisionPredictionManager;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Valerij
 */
public class ProductRecognitionAPI {

    public static void startAnalyse(String StartTime, String directoryPath) {
        String[] keys = FileHelperClass.getKey();
        String trainingApiKey = keys[0];
        String predictionApiKey = keys[1];

        TrainingApi trainClient = CustomVisionTrainingManager.authenticate(trainingApiKey);
        PredictionEndpoint predictClient = CustomVisionPredictionManager.authenticate(predictionApiKey);
        Map<String, ImagePrediction> imagePrediction = TestImage(trainClient, predictClient, directoryPath);
    }

    public static Map<String, ImagePrediction> TestImage(TrainingApi trainClient, PredictionEndpoint predictor, String directoryPath) {
        Map<String, ImagePrediction> imgPrediction = new HashMap<>();
        JSONArray jArray = new JSONArray();
        org.json.JSONObject json = null;
        String jsonInString = "";
        try {
            Map<String, byte[]> imageDictionary = FileHelperClass.getImages(directoryPath);
            for (String imagePathKey : imageDictionary.keySet()) {
                try {
                    Trainings trainer = trainClient.trainings();
                    Project project = trainer.getProject(UUID.fromString("34403d0c-7022-4be0-bd90-8116c410b190"));

                    // predict
                    ImagePrediction results = predictor.predictions().predictImage()
                            .withProjectId(project.id())
                            .withImageData(imageDictionary.get(imagePathKey))
                            .execute();
                    if (results != null) {
                        jsonInString = new Gson().toJson(results);
                        imgPrediction.put(imagePathKey, results);
                        // json = new org.json.JSONObject(results);
                        //jArray.put(json);
                    }

                    /*    for (Prediction prediction : results.predictions()) {
                        System.out.println(String.format("\t%s: %.2f%% at: %.2f, %.2f, %.2f, %.2f",
                                prediction.tagName(),
                                prediction.probability() * 100.0f,
                                prediction.boundingBox().left(),
                                prediction.boundingBox().top(),
                                prediction.boundingBox().width(),
                                prediction.boundingBox().height()
                        ));
                    }*/
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                // System.out.println(json.toString(2));
                FileHelperClass fh = new FileHelperClass();
                fh.WriteJsonToFile(jsonInString, fh.setPostfixToPathName(imagePathKey));
            }

        } catch (IOException ex) {
            Logger.getLogger(ProductRecognitionAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imgPrediction;
    }
}
