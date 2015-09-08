/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degrees.to.cage;

import degrees.to.cage.Tree.Node;
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
    static Tree<String> linkTree;
    
    public static void main(String[] args) throws IOException {
        DegreesToCage deg = new DegreesToCage("https://en.wikipedia.org/wiki/Nicolas_Cage", "German American");
        try {
            Node<String> found = deg.driver();
            System.out.println(found.data);
        } catch (SocketTimeoutException ex) {
            Logger.getLogger(DegreesToCage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
