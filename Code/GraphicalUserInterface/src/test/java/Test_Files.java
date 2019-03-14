
import hhz.graphicaluserinterface.Home;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
public class Test_Files {
    ImageIcon imageIcon;
    public static void main(String[] args) {
        Test_Files t = new Test_Files();
        t.test();
    }
    
    public void test(){
        imageIcon = new javax.swing.ImageIcon(getClass().getResource("/images/gallery.png"));
    }
    
}
