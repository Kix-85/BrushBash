/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package brushbash;


import com.sun.tools.javac.Main;

import javax.swing.JFrame;
import java.awt.*;
import java.net.URL;

public class BrushBash {
    public static void main(String[] args) {
        // Create the JFrame
        JFrame frame = new JFrame("BrushBash");
        frame.setSize(500, 500);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("src/brushbash/BrushBash.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel p = new Panel();
        frame.setContentPane(p);
        frame.setVisible(true);
    }
    
}
