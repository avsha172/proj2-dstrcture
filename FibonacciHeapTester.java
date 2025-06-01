import java.util.*;

// import FibonacciHeap.HeapNode;


public class FibonacciHeapTester {
    public static void main(String[] args) {
        testInsertAndFindMin();
        testDeleteMin();
        // testDeleteMinUntilEmpty();
        // testMeldAndDeleteMin();
        // testMeldAssertions();
        // testTotalCutsAndLinks();
        // testFirstNodeAndStructure();
        // testRandomizedDeleteMin();
        // testRandomizedNumTreesAfterDeleteMin();
        // testDecreaseKeyFunctionality();
        // testManyDecreaseKeyAndDeleteMin();
        // testDeleteAndDecreaseKeyOnSameTree();
        // System.out.println("All tests passed.");
    }

    private static void testInsertAndFindMin() {
        FibonacciHeap heap = new FibonacciHeap(2);
        if (heap.findMin() != null) throw new AssertionError("Heap should be empty initially");
        heap.insert(10, "A");
        if (heap.findMin().key != 10) throw new AssertionError("Min should be 10 after first insert");
        heap.insert(5, "B");
        if (heap.findMin().key != 5) throw new AssertionError("Min should be 5 after inserting 5");
        heap.insert(20, "C");
        if (heap.findMin().key != 5) throw new AssertionError("Min should still be 5");
        System.out.println("testInsertAndFindMin passed.");
    }

    private static void testDeleteMin() {
         Random rand = new Random(234);
        int n = 40;
        FibonacciHeap heap = new FibonacciHeap(2);
        List<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        List<Integer> uniqueVals = new ArrayList<>();
        for (int i = 0; i < n; i++) uniqueVals.add(i); // Ensure uniqueness
        Collections.shuffle(uniqueVals, rand);
        for (int i = 0; i < n; i++) {
            nodes.add(heap.insert(uniqueVals.get(i), "val" + i));
        }
        System.out.println();System.out.println();System.out.println();
        display(heap.getFirstNode());
        heap.deleteMin();
        System.out.println();System.out.println();System.out.println();
        display(heap.getFirstNode());
        heap.deleteMin();
        System.out.println();System.out.println();System.out.println();
        display(heap.getFirstNode());
        heap.deleteMin();
        System.out.println();System.out.println();System.out.println();
        display(heap.getFirstNode());

    }

    private static void testDeleteMinUntilEmpty() {
        FibonacciHeap heap = new FibonacciHeap(2);
        FibonacciHeap.HeapNode node = heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        if (heap.findMin() != null) throw new AssertionError("Heap should be empty after deleting all");
        if (heap.size() != 0) throw new AssertionError("Size should be 0 after deleting all");
        System.out.println("testDeleteMinUntilEmpty passed.");
    }

    private static void testMeldAndDeleteMin() {
        FibonacciHeap heap1 = new FibonacciHeap(2);
        FibonacciHeap heap2 = new FibonacciHeap(2);
        heap1.insert(7, "X");
        heap1.insert(3, "Y");
        heap2.insert(15, "Z");
        heap2.insert(2, "W");
        heap1.meld(heap2);
        if (heap1.findMin().key != 2) throw new AssertionError("Min should be 2 after meld");
        heap1.deleteMin();
        if (heap1.findMin().key != 3) throw new AssertionError("Min should be 3 after deleting 2");
        System.out.println("testMeldAndDeleteMin passed.");
    }

    private static void testMeldAssertions() {
        FibonacciHeap heap3 = new FibonacciHeap(2);
        FibonacciHeap heap4 = new FibonacciHeap(2);
        heap3.insert(10, "A");
        heap3.insert(5, "B");
        heap3.insert(20, "C");
        heap4.insert(7, "X");
        heap4.insert(3, "Y");
        heap4.insert(15, "Z");
        int heap3MinBefore = heap3.findMin() != null ? heap3.findMin().key : Integer.MAX_VALUE;
        int heap4MinBefore = heap4.findMin() != null ? heap4.findMin().key : Integer.MAX_VALUE;
        int heap3SizeBefore = heap3.size();
        int heap4SizeBefore = heap4.size();
        System.out.println("Heap3 min before meld: " + heap3MinBefore);
        System.out.println("Heap4 min before meld: " + heap4MinBefore);
        if (heap3MinBefore != 5) throw new AssertionError("Heap3 min before meld should be 5");
        if (heap4MinBefore != 3) throw new AssertionError("Heap4 min before meld should be 3");
        if (heap3SizeBefore != 3) throw new AssertionError("Heap3 size before meld should be 3");
        if (heap4SizeBefore != 3) throw new AssertionError("Heap4 size before meld should be 3");
        heap3.meld(heap4);
        int heap3MinAfter = heap3.findMin() != null ? heap3.findMin().key : Integer.MAX_VALUE;
        int heap3SizeAfter = heap3.size();
        int heap3NumTreesAfter = heap3.numTrees();
        System.out.println("Heap3 min after meld: " + heap3MinAfter);
        System.out.println("Heap3 size after meld: " + heap3SizeAfter);
        System.out.println("Heap3 numTrees after meld: " + heap3NumTreesAfter);
        if (heap3MinAfter != 3) throw new AssertionError("Heap3 min after meld should be 3");
        if (heap3SizeAfter != 6) throw new AssertionError("Heap3 size after meld should be 6");
        if (heap3NumTreesAfter != 6) throw new AssertionError("Heap3 numTrees after meld should be 6");
        try {
            int heap4MinAfter = heap4.findMin() != null ? heap4.findMin().key : Integer.MAX_VALUE;
            int heap4SizeAfter = heap4.size();
            if (heap4MinAfter != Integer.MAX_VALUE) throw new AssertionError("Heap4 min after meld should be invalid (null)");
            if (heap4SizeAfter != 0) throw new AssertionError("Heap4 size after meld should be 0");
        } catch (Exception e) {
            System.out.println("Heap4 is invalid after meld (as expected).");
        }
        System.out.println("testMeldAssertions passed.");
    }

    private static void testTotalCutsAndLinks() {
        FibonacciHeap heap = new FibonacciHeap(2);
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");
        int links = heap.deleteMin();
        int totalLinks = heap.totalLinks();
        int totalCuts = heap.totalCuts();
        if (links != totalLinks) throw new AssertionError("deleteMin should return the number of links performed");
        if (totalCuts != 0) throw new AssertionError("No cuts should have occurred yet");
        System.out.println("testTotalCutsAndLinks passed.");
    }

    private static void testFirstNodeAndStructure() {
        FibonacciHeap heap = new FibonacciHeap(2);
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");
        List<List<Integer>> structure = heapToListOfLists(heap);
        Set<Integer> rootKeys = new HashSet<>();
        for (List<Integer> tree : structure) {
            if (!tree.isEmpty()) rootKeys.add(tree.get(0));
        }
        if (!(rootKeys.contains(10) && rootKeys.contains(5) && rootKeys.contains(20))) throw new AssertionError("Root list should contain all inserted keys");
        if (heap.getFirstNode() == null) throw new AssertionError("firstNode should not be null after insertions");
        System.out.println("testFirstNodeAndStructure passed. Heap structure: " + structure);
    }

    private static void testRandomizedDeleteMin() {
        Random rand = new Random(42); // Fixed seed for reproducibility
        int n = 1000;
        FibonacciHeap heap = new FibonacciHeap(2);
        List<Integer> inserted = new ArrayList<>();
        List<Integer> uniqueVals = new ArrayList<>();
        for (int i = 0; i < n; i++) uniqueVals.add(i + 1000000); // Ensure uniqueness
        Collections.shuffle(uniqueVals, rand);
        FibonacciHeap.HeapNode keepNode = null;
        for (int i = 0; i < n; i++) {
            int val = uniqueVals.get(i);
            FibonacciHeap.HeapNode node = heap.insert(val, Integer.toString(val));
            inserted.add(val);
            if(val == 1000164){
                keepNode = node; // Keep this node for later checks
            }
        }
        Collections.sort(inserted);
        for (int i = 0; i < n; i++) {
            FibonacciHeap.HeapNode minNode = heap.findMin();
            if (minNode == null) throw new AssertionError("Heap should not be empty at iteration " + i);
            int minVal = minNode.key;
            if (minVal != inserted.get(i)){
                throw new AssertionError("deleteMin order mismatch at i=" + i + ", expected " + inserted.get(i) + ", got " + minVal);   
            }
            heap.deleteMin();
        }
        if (heap.findMin() != null) throw new AssertionError("Heap should be empty after all deleteMin");
        if (heap.size() != 0) throw new AssertionError("Heap size should be 0 after all deleteMin");
        System.out.println("testRandomizedDeleteMin passed.");
    }

    private static void testRandomizedNumTreesAfterDeleteMin() {
        Random rand = new Random(123);
        int n = 200;
        FibonacciHeap heap = new FibonacciHeap(2);
        List<Integer> uniqueVals = new ArrayList<>();
        for (int i = 0; i < n; i++) uniqueVals.add(i + 2000000); // Ensure uniqueness
        Collections.shuffle(uniqueVals, rand);
        for (int i = 0; i < n; i++) {
            heap.insert(uniqueVals.get(i), "val" + i);
        }
        for (int i = 0; i < n; i++) {
            int beforeNumTrees = heap.numTrees();
            heap.deleteMin();
            int afterNumTrees = heap.numTrees();
            if (heap.size() > 0 && afterNumTrees < 1) {
                throw new AssertionError("numTrees should be at least 1 after deleteMin, got " + afterNumTrees + " at i=" + i);
            }
            if (heap.size() == 0 && afterNumTrees != 0) {
                throw new AssertionError("numTrees should be 0 when heap is empty, got " + afterNumTrees + " at i=" + i);
            }
            if (afterNumTrees > beforeNumTrees + 20) {
                throw new AssertionError("numTrees increased too much after deleteMin: before=" + beforeNumTrees + ", after=" + afterNumTrees + ", at i=" + i);
            }
        }
        System.out.println("testRandomizedNumTreesAfterDeleteMin passed.");
    }

    private static void testDecreaseKeyFunctionality() {
        FibonacciHeap heap = new FibonacciHeap(2);
        // Build a tree: insert 20, 30, 40, 50, 60, 70
        FibonacciHeap.HeapNode n20 = heap.insert(20, "A");
        FibonacciHeap.HeapNode n30 = heap.insert(30, "B");
        FibonacciHeap.HeapNode n40 = heap.insert(40, "C");
        FibonacciHeap.HeapNode n50 = heap.insert(50, "D");
        FibonacciHeap.HeapNode n60 = heap.insert(60, "E");
        FibonacciHeap.HeapNode n70 = heap.insert(70, "F");
        // Meld all into one tree by repeated deleteMin
        heap.deleteMin(); // Remove 20, triggers consolidation
        // Now decrease key of n70 to 10 (should become new min)
        heap.decreaseKey(n70, 60);
        if (heap.findMin().key != 10) throw new AssertionError("decreaseKey should update min");
        // Decrease key of n50 to 5 (should become new min)
        heap.decreaseKey(n50, 45);
        if (heap.findMin().key != 5) throw new AssertionError("decreaseKey should update min to 5");
        // Decrease key of a root node (should not cut)
        int cutsBefore = heap.totalCuts();
        heap.decreaseKey(n40, 10); // n40 is a root
        int cutsAfter = heap.totalCuts();
        if (cutsAfter != cutsBefore) throw new AssertionError("decreaseKey on root should not cut");
        // Decrease key of a child node to less than its parent (should cut)
        // First, insert a new heap and meld to create a child
        FibonacciHeap.HeapNode n80 = heap.insert(80, "G");
        heap.decreaseKey(n80, 70); // n80 becomes 10, should be a root
        // Now, insert a new node and decrease its key to force a cut
        FibonacciHeap.HeapNode n90 = heap.insert(90, "H");
        heap.decreaseKey(n90, 85); // n90 becomes 5, should be a root
        if (heap.findMin().key != 5) throw new AssertionError("decreaseKey should update min to 5 after cut");
        System.out.println("testDecreaseKeyFunctionality passed.");
    }

    private static void testManyDecreaseKeyAndDeleteMin() {
        Random rand = new Random(2025);
        int n = 200;
        FibonacciHeap heap = new FibonacciHeap(2);
        List<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        List<Integer> uniqueVals = new ArrayList<>();
        for (int i = 0; i < n; i++) uniqueVals.add(i + 3000000); // Ensure uniqueness
        Collections.shuffle(uniqueVals, rand);
        for (int i = 0; i < n; i++) {
            nodes.add(heap.insert(uniqueVals.get(i), "val" + i));
        }
        // for (int i = 0; i < n / 2; i++) {
        //     int idx = rand.nextInt(n);
        //     FibonacciHeap.HeapNode node = nodes.get(idx);
        //     int newKey = rand.nextInt(500); // Make some keys much smaller
        //     if (newKey < node.key) {
        //         heap.decreaseKey(node, node.key - newKey);
        //     }
        // }
        int minKey = Integer.MAX_VALUE;
        for (FibonacciHeap.HeapNode node : nodes) {
            if (node.key < minKey && node.parent == null) minKey = node.key;
        }
        if (heap.findMin().key != minKey) throw new AssertionError("Min after decreaseKey should be " + minKey + ", got " + heap.findMin().key);
        int prevMin = heap.findMin().key;
        for (int i = 0; i <n/2; i++) {
            int oldSize = heap.size();
            if(oldSize == 101){
                int l = 1;
            }
            heap.deleteMin();
            if (heap.size() != oldSize - 1) throw new AssertionError("Heap size mismatch after deleteMin");
            if (heap.size() > 0) {
                int newMin = heap.findMin().key;
                if (newMin < prevMin) throw new AssertionError("Min should not decrease after deleteMin");
                prevMin = newMin;
            }
        }
        for (FibonacciHeap.HeapNode node : nodes) {
            if (node.parent == null && node.key > 0 && heap.size() > 0) {
                heap.decreaseKey(node, node.key - 1);
                break;
            }
        }
        while (heap.size() > 0) {
            heap.deleteMin();
        }
        if (heap.findMin() != null) throw new AssertionError("Heap should be empty at end");
        System.out.println("testManyDecreaseKeyAndDeleteMin passed.");
    }

    private static void testDeleteAndDecreaseKeyOnSameTree() {
        FibonacciHeap heap = new FibonacciHeap(2);
        // Insert nodes to form a tree
        FibonacciHeap.HeapNode n10 = heap.insert(10, "A");
        FibonacciHeap.HeapNode n20 = heap.insert(20, "B");
        FibonacciHeap.HeapNode n30 = heap.insert(30, "C");
        FibonacciHeap.HeapNode n40 = heap.insert(40, "D");
        FibonacciHeap.HeapNode n50 = heap.insert(50, "E");
        // Consolidate into a single tree
        heap.deleteMin(); // Remove 10, triggers consolidation
        // Now n20 is root, n30, n40, n50 are children (structure depends on consolidate)
        // Decrease key of n50 to 5 (should become new min and be cut to root list)
        heap.decreaseKey(n50, 45);
        if (heap.findMin().key != 5) throw new AssertionError("decreaseKey should update min to 5");
        if (n50.parent != null) throw new AssertionError("n50 should be a root after cut");
        // Delete n40 (should remove it from the heap)
        heap.delete(n40);
        // n40 should not be in the heap anymore
        List<List<Integer>> structure = heapToListOfLists(heap);
        for (List<Integer> tree : structure) {
            if (tree.contains(40)) throw new AssertionError("n40 should be deleted from the heap");
        }
        // Heap min and size should be correct
        if (heap.findMin().key != 5) throw new AssertionError("Min should still be 5 after delete");
        if (heap.size() != 3) throw new AssertionError("Heap size should be 3 after delete");
        System.out.println("testDeleteAndDecreaseKeyOnSameTree passed.");
    }

    // Helper: Convert heap to list of lists (each root and its children in BFS order)
    private static List<List<Integer>> heapToListOfLists(FibonacciHeap heap) {
        List<List<Integer>> result = new ArrayList<>();
        Set<FibonacciHeap.HeapNode> visited = new HashSet<>();
        FibonacciHeap.HeapNode start = heap.getFirstNode();
        if (start == null) return result;
        FibonacciHeap.HeapNode curr = start;
        do {
            result.add(treeToList(curr, visited));
            curr = curr.next;
        } while (curr != start);
        return result;
    }

    // Helper: BFS traversal of a tree rooted at node
    private static List<Integer> treeToList(FibonacciHeap.HeapNode node, Set<FibonacciHeap.HeapNode> visited) {
        List<Integer> list = new ArrayList<>();
        Queue<FibonacciHeap.HeapNode> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            FibonacciHeap.HeapNode curr = queue.poll();
            if (curr == null || visited.contains(curr)) continue;
            visited.add(curr);
            list.add(curr.key);
            if (curr.child != null) {
                FibonacciHeap.HeapNode child = curr.child;
                do {
                    queue.add(child);
                    child = child.next;
                } while (child != curr.child);
            }
        }
        return list;
    }

    // Helper: Print the keys and prevs/nexts of all nodes in a circular doubly linked list at the same level
    private static void printLevel(FibonacciHeap.HeapNode node) {
        if (node == null) {
            System.out.println("[empty level]");
            return;
        }
        List<String> nodes = new ArrayList<>();
        FibonacciHeap.HeapNode curr = node;
        do {
            String s = "(key=" + curr.key + ", prev=" + (curr.prev != null ? curr.prev.key : "null") + ", next=" + (curr.next != null ? curr.next.key : "null")+")";
            nodes.add(s);
            curr = curr.next;
        } while (curr != node);
        System.out.println(nodes);
    }
    private static void  display(FibonacciHeap.HeapNode c) {
    System.out.print("(");
    if (c == null) {
      System.out.print(")");
      return;
    } else {
      FibonacciHeap.HeapNode temp = c;
      do {
        System.out.print(temp.key);
        FibonacciHeap.HeapNode k = temp.child;
        display(k);
        System.out.print("->");
        temp = temp.next;
      } while (temp != c);
      System.out.print(")");
    }
  }
}
