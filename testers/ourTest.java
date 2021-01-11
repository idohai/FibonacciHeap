// package Heap;
// By Elad Feldman 19/12/20

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;

public class ourTest {
    public static void main(String[] args)  {
        // FibonacciHeap h1 = new FibonacciHeap();
        

        // h1.insert(55);
        // h1.insert(33);

        // h1.insert(22);
        // System.out.println("size: " + h1.size);
        // h1.deleteMin(); // now 22 is the min
        // FibonacciHeap.HeapNode fst = h1.first;
        // FibonacciHeap.HeapNode node = h1.first;
        // System.out.println("************");
        // System.out.println("Tree Size: " + h1.size);
        // System.out.println("numofRoots: " + h1.numOfRoots);
        // System.out.print("Root List: ");
        // do{
        //     System.out.print(node.getKey());
        //     System.out.print(", ");
        //     node = node.getNext();
        // } while (node != fst);
        // System.out.println();

        // h1.insert(22);
        // h1.insert(5);
        // h1.insert(2);
        // h1.insert(3);
        // fst = h1.first;
        // node = h1.first;
        // System.out.println("************");
        // System.out.println("insert 22, 5 ,2 3");
        // System.out.println("Tree Size: " + h1.size);
        // System.out.println("numofRoots: " + h1.numOfRoots);
        // System.out.println("first: " + fst.getKey());
        // System.out.print("Root List: ");
        // do{
        //     System.out.print(node.getKey());
        //     System.out.print(", ");
        //     node = node.getNext();
        // } while (node != fst);
        // System.out.println("");
        // System.out.println(h1.first.getKey());
        // System.out.println(h1.first.getPrev().getKey());
        // h1.deleteMin();
        // System.out.println(h1.findMin().getKey());
        // System.out.println("size: " + h1.size);
        // System.out.println("numofRoots: " + h1.numOfRoots);
        // System.out.println("minimum: " + h1.min.getKey());
        // h1.insert(2);
        // System.out.println("size: " + h1.size);
        // System.out.println("numofRoots: " + h1.numOfRoots);
        // System.out.println("NOW h1 hold: 55, 33, 22, 5, 2, 3");
        // for (int i = 0; i <6; i++) {
        //     System.out.println("h1.size: " + h1.size());
        //     System.out.println("h1.minimum: " + h1.findMin().getKey());
        //     h1.deleteMin(); 
        // }
        // FibonacciHeap h2 = new FibonacciHeap();
        // int[] kMinArrayagain = FibonacciHeap.kMin(h2, 5);
        // System.out.println("kMinArrayagain: " + Arrays.toString(kMinArrayagain));
        // h2.insert(5);
        // h2.insert(26);
        // h2.insert(32);
        // h2.insert(86);
        // h2.insert(12);
        // h2.insert(2);
        // h2.insert(1);
        // h2.insert(99);
        // h2.insert(1023);
        // h2.insert(4);
        // h2.insert(34);
        // h2.insert(23);
        // h2.insert(11);
        // h2.insert(65);
        // h2.insert(43);
        // h2.insert(0);
        // h2.insert(102);
        // h2.deleteMin();
        // System.out.println("validate that h2 is a single tree. numOfRoots should be one.");
        // System.out.println("h2.numOfRoots: " + h2.numOfRoots);
        // System.out.println("h2.size: " + h2.size());
        // Scanner sc = new Scanner(System.in);
        // System.out.println("enter k for kMin algorithm: ");
        // int k = sc.nextInt();
        // int[] kMinArray = FibonacciHeap.kMin(h2, k);
        // System.out.print("kMinArray: ");
        // System.out.println(Arrays.toString(kMinArray));
        // int[] kMinArrayNon = FibonacciHeap.kMin(h2, -1);
        // System.out.println("kMinArrayNon: " + Arrays.toString(kMinArrayNon));
        // for (int i = 0; i < 16; i++) {
        //     System.out.println(h2.findMin().getKey());
        //     h2.deleteMin();
        // }


        System.out.println("********************MEASURMENTS********************");
        System.out.println("                    Sequence1");
        System.out.print("m = 2^10 = ");
        seq1((int)Math.pow(2,10));
        System.out.print("m = 2^11 = ");
        seq1((int)Math.pow(2,11));
        System.out.print("m = 2^12 = ");
        seq1((int)Math.pow(2,12));

        System.out.println("                    Sequence2");
        seq2(1000);
        seq2(2000);
        seq2(3000);
    }
    
    public static void seq1(int m) {
        System.out.println(m + ":");
        FibonacciHeap.HeapNode[] nodeArr = new FibonacciHeap.HeapNode[m+1];
        double startTime = System.nanoTime();
        FibonacciHeap heap = new FibonacciHeap();
        for (int i=m; i>= 0; i--) {
            nodeArr[i] = heap.insert(i);
        }
        heap.deleteMin();
        for (int i = 0; i < Math.log(m); i++) {
            int sum = 0;
            for (int k = 1; k <= i; k++) {
                sum += Math.pow(0.5, k);
            }
            heap.decreaseKey(nodeArr[m*sum + 2], m+1);
        }
        heap.decreaseKey(nodeArr[m-1], m+1);
        double endTime = System.nanoTime();
        double elapsed = (endTime - startTime)/1000000 ;
        System.out.println("runtime: " + elapsed + "ms");
        System.out.println("totalLinks: " + FibonacciHeap.totalLinks());
        System.out.println("totalCuts: " + FibonacciHeap.totalCuts());
        System.out.println("Potential: " + heap.potential());
        System.out.println("------------------");
    }

    public static void seq2(int m) {  
        double startTime = System.nanoTime(); 
        FibonacciHeap heap = new FibonacciHeap();
        for (int i=m; i>=0; i--) {
            heap.insert(i);
        }
        for (int i=0; i<(m/2); i++) {
            heap.deleteMin();
        }
        double endTime = System.nanoTime();
        double elapsed = (endTime - startTime)/1000000;
        System.out.println("m = " + m + " runtime: " + elapsed + "ms");


    }

}
