package com.microsoft.azure.cognitiveservices.vision.customvision.samples;
//
//allala
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteStreams;

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

public class Test {
    public static void main(String[] args){
        final String trainingApiKey = "bd4397c8393b4dd994628fb2b3facb12";
        final String predictionApiKey = "68ecc463ba0c4ccfb9772717cdd1ec38";

        TrainingApi trainClient = CustomVisionTrainingManager.authenticate(trainingApiKey);
        PredictionEndpoint predictClient = CustomVisionPredictionManager.authenticate(predictionApiKey);
        TestImage(trainClient, predictClient);

    }
    
    public static void TestImage (TrainingApi trainClient, PredictionEndpoint predictor){
        try{
            byte[] testImage = GetImage("/ObjectTest", "test_image.jpg");
            Trainings trainer = trainClient.trainings();
            Project project = trainer.getProject(UUID.fromString("1766da8f-9833-41e1-9893-427a7cc52a04"));


                // predict
                ImagePrediction results = predictor.predictions().predictImage()
                    .withProjectId(project.id())
                    .withImageData(testImage)
                    .execute();

                for (Prediction prediction: results.predictions())
                {
                    System.out.println(String.format("\t%s: %.2f%% at: %.2f, %.2f, %.2f, %.2f",
                        prediction.tagName(),
                        prediction.probability() * 100.0f,
                        prediction.boundingBox().left(),
                        prediction.boundingBox().top(),
                        prediction.boundingBox().width(),
                        prediction.boundingBox().height()
                    ));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static byte[] GetImage(String folder, String fileName)
    {
        try {
            return ByteStreams.toByteArray(CustomVisionSamples.class.getResourceAsStream(folder + "/" + fileName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

