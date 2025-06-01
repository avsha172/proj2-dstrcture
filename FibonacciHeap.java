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
		size--;

	    // Remove min from root list

	    // If min is the first node in the root list, update firstNode
	    if (this.firstNode == oldMin) {
			// System.out.println("firstNode is oldMin"+min.key);
			// Edge case 2: min is the only node in the root list (but may have children)
	        if (oldMin.next == oldMin) {
	            this.firstNode = null;
	        } else {
	            this.firstNode = oldMin.next;
	        }
	    }
		removeNode(oldMin);

	    // If min has children, add them to root list
	    if (oldMin.child != null) {

	        HeapNode child = oldMin.child;
	        HeapNode start = child;
	        do {
	            child.parent = null;
	            child = child.next;
				totalCuts++; // Count the cut when moving child to root list
	        } while (child != start);
	        // Splice children into root list
	        if (this.firstNode != null) {
	            linkNodeLists(this.firstNode, oldMin.child);
	        } else {
	            this.firstNode = oldMin.child;
	        }
	    }

		
	    // Set new min (will be fixed in consolidation)
	    // If heap is now empty, set min to null
	    if (this.size == 0) {
	        this.min = null;
	        this.firstNode = null;
	        this.numTrees = 0;
			return 0;
	    }
		else
		{
			this.min = this.firstNode;
			int links = consolidate();
			totalLinks += links;
	    	return links;
		}
	}

	private int consolidate() {

	    // Step 1: Collect all roots into a list to avoid issues with root list mutation
	    java.util.List<HeapNode> roots = new java.util.ArrayList<>();
	    if (firstNode != null) {
	        HeapNode curr = firstNode;
	        do {
	            roots.add(curr);
	            curr = curr.next;
	        } while (curr != firstNode);
	    }
		int maxDegree = (int) Math.ceil(c*Math.log(size) + 2);
	    HeapNode[] bucket = new HeapNode[maxDegree];
	    int links = 0;
	    // Step 2: Consolidate using the collected roots
	    for (HeapNode x : roots) {
	        int d = x.rank;
	        while (bucket[d] != null) {
	            HeapNode y = bucket[d];
	            if (x == y) {
	                // Defensive: break to avoid infinite loop and corruption
	                break;
	            }
	            if (x.key > y.key) {
	                HeapNode temp = x;
	                x = y;
	                y = temp;
	            }
	            // Remove y from root list
	            removeNode(y);
	            x.addChild(y);
	            bucket[d] = null;
	            d++;
	            links++;
	        }
	        bucket[d] = x;
	    }

	    // Step 3: Rebuild root list & discover new min
	    firstNode = null;
	    min = null;
	    numTrees = 0;
	    for (HeapNode n : bucket) if (n != null) {
	        if (firstNode == null) {
	            firstNode = n;
	            n.next = n.prev = n;
	        } else {
	            HeapNode last = firstNode.prev;
	            last.next = n;
	            n.prev = last;
	            n.next = firstNode;
	            firstNode.prev = n;
	        }
	        if (min == null || n.key < min.key) min = n;
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
				if (x.key < min.key) {
					min = x; // Update min if necessary
				}
				int cuts = cutNode(x);
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
		x.prev = firstNode.prev;
		x.next = firstNode;
		firstNode.prev.next = x;
		firstNode.prev = x;
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
		decreaseKey(x, x.key);
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
		if(heap2.Invalidate == true){
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
		public void addChild(HeapNode newChild) {
			if (this.child == null) {
				this.child = newChild;
				newChild.next = newChild; // Point to itself
				newChild.prev = newChild; // Point to itself
			} else {
				// Link the new child to the existing child
				newChild.next = newChild;
				newChild.prev = newChild;
				linkNodeLists(this.child, newChild);


				// HeapNode lastChild = this.child.prev;
				// lastChild.next = newChild;
				// newChild.prev = lastChild;
				// newChild.next = this.child;
				// this.child.prev = newChild;
			}
			newChild.parent = this; // Set parent of the new child
			this.rank++; // Increase rank as we added a child
		}
	}

	// Add a package-private getter for firstNode for testing purposes
	HeapNode getFirstNode() {
	    return this.firstNode;
	}
}
