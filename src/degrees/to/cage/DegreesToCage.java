/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degreestocage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
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
    HashMap<String, String> upcoming;
    HashMap<String, String> visited; 
    String searchTerm;
    boolean found;
    Node<String> foundNode = null;
    Document page;
    Elements linklvl;
    Deque<Node<String>> stack;
    List<Deque<Node<String>>> nextList;
    List<Deque<Node<String>>> tempList;
    
    public void driver() throws IOException{	
        nextList = tempList;
        while (!nextList.isEmpty()){
            stack = nextList.remove(0);
            while(stack.peek() != null){
                Node<String> thisElement = stack.pop();               
                page = getPage(thisElement.data);
                getLinks(page, thisElement);               
            }          
        }
        
        driver();
    }
    
    public DegreesToCage(String URL, String search) throws IOException{
        scrubber = new Cleaner(Whitelist.basicWithImages());
        searchTerm = search;
        stack = new ArrayDeque<>();
        nextList = new ArrayList<>();
        tempList = new ArrayList<>();
        visited = new HashMap<>();
        Document page = getPage(URL);
        Node<String> rootNode = new Node<>();
        rootNode.data = URL;
        getLinks(page, rootNode);
        
       
    }
    
    public Boolean checkHeading(Node<String> node) throws IOException{

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
    
    public Document getPage(String url) throws IOException{        
        Document page = Jsoup.connect(url).timeout(10*1000).get();
        page.setBaseUri(url);
        visited.put(url, "");
        return page;                            
    }
    
    public void getLinks(Document page, Node<String> parentNode){
        tempList.clear();
        Deque<Node<String>> tempStack = new ArrayDeque<>();       
        Elements content = page.select("[id=mw-content-text]");
        Elements links = content.select("a[href^=/wiki]:not(a[href^=/wiki/File])"
                + ":not(a[href^=/wiki/Wikipedia]):not(a[href^=/wiki/Help]):not(a[href^=/wiki/Talk])"
                + ":not(a[href^=/wiki/Portal]):not(a[href^=/wiki/Special:]):not(a[href^=/wiki/Template:])");
        for (Element child : links) { //convert these elements to Node<String> to save memory       
            //System.out.println(child.toString());
            if (visited.containsKey(child.attr("abs:href"))){
                    links = links.not("a[href^=" + child.attr("abs:href") + "]");
                    continue;
            }
            Node<String> childLink = new Node<>();           
            childLink.parent = parentNode;
            childLink.data = child.attr("abs:href");
            parentNode.addChild(childLink);
            tempStack.add(childLink);
            if(childLink.data.equals("https://en.wikipedia.org/wiki/Sandpaper")){               
                foundNode = childLink;
                StringBuilder sb = new StringBuilder();
                sb.append(childLink.data);
                Node<String> temp = childLink.parent;
                while(temp.parent != null){
                    sb.append(" ");
                    sb.append(temp.data);
                    temp = temp.parent;
                }
                System.out.println(sb.toString());
                System.exit(0);
            }
        }
        tempList.add(tempStack);
        links.clear();
    }
    
    public static String getHeading(Document page){
        Element content = page.select("[id=firstHeading]").first();
        return content.text();
    }
    
    
}
