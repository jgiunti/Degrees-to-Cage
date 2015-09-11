/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degreestocage;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.safety.Cleaner;

/**
 *
 * @author Joe
 */
public class Driver {
    
    Cleaner scrubber;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        DegreesToCage deg = new DegreesToCage();
        try {
            Node<String> node = new Node<String>();
            node.data = "https://en.wikipedia.org/wiki/Nicolas_Cage";
            Node<String> found = deg.getLinks(node);
            StringBuilder sb = new StringBuilder();
            while(found.parent != null){
                sb.append((found.data));
                found = found.parent;
            }
            System.out.println(sb.toString());
        } catch (SocketTimeoutException ex) {
            Logger.getLogger(DegreesToCage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
