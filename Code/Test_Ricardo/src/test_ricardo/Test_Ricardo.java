package test_ricardo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ricardo
 */
/*
 * Main
 */
public class Test_Ricardo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Get picture
        BufferedImage bimg = ImageIO.read(new File("./src/resources/test_image.JPG"));
        int width          = bimg.getWidth(); //width of the whole picture in px
        int height         = bimg.getHeight();  //height of the whole picture in px
        System.out.println(width);
       
        //prepare for OCR
        double priceBox_left_rel = 0.11 - 0.01; //prediction.boundingBox().left() - 0.01;
        double priceBox_top_rel = 0.79 - 0.01; //prediction.boundingBox().top() - 0.01;
        double priceBox_width_rel = 0.18 + 0.03; //prediction.boundingBox().width() + 0.03;
        double priceBox_height_rel = 0.15 + 0.03; //prediction.boundingBox().height() + 0.03;
        
        int priceBox_left_abs = (int)(width*priceBox_left_rel);
        int priceBox_top_abs = (int)(height*priceBox_top_rel);
        int priceBox_width_abs = (int) (priceBox_width_rel*width);
        int priceBox_height_abs =(int) (priceBox_height_rel*height);
        
//        byte[] byteArray = null;
        try {
            
            BufferedImage newImg = bimg.getSubimage(priceBox_left_abs, priceBox_top_abs, priceBox_width_abs, priceBox_height_abs);
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImg, "jpg", new File("./src/resources/neu3.jpg"));
//            ImageIO.write(newImg, "jpg", baos );
//            baos.flush();
//            byteArray = baos.toByteArray();
//            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(Test_Ricardo.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(byteArray);
   } 
    
    

    
}
