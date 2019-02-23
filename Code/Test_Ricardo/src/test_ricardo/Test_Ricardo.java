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
        BufferedImage bimg = ImageIO.read(new File("C:\\Users\\Ricardo\\OneDrive\\Netbeans\\Code\\CustomVision\\src\\main\\resources\\ObjectTest\\test_image.JPG"));
        int width          = bimg.getWidth(); //width of the whole picture in px
        int height         = bimg.getHeight();  //height of the whole picture in px
        System.out.println(width);
       
        
        double box_left = 0.11 - 0.01; //prediction.boundingBox().left() - 0.01;
        double box_top = 0.79 - 0.01; //prediction.boundingBox().top() - 0.01;
        double box_width = 0.18 + 0.03; //prediction.boundingBox().width() + 0.03;
        double box_height = 0.15 + 0.03; //prediction.boundingBox().height() + 0.03;
  
//        byte[] byteArray = null;
        try {
            
            BufferedImage newImg = bimg.getSubimage((int) (width*box_left), (int) (height*box_top),(int) (box_width*width), (int) (box_height*height));
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImg, "jpg", new File("/neu3.jpg"));
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
