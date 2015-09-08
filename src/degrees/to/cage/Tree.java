/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package degrees.to.cage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
        root.next = null;
    }

    public static class Node<T> {
        public T data;
        public Node<T> parent;
        public Node<T> next;
        public List<Node<T>> children;
        
        public boolean hasChildren(){
            return children.size() > 0;
        }
        
        public Node<T> getChild(int i){
            return children.get(i);
        }
        
        public T getData(){
            return data;
        }
    }
    
    public Node<T> getRoot(){
        return root;
    }    
    
}
