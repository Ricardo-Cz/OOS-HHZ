/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.ocr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Valerij
 */
public class DrawBoundingBox extends JFrame {

    private static final double scaleFactor = 0.2;
    Polygon P;

    //Constructor
    DrawBoundingBox(String imagePath) {
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
                //Dimension screensize =Toolkit.getDefaultToolkit().getScreenSize();
                Image image = img.getScaledInstance((int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(image);
                JLabel jlabel = new JLabel(imageIcon) {
                    public void paint(Graphics g) {
                        super.paint(g);
                        //g.setColor(Color.red);
                        //g.drawRect(50, 50, 100, 100);
                        //g.drawString("1)", 55, 55);
                        int[] y = {(int) (853 *scaleFactor),
                            (int)(834*scaleFactor),
                            (int)(980*scaleFactor),
                            (int)(999*scaleFactor)};
                        int[] x ={
                            (int)(672*scaleFactor),
                            (int)(1543*scaleFactor),
                            (int)(1546*scaleFactor),
                            (int)(675*scaleFactor)};
                        g.setColor(Color.red);
                        g.drawPolygon(x , y, x.length);
                    }
                };
                
                frame.getContentPane().add(jlabel, BorderLayout.CENTER);
                JTextArea textArea = new JTextArea(1, 30);
                JScrollPane jsp = new JScrollPane(textArea);
                textArea.setText("Folgender Text wurde erkannt: "
                        + "\n A text area is a \"plain\" text component, "
                        + "\n which means that although it can display text "
                        + "\n in any font, all of the text is in the same font.");
                textArea.setFont(new Font("Serif", Font.CENTER_BASELINE, 12));
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setBackground(Color.LIGHT_GRAY);
                //textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                textArea.setMargin(new Insets(10, 10, 10, 10));
                textArea.setEditable(false);
                frame.getContentPane().add(textArea, BorderLayout.EAST);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }

    public void DrawBoundingBoxOnImage(String imagePath) {

    }
}
