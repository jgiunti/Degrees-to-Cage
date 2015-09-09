/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degrees.to.cage;

import degrees.to.cage.Tree.Node;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
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
    HashMap<String, String> upcoming = new HashMap<>();
    HashMap<String, String> visited = new HashMap<>();
    String searchTerm;
    boolean found;
    Node<String> foundNode = null;
    Document page;
    Elements linklvl;
    Deque<Elements> stack;
    Deque<Elements> nextStack;
    
    public void driver() throws IOException{
    	while(stack.peek() != null){
            linklvl = stack.pop();
            for (Element thisElement : linklvl) {
                page = getPage(thisElement.attr("abs:href"));
                Elements links = getLinks(page, thisElement);
                nextStack.push(links);
            }
        }
        stack = nextStack;
        driver();
    }
    
    public DegreesToCage(String URL, String search) throws IOException{
        scrubber = new Cleaner(Whitelist.basicWithImages());  
        Document page = getPage(URL);
        Element starter = new Element(Tag.valueOf("a"), URL);
        linklvl = getLinks(page, starter);
        searchTerm = search;
        stack = new ArrayDeque<>();
        nextStack = new ArrayDeque<>();
        stack.push(linklvl);
    }
    
    public Boolean checkHeading(Node<String> node) throws IOException{
        page = getPage(node.getData());
        String heading = getHeading(page);
        System.out.println(heading);
        if(heading.equals(searchTerm)){           
            foundNode = node;
            found = true;
            return true;
        }
        else{
            found = false;
            return false;
        }
    }
    
    public void addChildren(Node<String> node) throws IOException{
     
        /*links.stream().map((link) -> {
            Node<String> newNode = new Node();
            newNode.data = link.attr("abs:href");
            return newNode;
        }).forEach((newNode) -> {
            if(!upcoming.containsKey(newNode.data)){
                if(newNode.data.equalsIgnoreCase("https://en.wikipedia.org/wiki/German_American")){
                    found = true;
                    newNode.parent = node;
                    foundNode = newNode;
                }
                newNode.parent = node;
                newNode.children = new ArrayList<>();
                node.children.add(newNode);
                upcoming.put(newNode.getData(), "");
            }           
        });*/
        visited.put(node.data, "");
    }
    
    public static Document getPage(String url) throws IOException{        
        Document page = Jsoup.connect(url).timeout(10*1000).get();
        page.setBaseUri(url);
        return page;                            
    }
    
    public static Elements getLinks(Document page, Element parentElement){
        Elements content = page.select("[id=mw-content-text]");
        Elements links = content.select("a[href^=/wiki]:not(a[href^=/wiki/File])"
                + ":not(a[href^=/wiki/Wikipedia]):not(a[href^=/wiki/Help]):not(a[href^=/wiki/Talk])"
                + ":not(a[href^=/wiki/Portal]):not(a[href^=/wiki/Special:]");
        for (Element child : links) {
            parentElement.appendChild(child);
            if(child.attr("abs:href").equals("https://en.wikipedia.org/wiki/Casino")){
                StringBuilder sb = new StringBuilder();
                sb.append(child.baseUri());
                Element temp = child.parent();
                while(temp.parent() != null){
                    sb.append(" ");
                    sb.append(temp.baseUri());
                    temp = temp.parent();
                }
                System.out.println(sb.toString());
            }
        }
        return links;
    }
    
    public static String getHeading(Document page){
        Element content = page.select("[id=firstHeading]").first();
        return content.text();
    }
    
    
}