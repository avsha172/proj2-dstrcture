public class FibonacciHeapTester {
    public static void main(String[] args) {
        // Create two Fibonacci heaps
        FibonacciHeap heap1 = new FibonacciHeap(2);
        FibonacciHeap heap2 = new FibonacciHeap(2);

        // Insert elements into heap1
        heap1.insert(10, "A");
        heap1.insert(5, "B");
        heap1.insert(20, "C");

        // Insert elements into heap2
        heap2.insert(7, "X");
        heap2.insert(3, "Y");
        heap2.insert(15, "Z");

        // Print and assert min before meld
        int heap1MinBefore = heap1.findMin() != null ? heap1.findMin().key : Integer.MAX_VALUE;
        int heap2MinBefore = heap2.findMin() != null ? heap2.findMin().key : Integer.MAX_VALUE;
        int heap1SizeBefore = heap1.size();
        int heap2SizeBefore = heap2.size();
        System.out.println("Heap1 min before meld: " + heap1MinBefore);
        System.out.println("Heap2 min before meld: " + heap2MinBefore);
        assert heap1MinBefore == 5 : "Heap1 min before meld should be 5";
        assert heap2MinBefore == 3 : "Heap2 min before meld should be 3";
        assert heap1SizeBefore == 3 : "Heap1 size before meld should be 3";
        assert heap2SizeBefore == 3 : "Heap2 size before meld should be 3";

        // Meld heap2 into heap1
        heap1.meld(heap2);

        // Print and assert min and size after meld
        int heap1MinAfter = heap1.findMin() != null ? heap1.findMin().key : Integer.MAX_VALUE;
        int heap1SizeAfter = heap1.size();
        int heap1NumTreesAfter = heap1.numTrees();
        System.out.println("Heap1 min after meld: " + heap1MinAfter);
        System.out.println("Heap1 size after meld: " + heap1SizeAfter);
        System.out.println("Heap1 numTrees after meld: " + heap1NumTreesAfter);
        assert heap1MinAfter == 3 : "Heap1 min after meld should be 3";
        assert heap1SizeAfter == 6 : "Heap1 size after meld should be 6";
        // numTrees after meld should be 6 if all are roots (depends on your meld logic)
        assert heap1NumTreesAfter == 6 : "Heap1 numTrees after meld should be 6";

        // Try to use heap2 after meld (should be invalidated)
        try {
            int heap2MinAfter = heap2.findMin() != null ? heap2.findMin().key : Integer.MAX_VALUE;
            int heap2SizeAfter = heap2.size();
            assert heap2MinAfter == Integer.MAX_VALUE : "Heap2 min after meld should be invalid (null)";
            assert heap2SizeAfter == 0 : "Heap2 size after meld should be 0";
        } catch (Exception e) {
            System.out.println("Heap2 is invalid after meld (as expected).");
        }
        System.out.println("All assertions passed.");
    }
}
