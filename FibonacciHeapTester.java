public class FibonacciHeapTester {
    public static void main(String[] args) {
        testInsertAndFindMin();
        testDeleteMin();
        testDeleteMinUntilEmpty();
        testMeldAndDeleteMin();
        testMeldAssertions();
        System.out.println("All tests passed.");
    }

    private static void testInsertAndFindMin() {
        FibonacciHeap heap = new FibonacciHeap(2);
        assert heap.findMin() == null : "Heap should be empty initially";
        heap.insert(10, "A");
        assert heap.findMin().key == 10 : "Min should be 10 after first insert";
        heap.insert(5, "B");
        assert heap.findMin().key == 5 : "Min should be 5 after inserting 5";
        heap.insert(20, "C");
        assert heap.findMin().key == 5 : "Min should still be 5";
        System.out.println("testInsertAndFindMin passed.");
    }

    private static void testDeleteMin() {
        FibonacciHeap heap = new FibonacciHeap(2);
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");
        int links = heap.deleteMin();
        assert heap.findMin().key == 10 : "Min should be 10 after deleting 5";
        assert heap.size() == 2 : "Size should be 2 after one deleteMin";
        System.out.println("testDeleteMin passed. Links: " + links);
    }

    private static void testDeleteMinUntilEmpty() {
        FibonacciHeap heap = new FibonacciHeap(2);
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        assert heap.findMin() == null : "Heap should be empty after deleting all";
        assert heap.size() == 0 : "Size should be 0 after deleting all";
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
        assert heap1.findMin().key == 2 : "Min should be 2 after meld";
        heap1.deleteMin();
        assert heap1.findMin().key == 3 : "Min should be 3 after deleting 2";
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
        assert heap3MinBefore == 5 : "Heap3 min before meld should be 5";
        assert heap4MinBefore == 3 : "Heap4 min before meld should be 3";
        assert heap3SizeBefore == 3 : "Heap3 size before meld should be 3";
        assert heap4SizeBefore == 3 : "Heap4 size before meld should be 3";
        heap3.meld(heap4);
        int heap3MinAfter = heap3.findMin() != null ? heap3.findMin().key : Integer.MAX_VALUE;
        int heap3SizeAfter = heap3.size();
        int heap3NumTreesAfter = heap3.numTrees();
        System.out.println("Heap3 min after meld: " + heap3MinAfter);
        System.out.println("Heap3 size after meld: " + heap3SizeAfter);
        System.out.println("Heap3 numTrees after meld: " + heap3NumTreesAfter);
        assert heap3MinAfter == 3 : "Heap3 min after meld should be 3";
        assert heap3SizeAfter == 6 : "Heap3 size after meld should be 6";
        assert heap3NumTreesAfter == 6 : "Heap3 numTrees after meld should be 6";
        try {
            int heap4MinAfter = heap4.findMin() != null ? heap4.findMin().key : Integer.MAX_VALUE;
            int heap4SizeAfter = heap4.size();
            assert heap4MinAfter == Integer.MAX_VALUE : "Heap4 min after meld should be invalid (null)";
            assert heap4SizeAfter == 0 : "Heap4 size after meld should be 0";
        } catch (Exception e) {
            System.out.println("Heap4 is invalid after meld (as expected).");
        }
        System.out.println("testMeldAssertions passed.");
    }
}
