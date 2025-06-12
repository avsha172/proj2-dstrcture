import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ToolTipManager;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	private HeapNode min;
	private int c;
	private int size;
	private int numTrees;
	private boolean Invalidate = false;
	private int totalLinks = 0;
	private int totalCuts = 0;
	/**
	 *
	 * Constructor to initialize an empty heap.
	 * pre: c >= 2.
	 *
	 */
	public FibonacciHeap(int c)
	{
		this.c = c;
		// should be replaced by student code
	}
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		HeapNode node = new HeapNode(key, info);
		if (min == null) {
			min = node;
			node.next = node;
			node.prev = node;
		}
		else{
			node.next = min;
			node.prev = min.prev;
			min.prev.next = node;
			min.prev = node;
			if(node.key < min.key) {
				min = node; // Update min if the new node has a smaller key
			}
		}
		size++;
		numTrees++;
		return node;
	}

	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return min; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the minimal item.
	 * Return the number of links.
	 *
	 */
	
	public int deleteMin()
	{
		// Edge case 1: Heap is empty
	    if (min == null) {
			if(size != 0){
				System.err.println(size);
				System.err.println("heap should be empty but it actually isnt");
				throw new AssertionError("Min should be 20 after deleting 10");
			}
	        return 0;
	    }
		size--;
		if (this.size == 0) {
	        min = null;
	        numTrees = 0;
			return 0;
	    }

	    HeapNode oldMin = min;
		removeNode(min);
	    // Remove min from root list
	    // If min has children, add them to root list
	    if (oldMin.child != null) 
		{

	        HeapNode child = oldMin.child;
	        HeapNode start = child;
			// HeapNode[] children = new HeapNode[oldMin.rank];
			int i = 0;

	        do {
				// children[i] = child;
				if(child.parent == null){
					System.err.println("child.parent is null, something went wrong");
					break;
				}
				child.parent = null;
				
	            child = child.next;
				i++;
	        } while (child != start && i<oldMin.rank);
			if(child != start) {
				System.err.println("child is not equal to start, something went wrong");
			}
	        // Add children to root list
			// more than one root node in the heap
	        if(numTrees>1){
				min = oldMin.next;
				if(min == oldMin) {
					System.err.println("min is equal to oldMin, something went wrong very bad");
				}
				linkNodeLists(min, start);
			}
			else{
				min = start; // If this was the only root node, set min to the first child
			}
			numTrees += oldMin.rank-1; // Increase numTrees by the number of children added
			totalCuts += oldMin.rank; // Count the cuts made when removing children
	        oldMin.child = null; // Clear the child pointer of oldMin
	        oldMin.rank = 0; // Reset rank since we are removing children
		}
		else // has no children
		{
			numTrees--;
	        min = oldMin.next; // Set min to the next node in the root list
			if(oldMin == min.next && oldMin == min.prev && min.next == min) {
				System.err.println("oldMin is equal to min.next, something went wrong");
			}
	    }

		
	    // Set new min (will be fixed in consolidation)
	    // If heap is now empty, set min to null
		int links = consolidate();
		totalLinks += links;
		return links;
	}

	public int consolidate() {
		int links = 0;
		// Step 1: Collect all roots into a list before mutating the root list
		HeapNode[] rootNodes = new HeapNode[numTrees];
		HeapNode curr = min;
		for (int i = 0; i < numTrees; i++) {
			rootNodes[i] = curr;
			curr = curr.next;
		}
		if(curr != min) {
			System.err.println("curr is not equal to min, something went wrong");
		}
		// Step 2: Consolidate using the collected roots
		ArrayList<HeapNode> bucket = new ArrayList<>();
		for (HeapNode node : rootNodes) {
			curr = node;
			int d = curr.rank;
			if(d>= bucket.size()) {
				// Extend the bucket to accommodate the rank
				for (int j = bucket.size(); j <= d; j++) {
					bucket.add(null);
				}
			}
			if(bucket.get(d) == curr) {
				break;
			}
			while (bucket.get(d) != null) {
				HeapNode y = bucket.get(d);
				if (curr.key > y.key) {
					HeapNode temp = curr;
					curr = y;
					y = temp;
				}
				removeNode(y);
				curr.addChild(y);
				bucket.set(d, null);
				d++;
				links++;
				if(d>=bucket.size()) {

					bucket.add(null);
					break; // Break if we need to extend the bucket
				}
			}
			bucket.set(d, curr);
		}
		// Step 3: Rebuild root list & discover new min
		numTrees = 0;
		min = null;
		HeapNode first = null, last = null;
		for (HeapNode n : bucket) if (n != null) {
			if (min == null || n.key < min.key) min = n;
			if (first == null) {
				first = n;
				last = n;
				n.next = n.prev = n;
			} else {
				// Insert n after last
				n.prev = last;
				n.next = first;
				last.next = n;
				first.prev = n;
				last = n;
			}
			numTrees++;
		}
		return links;
	}
	private void removeNode(HeapNode y) {
		y.prev.next = y.next;
		y.next.prev = y.prev;
	}


	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap.
	 * Return the number of cuts.
	 * 
	 */
	public int decreaseKey(HeapNode x, int diff) 
	{    
		if (Invalidate == true) {
			return 0; // Invalid heap, cannot perform operation
		}
		if (x == null || diff <= 0 || x.key - diff < 0) {
			throw new IllegalArgumentException("Invalid key decrease operation");
		}
		if(x.parent == null) {
			// x is a root node
			x.key -= diff;
			
			if (x.key < min.key) {
				min = x; // Update min if necessary
			}
			return 0; // No cuts needed for root nodes
		}
		else {
			// x is a child node
			x.key -= diff;
			if (x.key < x.parent.key) {
				// Cut x from its parent
				int cuts = cutNode(x);
				if (x.key < min.key) {
					min = x; // Update min if necessary
				}
				return cuts; // Return the number of cuts made
			}
			return 0; // No cuts made
		}
	}

	private int cutNode(HeapNode x) {
		if(x.parent == null) {
			return 0; // No cuts needed if x has no parent
		}
		int cuts = 1;
		HeapNode parent = x.parent;
		// Remove x from its parent's child list
		if (parent.child == x) {
			if(x.next == x) {
				// x is the only child
				parent.child = null;
			} else {
				parent.child = x.next; // Update parent's child pointer
			}
		}
		removeNode(x);
		parent.rank--;
		parent.lostChildren++;
		// Add x to the root list
		x.next = x;
		x.prev = x;
		linkNodeLists(min, x);
		numTrees++;
		x.parent = null; // Remove parent reference BEFORE possible recursive cut
		if (parent.lostChildren >= c) {
			cuts += cutNode(parent);
			parent.lostChildren = 0; // Reset lostChildren after parent is cut
		}
		return cuts; // Return the number of cuts made
	}
	/**
	 * 
	 * Delete the x from the heap.
	 * Return the number of links.
	 *
	 */
	public int delete(HeapNode x) 
	{   
		HeapNode par = x.parent;
		int cuts = decreaseKey(x, x.key);
		int links = deleteMin();
		return links;
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return totalLinks;
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return totalCuts;
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		if(heap2.Invalidate == true){
			return;
		}
		if (heap2 == null || heap2.min == null) {
			heap2.InvalidateHeap();
			return; // Nothing to meld
		}
		if(size == 0) {
			this.min = heap2.min;
			this.min = heap2.min;
			this.size = heap2.size;
			this.numTrees = heap2.numTrees;
			heap2.InvalidateHeap();
			return;
		}
		linkNodeLists(this.min, heap2.min);
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
		if(this.min.key > heap2.min.key){
			this.min = heap2.min;
		}
		this.totalCuts += heap2.totalCuts;
		this.totalLinks += heap2.totalLinks;
		// Invalidate heap2 after melding
		heap2.InvalidateHeap();
	}
	private void InvalidateHeap() {
		this.Invalidate = true;
		this.min = null;
		this.min = null;
		this.size = 0;
		this.numTrees = 0;
	}

	private static HeapNode linkNodeLists(HeapNode node1, HeapNode node2) {
		HeapNode lastNode2 = node2.prev;
		HeapNode lastNode1 = node1.prev;

		lastNode1.next = node2;
		node2.prev = lastNode1;
		
		lastNode2.next = node1;
		node1.prev = lastNode2;

		return node1; // Return the head of the new linked list
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return numTrees;
	}

	public void display(HeapNode c) {
    System.out.print("(");
    if (c == null) {
      System.out.print(")");
      return;
    } else {
      HeapNode temp = c;
      do {
        System.out.print(temp.key);
        HeapNode k = temp.child;
        display(k);
        System.out.print("->");
        temp = temp.next;
      } while (temp != c);
      System.out.print(")");
    }
  }

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child = null; // Child node, null if it has no children
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent = null; // Parent node, null if it's a root node
		public int rank=0;
		public int lostChildren = 0;
		public HeapNode(int key, String info){
			this.key = key;
			this.info = info;
			prev = this;
			next = this; // Point to itself initially
		}
		public void addChild(HeapNode newChild) {
			newChild.next = newChild; // Point to itself
			newChild.prev = newChild; // Point to itself
			if (child == null) {
				child = newChild;
			} else {
				// Link the new child to the existing child
				linkNodeLists(child, newChild);
			}
			newChild.parent = this; // Set parent of the new child
			this.rank++; // Increase rank as we added a child
		}
	}

	// Add a package-private getter for min for testing purposes
	HeapNode getFirstNode() {
	    return this.min;
	}
}


