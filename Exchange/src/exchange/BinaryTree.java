package exchange;

import java.util.LinkedList;

/**
 * This binary tree class is used for bid/offer book data structure
 * This binary tree class allow duplicate Keys 
 * Value of nodes is a linkedlist: When adding a node with existing key, add the value to the linkedlist
 * 
 * 
 * @author yininggao
 *
 * @param <Key>
 * @param <Value>
 */
public class BinaryTree<Key extends Comparable<Key>, Value> {
    protected Node root;            

    protected class Node {
        protected Key key;          
        protected LinkedList<Value> nodelist;        
        protected Node left, right;  

        public Node(Key key, Value val){
        	this.nodelist=new LinkedList<Value>();
        	this.key=key;
        	this.nodelist.add(val);       	       	
        }
        
        public LinkedList<Value> getlist(){
        	return nodelist;
        }
        
        public Key getkey(){
        	return key;
        }        
    }
   
    public BinaryTree() { }
    
    public boolean isEmpty() {
        return root==null;
    }
    
    //add new <key, value> to tree
    public void add(Key key, Value val) {
    	if (root==null) {root=new Node(key,val);}   	
    	else if (get(key)!=null) get(key).add(val); //if key exist, add to linkedlist
    	else addKey(key,val);  //create new key		
    }	
    
    public boolean contains(Key key){ return get(key)!=null;}
    
    //get the nodelist of a specific key
    public LinkedList<Value> get(Key key) { return get(root, key); }

    private  LinkedList<Value> get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if(cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.nodelist;
    }

    //If key doesn't exsit : add new key
    private void addKey(Key key, Value val) { root = addKey(root, key, val); }

    private Node addKey(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0)  x.left  = addKey(x.left, key, val);
        else x.right = addKey(x.right, key, val);
        return x;
    }
    
    //delete: need key and id
    public void delete(Key key,Value val) {
    	get(key).remove(val);
        if (get(key).size()==0) deleteKey(key);
    }

    private void deleteKey(Key key) { root = deleteKey(root, key); }

    private Node deleteKey(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = deleteKey(x.left,  key);
        else if (cmp > 0) x.right = deleteKey(x.right, key);
        else { 
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMinKey(t.right);
            x.left = t.left;
        } 
        return x;
    } 

    //Returns the smallest sub node.
    public Node min() {
    	if (!this.isEmpty()) return min(root);
    	else return null;
    }

    private Node min(Node x) { 
        if (x.left == null) return x; 
        else return min(x.left); 
    } 
    
    // Returns the largest sub node.
    public Node max(){
    	if (!this.isEmpty()) return max(root);
    	else return null;
    }
    
    private Node max(Node x) {
        if (x.right == null) return x; 
        else return max(x.right); 
    } 
    
    //Removes the smallest key.
    private void deleteMinKey() { root = deleteMinKey(root);}

    private Node deleteMinKey(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMinKey(x.left);
        return x;
    }
   
    //Removes the largest key.
    private void deleteMaxKey() { root = deleteMaxKey(root);}

    private Node deleteMaxKey(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMaxKey(x.right);
        return x;
    }
    
    //Removes the first value in the linkedlist with smallest key
    public void deleteMin(){
    	this.min().nodelist.remove();
    	if (this.min().nodelist.size()==0) deleteMinKey();
    }
    
    //Removes the first value in the linkedlist with max key
    public void deleteMax(){
    	this.max().nodelist.remove();
       	if (this.max().nodelist.size()==0) deleteMaxKey();
    }
    
    public String inOrderWalk() {
        return inOrderWalk(root);
     }

    public String reverseWalk() {
        return reverseWalk(root);
     }
    
    //print string from smallest key to largest
    //for each key, print linkedlist inorder
     public  String inOrderWalk(Node node) {
        if (node == null) return "\n";
        String string = "";
        @SuppressWarnings("unchecked")
		LinkedList<Value> list = (LinkedList<Value>) node.nodelist.clone();
        while (list.size()>0){
        	string=string+list.remove().toString()+"\n";
        }
        	
        if (node.left != null)
           string = inOrderWalk(node.left)  + string;
        if (node.right != null)
           string = string +  inOrderWalk(node.right);
        return string;
     }
     
     //print string from largest key to smallest
     //for each key, print linkedlist inorder
     public  String reverseWalk(Node node) {
         if (node == null) return "\n";
         String string = "";
         @SuppressWarnings("unchecked")
		LinkedList<Value> list=(LinkedList<Value>) node.nodelist.clone();
         while (!list.isEmpty()){
         	string=string+list.remove().toString()+"\n";
         }
         	
         if (node.right != null)
            string = reverseWalk(node.right)  + string;
         if (node.left != null)
            string = string +reverseWalk(node.left);
         return string;
      }
 
     public String toString(){
    	 return inOrderWalk();
     }
 
}