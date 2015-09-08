/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degrees.to.cage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 *
 * @author Joe
 */
public class DegreesToCage {

    /**
     * @param args the command line arguments
     */
    Cleaner scrubber;
    static Tree<String> linkTree;
    
    public static void main(String[] args) {
        DegreesToCage deg = new DegreesToCage();
        try {
            linkTree = new Tree("https://en.wikipedia.org/wiki/Madonna_in_the_Church");
            deg.driver("https://en.wikipedia.org/wiki/Madonna_in_the_Church", deg, "Nicholas Cage");
            if(true){
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(DegreesToCage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void driver(String url, DegreesToCage deg, String searchTerm) throws IOException{
        Document page = deg.getPage("https://en.wikipedia.org/wiki/Madonna_in_the_Church");   
        String heading = deg.getHeading(page);
        if(heading.equals(searchTerm)){
            return;
        }  
        Elements links = deg.getLinks(page);
        for(Element link : links){
            String urlToVisit = link.text();
        }
        
        
    }
    
    public DegreesToCage(){
        scrubber = new Cleaner(Whitelist.basicWithImages());       
    }
    
    public Document getPage(String url) throws IOException{        
        Document page = Jsoup.connect(url).get();
        page.setBaseUri(url);
        return page;                            
    }
    
    public Elements getLinks(Document page){
        Elements content = page.select("[id=mw-content-text]");
        Elements links = content.select("a");    
        return links;
    }
    
    public String getHeading(Document page){
        Element content = page.select("[id=firstHeading]").first();
        return content.text();
    }
}
