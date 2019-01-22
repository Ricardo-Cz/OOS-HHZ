/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.ocr;

import com.google.gson.Gson;
import hhz.ocr.json.BoundingBoxObject;
import hhz.ocr.json.Lines;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.JSONObject;

/**
 *
 * @author Valerij
 */
public class DrawBoundingBox extends JFrame {

    private static final double scaleFactor = 0.2;
    Polygon P;

    //Constructor
    public DrawBoundingBox(String imagePath) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Bounding Box from: " + imagePath);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                BufferedImage img = null;

                try {
                    img = ImageIO.read(new File(imagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                Image image = img.getScaledInstance((int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(image);
                // Extrakt data
                JSONObject json = GraphicHelper.ReadJsonFromFile("C:\\Users\\Valerij\\Desktop\\Projekt 2\\OCR\\img_20150328_131815.json");
                Gson gson = new Gson();
                BoundingBoxObject bbo = new BoundingBoxObject();
                bbo = gson.fromJson(json.toString(), BoundingBoxObject.class);
                List<Integer> lineBoundingBox = ExtractLineBoundingBox(bbo);
                int[] allXCoordinatesOfBoundingBox = ListToIntArray(ExtractTheXCoordinates(lineBoundingBox));
                int[] allYCoordinatesOfBoundingBox = ListToIntArray(ExtractTheYCoordinates(lineBoundingBox));
                String recognizedText = ExtractLineText(bbo);

                JLabel jlabel = new JLabel(imageIcon) {
                    public void paint(Graphics g) {
                        super.paint(g);
                        int[] tempX = new int[4];
                        int[] tempY = new int[4];
                        int count = 0;
                        int number = 1;
                        for (int i = 0; i < allXCoordinatesOfBoundingBox.length; i++) {

                            for (int j = 0; j < 4; j++) {
                                tempX[j] = allXCoordinatesOfBoundingBox[count + j];
                                tempY[j] = allYCoordinatesOfBoundingBox[count + j];
                            }
                            g.setColor(Color.red);
                            g.drawPolygon(tempX, tempY, 4);
                            g.setColor(Color.GREEN);
                            g.setFont(new Font("Normal", Font.BOLD,14));
                            g.drawString(number + ") " , (tempX[1]+5), (tempY[0]+tempY[2])/2);
                            System.out.println("X: " + tempX[0] + "," + tempX[1] + "," + tempX[2] + "," + tempX[3] + " Y: " + tempY[0] + "," + tempY[1] + "," + tempY[2] + "," + tempY[3]);
                            count += 4;
                            number++;
                            if (count == allXCoordinatesOfBoundingBox.length) {
                                break;
                            }
                        }
                    }
                };

                frame.getContentPane().add(jlabel, BorderLayout.CENTER);
                JTextArea textArea = new JTextArea(1, 30);
                JScrollPane jsp = new JScrollPane(textArea);
                textArea.setText("Folgender Text wurde erkannt: " + "\n\n" + recognizedText);
                textArea.setFont(new Font("Serif", Font.CENTER_BASELINE, 14));
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setBackground(Color.LIGHT_GRAY);
                textArea.setMargin(new Insets(10, 10, 10, 10));
                textArea.setEditable(false);
                frame.getContentPane().add(textArea, BorderLayout.EAST);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }

    public int[] ListToIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        Iterator<Integer> iter = list.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            ret[i] = iter.next();
        }
        return ret;
    }

    public static List<Integer> ExtractLineBoundingBox(BoundingBoxObject bbo) {

        List<Integer> coordinats = new ArrayList();
        for (Lines line : bbo.getRecognitionResult().getLines()) {
            for (Integer boundingBox : line.getBoundingBox()) {
                coordinats.add((int) (boundingBox * scaleFactor));
            }
        }
        System.out.println(coordinats);
        return coordinats;
    }

    public List<Integer> ExtractTheXCoordinates(List<Integer> wholeList) {
        List<Integer> xCoordinates = new ArrayList<Integer>();
        for (int i = 0; i < wholeList.size(); i++) {
            if ((i % 2) == 0 || i == 0) {
                xCoordinates.add(wholeList.get(i));
            }
        }
        System.out.println(xCoordinates);
        return xCoordinates;
    }

    public List<Integer> ExtractTheYCoordinates(List<Integer> wholeList) {
        List<Integer> yCoordinates = new ArrayList<Integer>();
        for (int i = 0; i < wholeList.size(); i++) {
            if ((i % 2) == 1) {
                yCoordinates.add(wholeList.get(i));
            }
        }
        System.out.println(yCoordinates);
        return yCoordinates;
    }

    public String ExtractLineText(BoundingBoxObject bbo) {
        String text = "";
        int counter = 1;
        for (Lines line : bbo.getRecognitionResult().getLines()) {
            text += counter + ") " + line.getText() + "\n";
            counter++;
        }
        System.out.println(text);
        return text;
    }
}
