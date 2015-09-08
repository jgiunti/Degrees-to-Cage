/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degrees.to.cage;

import degrees.to.cage.Tree.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    static Node<String> root;
    HashMap<String, String> visited = new HashMap<>();
    boolean found = false;
    String searchTerm;
    
    public void driver() throws IOException{
        while(!found){
            Node<String> baseNode = root;
            while(baseNode.hasChildren()){
                baseNode = baseNode.getChild(0);
            }         
            //int lvl1 = root.parent.parent.children.size();
            if(baseNode == root){
                addChildren(root);
                driver();
            }
            if(baseNode.parent == root){
                Queue<Node<String>> lvl1Q = new LinkedList<>(root.children);
                while(!lvl1Q.isEmpty()){
                    Node<String> bottomNode = lvl1Q.poll();
                    addChildren(bottomNode);                      
                }
                driver();
            }
            else{
                Queue<Node<String>> lvl1Q = new LinkedList<>(root.parent.parent.children);
                while(!lvl1Q.isEmpty()){
                    Node<String> thisNode = lvl1Q.poll();
                    Queue<Node<String>> lvl2Q = new LinkedList<>(thisNode.children);
                    while(!lvl2Q.isEmpty()){
                        Node<String> bottomNode = lvl2Q.poll();
                        addChildren(bottomNode);                
                    }
                }
                driver();          
            }                               
        }
        
        
        
    }
    
    public DegreesToCage(String URL, String search){
        scrubber = new Cleaner(Whitelist.basicWithImages());  
        Tree<String> linkTree = new Tree(URL);
        root = linkTree.getRoot();
        searchTerm = search;
    }
    
    public void addChildren(Node<String> node) throws IOException{
        Document page = getPage(node.getData());
        String heading = getHeading(page);
        System.out.println(heading);
        if(heading.equals(searchTerm)){
            found = true;
        }
        Elements links = getLinks(page);
        links.stream().map((link) -> {
            Node<String> newNode = new Node();
            newNode.data = link.attr("abs:href");
            return newNode;
        }).forEach((newNode) -> {
            newNode.parent = node;
            newNode.children = new ArrayList<>();
            node.children.add(newNode);
        });
    }
    
    public static Document getPage(String url) throws IOException{        
        Document page = Jsoup.connect(url).timeout(10*1000).get();
        page.setBaseUri(url);
        return page;                            
    }
    
    public static Elements getLinks(Document page){
        Elements content = page.select("[id=mw-content-text]");
        Elements links = content.select("a[href^=/wiki]:not(a[href^=/wiki/File])"
                + ":not(a[href^=/wiki/Wikipedia]):not(a[href^=/wiki/Help]):not(a[href^=/wiki/Talk])"
                + ":not(a[href^=/wiki/Portal])");
        return links;
    }
    
    public static String getHeading(Document page){
        Element content = page.select("[id=firstHeading]").first();
        return content.text();
    }
    
    
}
