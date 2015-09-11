/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degreestocage;

import java.io.IOException;
import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
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
    HashMap<String, String> visited; 
    boolean found;
    Node<String> foundNode = null;
    Document page;
    AbstractQueue<Node<String>> queue;
    int count = 0;
    int linksChecked = 0;
     
    
    public DegreesToCage() {
        scrubber = new Cleaner(Whitelist.basicWithImages());
        queue = new ArrayBlockingQueue<>(200000);
        visited = new HashMap<>();       
    }
      
    public Document GetPage(String url) throws IOException, InterruptedException{ 
        Thread.sleep(1000); 
        Document thisPage = Jsoup.connect(url).timeout(10*1000).get();
        thisPage.setBaseUri(url);
        visited.put(url, "");
        count++;
        return thisPage;                            
    }
    
    public Node<String> Search(Node<String> rootNode) throws IOException, InterruptedException{                
        queue.add((rootNode));
        while(queue.peek() != null){
            Node<String> thisNode = queue.poll();
            while (visited.containsKey(thisNode.data)){
                thisNode = queue.poll();
            }
            page = GetPage(thisNode.data);
            Elements content = page.select("[id=mw-content-text]");
            Elements links = content.select("a[href^=/wiki]:not(a[href^=/wiki/File])"
                    + ":not(a[href^=/wiki/Wikipedia]):not(a[href^=/wiki/Help]):not(a[href^=/wiki/Talk])"
                    + ":not(a[href^=/wiki/Portal]):not(a[href^=/wiki/Special:]):not(a[href^=/wiki/Template:])");       
            for(Element link : links){           
                Node<String> childLink = new Node<>();
                childLink.parent = thisNode;
                childLink.data = link.attr("abs:href");
                queue.add(childLink);
                linksChecked++;
                System.out.println(childLink.data);
                if(childLink.data.equalsIgnoreCase("https://en.wikipedia.org/wiki/Alex_Proyas")){
                    foundNode = childLink;
                }                                    
            }
            System.out.println(thisNode.data);
            System.out.println(linksChecked);
            System.out.println(count);          
            }
        return foundNode;
              
    }    
}
