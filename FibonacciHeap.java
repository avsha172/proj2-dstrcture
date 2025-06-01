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
		return 46; // should be replaced by student code

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
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		assert(heap2.Invalidate == false);
		if (heap2 == null || heap2.firstNode == null) {
			return; // Nothing to meld
		}
		HeapNode firstNode2 = heap2.firstNode;
		HeapNode lastNode2 = heap2.firstNode.prev;
		HeapNode firstNode1 = this.firstNode;
		HeapNode lastNode1 = this.firstNode.prev;

		lastNode1.next = firstNode2;
		firstNode2.prev = lastNode1;
		
		lastNode2.next = firstNode1;
		firstNode1.prev = lastNode2;
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
		if(this.min.key > heap2.min.key){
			this.min = heap2.min;
		}

		// Invalidate heap2 after melding
		Invalidate = true;

		return; // should be replaced by student code   		
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
		public int rank;
		public boolean realNode = true;
		public int lostChildren = 0;
		public HeapNode(int key, String info, HeapNode prev, HeapNode parent){
			this.key = key;
			this.info = info;
			this.prev = prev;
			this.parent = parent;
		}
		public HeapNode(){
			realNode = false;
		}
	}
}
