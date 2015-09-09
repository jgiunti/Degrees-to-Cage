/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degreestocage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class Node<T> {
    
    public T data;
    public Node<T> parent;
    public Node<T> next;
    public List<Node<T>> children = new ArrayList<>();
    
    public boolean hasChildren(){
        return children.size() > 0;
    }
    
    public Node<T> addChild(Node<T> child){
        this.children.add(child);
    }
}
