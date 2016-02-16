package exchange;

import junit.framework.TestCase;

public class Test_BinaryTree extends TestCase {
	
	public void test1(){
		BinaryTree<Integer, String> bt=new BinaryTree<Integer, String>();
		
		assertTrue(bt.isEmpty());
		//when there's no node	
		assertTrue(bt.min()==null);
		//add node 
		bt.add(1, "First");

		assertTrue(bt.get(1).removeFirst().equals("First"));

		
		//add new node with same key
		bt.add(1, "Second");
		assertTrue(bt.get(1).size()==1);
		
		bt.add(2, "First");		
		bt.add(3, "First");
		bt.add(3, "Second");
		bt.add(1, "Third");
		
		//return min and max
		assertTrue(bt.max().getkey()==3);
		
		//test inorderwalk and reverse order walk
		System.out.println(bt.inOrderWalk());
		System.out.println("==============");
        System.out.println(bt.reverseWalk());
		
		//delete node
		bt.delete(1, "Second");
		bt.delete(1, "Third");
		assertTrue(bt.get(1)==null);
		assertFalse(bt.contains(1));	

		//delete min
		bt.deleteMin();
		assertTrue(bt.min().getkey().equals(3));

	

		
	}

}
