import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.random.*;
// import fibHeap.fibHeap;
// import fibHeap.heapNode;

// If fibHeap.java and heapNode.java are in the same directory as Experiment.java, use:
// import fibHeap; // Removed because fibHeap cannot be resolved; ensure fibHeap.java is in the same directory or package

// Or, if they are in the default package (no package declaration), remove the import statements entirely.
public class Experiment {
    public static class heapAndNodes
    {
        FibonacciHeap heap;
        FibonacciHeap.HeapNode[] nodes;
        heapAndNodes(FibonacciHeap heap, FibonacciHeap.HeapNode[] nodes) {
            this.heap = heap;
            this.nodes = nodes;
        }
    }
    static int n = 464646; // Number of nodes in the heap
    public static class ExpTableRow {
        int c;
        double timeMs;
        double finalSize;
        double numTrees;
        double numCuts;
        double numLinks;
        double numRoots;
        ExpTableRow(int c, double timeMs, double finalSize, double numTrees, double numCuts, double numLinks, double numRoots) {
            this.c = c;
            this.timeMs = timeMs;
            this.finalSize = finalSize;
            this.numTrees = numTrees;
            this.numCuts = numCuts;
            this.numLinks = numLinks;
            this.numRoots = numRoots;
        }
    }

    public static void printTable(String title, List<ExpTableRow> rows) {
        System.out.println("\n" + title);
        System.out.printf("%-5s | %-12s | %-12s | %-12s | %-12s | %-12s \n", "c", "Time(ms)", "FinalSize", "NumTrees", "NumCuts", "NumLinks" );
        for (ExpTableRow row : rows) {
            System.out.printf("%-5d | %-12.2f | %-12.2f | %-12.2f | %-12.2f | %-12.2f\n",
                row.c, row.timeMs, row.finalSize, row.numTrees, row.numCuts, row.numLinks);
        }
    }

    public static void main(String[] args) {
        int[] cLst = {2,3,4,10,20,100,1000,5000};
        int numRuns = 20;
        List<ExpTableRow> exp1Rows = new ArrayList<>();
        List<ExpTableRow> exp2Rows = new ArrayList<>();
        // Run exp1 for all c
        for (int c : cLst) {
            long sumTime1 = 0;
            int sumSize1 = 0;
            int sumTrees1 = 0;
            int sumRoots1 = 0;
            int sumCuts1 = 0;
            int sumLinks1 = 0;
            for(int i = 0; i < numRuns; i++) {
                heapAndNodes heap = createHeap(c);
                ExpResult res = exp1(heap);
                sumTime1 += res.timeMs;
                sumSize1 += res.finalSize;
                sumTrees1 += res.finalNumTrees;
                sumCuts1 += res.cuts;
                sumLinks1 += res.links;
                sumRoots1 += res.finalNumRoots;
            }
            exp1Rows.add(new ExpTableRow(
                c,
                sumTime1/(double)numRuns,
                sumSize1/(double)numRuns,
                sumTrees1/(double)numRuns,
                sumCuts1/(double)numRuns,
                sumLinks1/(double)numRuns,
                sumRoots1/(double)numRuns
            ));
        }
        printTable("Experiment 1 Results", exp1Rows);
        // Run exp2 for all c
        for (int c : cLst) {
            long sumTime2 = 0;
            int sumSize2 = 0;
            int sumTrees2 = 0;
            int sumRoots2 = 0;
            int sumCuts2 = 0;
            int sumLinks2 = 0;
            for(int i = 0; i < numRuns; i++) {
                heapAndNodes heap = createHeap(c);
                ExpResult res = exp2(heap);
                sumTime2 += res.timeMs;
                sumSize2 += res.finalSize;
                sumTrees2 += res.finalNumTrees;
                sumCuts2 += res.cuts;
                sumLinks2 += res.links;
                sumRoots2 += res.finalNumRoots;
            }
            exp2Rows.add(new ExpTableRow(
                c,
                sumTime2/(double)numRuns,
                sumSize2/(double)numRuns,
                sumTrees2/(double)numRuns,
                sumCuts2/(double)numRuns,
                sumLinks2/(double)numRuns,
                sumRoots2/(double)numRuns
            ));
        }
        printTable("Experiment 2 Results", exp2Rows);
    }
    public static class ExpResult {
        long timeMs;
        int finalSize;
        int finalNumTrees;
        int cuts;
        int links;
        int finalNumRoots;
        ExpResult(long timeMs, int finalSize, int finalNumTrees, int cuts, int links, int finalNumRoots) {
            this.timeMs = timeMs;
            this.finalSize = finalSize;
            this.finalNumTrees = finalNumTrees;
            this.cuts = cuts;
            this.links = links;
            this.finalNumRoots = finalNumRoots;
        }
    }

    private static ExpResult exp1(heapAndNodes heapAndNodes) {
        FibonacciHeap heap = heapAndNodes.heap;
        FibonacciHeap.HeapNode[] nodes = heapAndNodes.nodes;
        long startTime = System.currentTimeMillis();
        heap.deleteMin();
        int i =n-1;
        while(heap.size() > 46) {
            heap.delete(nodes[i]);
            i--;
        }
        long endTime = System.currentTimeMillis();
        return new ExpResult(
            endTime - startTime,
            heap.size(),
            heap.numTrees(),
            heap.totalCuts(),
            heap.totalLinks(),
            heap.numTrees()
        );
    }

    private static ExpResult exp2(heapAndNodes heapAndNodes) {
        FibonacciHeap heap = heapAndNodes.heap;
        FibonacciHeap.HeapNode[] nodes = heapAndNodes.nodes;
        long startTime = System.currentTimeMillis();
        heap.deleteMin();
        int i =n-1;
        while(i >= 46) {
            heap.decreaseKey(nodes[i], nodes[i].key);
            i--;
        }
        heap.deleteMin();
        long endTime = System.currentTimeMillis();
        return new ExpResult(
            endTime - startTime,
            heap.size(),
            heap.numTrees(),
            heap.totalCuts(),
            heap.totalLinks(),
            heap.numTrees()
        );
    }

    private static heapAndNodes createHeap(int c) {
        // int seed = 3215; // Fixed seed for reproducibility
        Random rand = new Random();
        FibonacciHeap heap = new FibonacciHeap(c);
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[n];
        List<Integer> keyList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            keyList.add(i);
        }
        Collections.shuffle(keyList, rand);
        for (int i = 0; i < n; i++) {
            // System.out.print(keyList.get(i)-1 + " ");
            FibonacciHeap.HeapNode node = heap.insert(keyList.get(i), "val"+keyList.get(i));
            nodes[keyList.get(i)-1] = node;
        }
        heapAndNodes heapAndNodes = new heapAndNodes(heap, nodes);
        return heapAndNodes;
    }
}