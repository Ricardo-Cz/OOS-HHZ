/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Valerij
 */
public class OcrApi {

    private static final String directoryPath = "C:\\Users\\Valerij\\Desktop\\Projekt 2\\OCR\\cam1";
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************
    // Replace <Subscription Key> with your valid subscription key.
    private static String subscriptionKey = "dummy";

    // You must use the same Azure region in your REST API method as you used to
    // get your subscription keys. For example, if you got your subscription keys
    // from the West US region, replace "westcentralus" in the URL
    // below with "westus".
    //
    // Free trial subscription keys are generated in the "westus" region.
    // If you use a free trial subscription key, you shouldn't need to change
    // this region.
    private static final String uriBase
            = "https://westeurope.api.cognitive.microsoft.com/vision/v2.0/recognizeText";

    public static void startOcrAnalyse(Map<String, List<byte[]>> mapWithPriceTags) {
        subscriptionKey = getKey();
        CloseableHttpClient httpTextClient = HttpClientBuilder.create().build();
        CloseableHttpClient httpResultClient = HttpClientBuilder.create().build();;

        for (String imagePathKey : mapWithPriceTags.keySet()) {
            
            JSONArray jArray = new JSONArray();
            for (byte[] priceTag : mapWithPriceTags.get(imagePathKey)) {
                try {
                    // This operation requires two REST API calls. One to submit the image
                    // for processing, the other to retrieve the text found in the image.

                    URIBuilder builder = new URIBuilder(uriBase);

                    // Request parameter.
                    builder.setParameter("mode", "Printed");

                    // Prepare the URI for the REST API method.
                    URI uri = builder.build();
                    HttpPost request = new HttpPost(uri);

                    // Request headers.
                    request.setHeader("Content-Type", "application/octet-stream");
                    request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                    request.setEntity(new ByteArrayEntity(priceTag));

                    // Request body.
                    //  StringEntity requestEntity =
                    //        new StringEntity("{\"url\":\"" + imageToAnalyze + "\"}");
                    // request.setEntity(requestEntity);
                    // Two REST API methods are required to extract handwritten text.
                    // One method to submit the image for processing, the other method
                    // to retrieve the text found in the image.
                    // Call the first REST API method to detect the text.
                    HttpResponse response = httpTextClient.execute(request);

                    // Check for success.
                    if (response.getStatusLine().getStatusCode() != 202) {
                        // Format and display the JSON error message.
                        HttpEntity entity = response.getEntity();
                        String jsonString = EntityUtils.toString(entity);
                        return;
                    }

                    // Store the URI of the second REST API method.
                    // This URI is where you can get the results of the first REST API method.
                    String operationLocation = null;

                    // The 'Operation-Location' response header value contains the URI for
                    // the second REST API method.
                    Header[] responseHeaders = response.getAllHeaders();
                    for (Header header : responseHeaders) {
                        if (header.getName().equals("Operation-Location")) {
                            operationLocation = header.getValue();
                            break;
                        }
                    }

                    if (operationLocation == null) {
                        System.out.println("\nError retrieving Operation-Location.\nExiting.");
                        System.exit(1);
                    }

                    // If the first REST API method completes successfully, the second
                    // REST API method retrieves the text written in the image.
                    //
                    // Note: The response may not be immediately available. Handwriting
                    // recognition is an asynchronous operation that can take a variable
                    // amount of time depending on the length of the handwritten text.
                    // You may need to wait or retry this operation.
                    System.out.println("\n Printed text submitted.\n"
                            + "Waiting 10 seconds to retrieve the recognized text.\n");
                    Thread.sleep(10000);

                    // Call the second REST API method and get the response.
                    HttpGet resultRequest = new HttpGet(operationLocation);
                    resultRequest.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                    HttpResponse resultResponse = httpResultClient.execute(resultRequest);
                    HttpEntity responseEntity = resultResponse.getEntity();

                    if (responseEntity != null) {
                        String jsonString = EntityUtils.toString(responseEntity);
                        JSONObject json = new JSONObject(jsonString);

                        jArray.put(json);
                        //System.out.println("Text recognition result response: \n");
                        //System.out.println(json.toString(2));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            //System.out.println("Response combined: \n");
            //System.out.println(jArray.toString(2));
            FileHelperClass fh = new FileHelperClass();
            fh.WriteJsonToFile(jArray, imagePathKey);

        }
    }

    public static String getKey() {
        String key = "dummy";
        try {
            FileReader fr = new FileReader("C:/OOS_KL/OCRAPI.txt");
            BufferedReader br = new BufferedReader(fr);
            key = br.readLine();
            br.close();
            System.out.println(key);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return key;
    }
}
