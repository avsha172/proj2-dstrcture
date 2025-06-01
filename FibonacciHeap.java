/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	private HeapNode min;
	private HeapNode firstNode;
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

	public FibonacciHeap(int c, int key, String info)
	{
		this.c = c;
		initFirstNode(key, info);
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
		if (this.size == 0) {
			initFirstNode(key, info);
			return this.firstNode;
		}
		
		FibonacciHeap heap2 = new FibonacciHeap(c, key, info);
		meld(heap2);
		assert(heap2.firstNode == this.firstNode.prev);
		return this.firstNode.prev; // should be replaced by student code
	}

	private void initFirstNode(int key, String info) {
		this.firstNode = new HeapNode(key, info, null, null);
		this.min = this.firstNode;
		this.firstNode.next = this.firstNode;
		this.firstNode.prev = this.firstNode;
		this.size = 1;
		this.numTrees = 1;
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
	    if (this.min == null) {
	        return 0;
	    }

	    HeapNode oldMin = this.min;
	    int links = 0; // Number of links performed (to be updated in consolidation)

	    // Edge case 2: min is the only node in the root list (but may have children)
	    boolean onlyRoot = (oldMin.next == oldMin);

	    // Remove min from root list
	    oldMin.prev.next = oldMin.next;
	    oldMin.next.prev = oldMin.prev;

	    // If min is the first node in the root list, update firstNode
	    if (this.firstNode == oldMin) {
	        if (onlyRoot) {
	            this.firstNode = null;
	        } else {
	            this.firstNode = oldMin.next;
	        }
	    }

	    // If min has children, add them to root list
	    if (oldMin.child != null) {
	        HeapNode child = oldMin.child;
	        HeapNode start = child;
	        do {
	            child.parent = null;
	            child = child.next;
	        } while (child != start);
	        // Splice children into root list
	        if (this.firstNode != null) {
	            linkNodeLists(this.firstNode, oldMin.child);
	        } else {
	            this.firstNode = oldMin.child;
	        }
	        this.numTrees += oldMin.rank;
	    }

	    this.size--;
		
	    // Set new min (will be fixed in consolidation)
	    this.min = this.firstNode;
	    // TODO: Consolidation step goes here
		links += consolidate();
	    // If heap is now empty, set min to null
	    if (this.size == 0) {
	        this.min = null;
	        this.firstNode = null;
	        this.numTrees = 0;
	    }

	    return links;
	}
	private int consolidate() {
		if (this.firstNode == null) {
			return 0; // No nodes to consolidate
		}
		int links = 0;
		HeapNode[] degreeTable = new HeapNode[(int)(c*Math.ceil(Math.log(this.size)))]; // Array to hold trees by degree, we proved Fibonacci heap has at most a tree with degree c*log(n).
		HeapNode smaller = this.firstNode;
		do {
			int degree = smaller.rank;
			while (degreeTable[degree] != null) {
				HeapNode larger = degreeTable[degree];
				if (smaller.key > larger.key) {
					// Swap current and other
					HeapNode temp = smaller;
					smaller = larger;
					larger = temp;
				}
				if(larger == this.firstNode) {
					this.firstNode = smaller; // Update firstNode if necessary
				}
				// Link larger to current
				if (larger == this.min) {
					this.min = smaller; // Update min if necessary
				}
				// Remove larger from root list
				larger.prev.next = larger.next;
				larger.next.prev = larger.prev;
				smaller.addChild(larger);
				smaller.rank++;
				degreeTable[degree] = null; // Clear the slot
				degree++;
				links++;
			}
			degreeTable[degree] = smaller; // Place current in the degree table
			smaller = smaller.next;
		} while (smaller != this.firstNode);
		int numtrees = 0;
		for (HeapNode node : degreeTable) {
			if (node != null) {
				numtrees++;
				assert node.key < this.min.key : "min should always be the smallest key";
			}
		}
		this.numTrees = numtrees; // Update number of trees
		// Consolidation logic to be implemented
		// This will involve linking trees of the same rank and updating the min pointer
		return links;
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
		return 46; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the x from the heap.
	 * Return the number of links.
	 *
	 */
	public int delete(HeapNode x) 
	{    
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return totalLinks; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return totalCuts; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		if(heap2.Invalidate == false){
			return;
		}
		if (heap2 == null || heap2.firstNode == null) {
			return; // Nothing to meld
		}
		if(size == 0) {
			this.firstNode = heap2.firstNode;
			this.min = heap2.min;
			this.size = heap2.size;
			this.numTrees = heap2.numTrees;
			heap2.InvalidateHeap();
			return;
		}
		linkNodeLists(this.firstNode, heap2.firstNode);
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
		if(this.min.key > heap2.min.key){
			this.min = heap2.min;
		}

		// Invalidate heap2 after melding
		heap2.InvalidateHeap();
	}
	private void InvalidateHeap() {
		this.Invalidate = true;
		this.firstNode = null;
		this.min = null;
		this.size = 0;
		this.numTrees = 0;
	}

	private void linkNodeLists(HeapNode node1, HeapNode node2) {
		HeapNode lastNode2 = node2.prev;
		HeapNode lastNode1 = node1.prev;

		lastNode1.next = node2;
		node2.prev = lastNode1;
		
		lastNode2.next = node1;
		node1.prev = lastNode2;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size; // should be replaced by student code
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return numTrees; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank=0;
		public int lostChildren = 0;
		public HeapNode(int key, String info, HeapNode prev, HeapNode parent){
			this.key = key;
			this.info = info;
			this.prev = prev;
			this.parent = parent;
		}
		public void addChild(HeapNode child) {
			if (this.child == null) {
				this.child = child;
			} else {
				// Link the new child to the existing child
				HeapNode lastChild = this.child.prev;
				lastChild.next = child;
				child.prev = lastChild;
				child.next = this.child;
				this.child.prev = child;
			}
			child.parent = this; // Set parent of the new child
			this.rank++; // Increase rank as we added a child
		}
	}
}
