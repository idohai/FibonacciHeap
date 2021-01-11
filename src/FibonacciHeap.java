/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
    public static final double GOLDEN = ( 1 + Math.sqrt(5))/2;
    private static int cutCnt = 0;
    private static int linkCnt = 0;
    HeapNode first;
    HeapNode min;
    int size;
    int numOfRoots;
    int mrkedCnt = 0;

    public FibonacciHeap() {
        this.size = 0;
        this.numOfRoots = 0;
    }
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    * 
    * Time complexity: O(1)
    */
    public boolean isEmpty()
    {
    	return (this.min == null);
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created.
    *
    * Time complexity: O(1) 
    */
    public HeapNode insert(int key)
    {    
        this.size += 1;
        this.numOfRoots += 1;
        HeapNode node = new HeapNode(key);
        if (this.isEmpty()) {
            this.first = node;
            this.min = node;
            node.next = node;
            node.prev = node;
        }
        else {
            this.first.getPrev().setNext(node);
            node.setPrev(first.getPrev());
            first.setPrev(node);
            node.setNext(first);
            this.first = node;
            if (key < min.getKey()) { 
                this.min = node;
            }
        }
    	return node; // should be replaced by student code
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    * Amortized complexity: O(log(n))
    * Worst case: O(n)
    */
    public void deleteMin()
    {
        if (this.isEmpty()) {
            return;
        }
        this.size -= 1;
        this.numOfRoots += this.min.getRank() - 1;
        if (this.min.getNext() == min) { // if this.min is the only root node in the tree
            if (min.getChild() != null) {
                this.first = min.getChild();
            }
            else {
                this.min = null;
                this.first = null;
                return;  
            }
        }
        else { // min is not the only root node in the heap
            if (this.min.getChild() != null) { // if this.min have children
                if (this.min == this.first) {
                    this.first = this.min.getChild();
                }
                this.min.getNext().setPrev(this.min.getChild().getPrev());
                this.min.getChild().getPrev().setNext(this.min.getNext());

                this.min.getPrev().setNext(this.min.getChild());
                this.min.getChild().setPrev(this.min.getPrev());

            }
            else { // this.min have no children
                if (this.min == this.first) {
                    this.first = this.min.getNext();
                }
                this.min.getPrev().setNext(this.min.getNext());
                this.min.getNext().setPrev(this.min.getPrev());
            }
        }

        HeapNode node = this.min.getChild(); 
        for (int i=0;i<this.min.getRank(); i++) { // for loop to disconnect deleted node children
            node.setParent(null);
            if (node.isMarked()) {
                node.setMark(false);
                this.mrkedCnt--;
            }
            node = node.getNext();
        }
        this.min.detach();

        this.successiveLinking();
     	return;
    }

   /**
    * private void successiveLinking()
    *
    * link the heap roots. Is used in deleteMin() func to achieve amoritzed O(log(n)) cost.
    * after successiveLinking operation, we obtain a non-lazy binomial heap. 
    * 
    * Amoritezd complexity: O(log(n))
    * Worst case: O(n)
    */
    private void successiveLinking() 
    {
        int numOfBuckets = ((int)(Math.log(size) / Math.log(GOLDEN)) + 1); //TO UPDATE (maybe): mATH.log(phi) instead of math.log(2)
        HeapNode[] buckets = new HeapNode[numOfBuckets];
        HeapNode node = this.first;
        int i = 0;
        while (i < this.numOfRoots) {
            int pos = node.getRank();
            if (buckets[pos] == null) {
                buckets[pos] = node;
                i++;
                node = node.getNext();
            }
            else {
                if (node == buckets[pos]) {
                    node = node.getNext();
                    continue;
                }
                node = link(buckets[pos], node); //link() need to return the root node of linked tree
                buckets[pos] = null;
            }
        }
        //redefine roots of tree 
        this.numOfRoots = 0;
        HeapNode x = null;
        for (int j = 0; j< numOfBuckets; j++){
            if (buckets[j] != null) {
                numOfRoots++;
                if (x == null) {
                    x = buckets[j];
                    this.first = x;
                    this.min = x;
                }
                else {
                    if (buckets[j].getKey() < this.min.getKey()) { // find new minimum
                        this.min = buckets[j];
                    }
                    x.setNext(buckets[j]);
                    buckets[j].setPrev(x);
                    x = buckets[j];

                }
            }
        }
        this.first.setPrev(x);
        x.setNext(this.first);
    }

   /**
    * public HeapNode link()
    *
    * link two trees with rank (k-1) to create a new k rank tree.
    *
    * Time complexity: O(1)
    */
    private HeapNode link(HeapNode x, HeapNode y) 
    {
        FibonacciHeap.linkCnt ++;
        if (x.getKey() > y.getKey()) {
            HeapNode tmp = x;
            x = y;
            y = tmp;
        }
        y.setParent(x);
        y.getPrev().setNext(y.getNext());
        y.getNext().setPrev(y.getPrev());
        if (x.getChild() != null) {
            y.setNext(x.getChild());
            y.setPrev(x.getChild().getPrev());
            x.getChild().getPrev().setNext(y);
            x.getChild().setPrev(y);
        }
        else {
            y.setNext(y);
            y.setPrev(y);
        }

        x.setChild(y);
        x.setRank(x.getRank() + 1);
        return x;
    }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    * Time complexity: O(1)
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    * Time complexity: O(1)
    */
    public void meld (FibonacciHeap heap2)
    {
        if (heap2.isEmpty()) { 
            return;
        }
        else if (this.isEmpty()) {
            this.first = heap2.first; //may be compilation error
            this.min = heap2.min;
            this.size = heap2.size;
            this.numOfRoots = heap2.numOfRoots;
            this.mrkedCnt = heap2.mrkedCnt;
            return;
        }
        if (this.min.getKey() > heap2.min.getKey()) {
            this.min = heap2.min;
        }

        this.first.getPrev().setNext(heap2.first); //this.last.next = heap2.first
        heap2.first.setPrev(this.first.getPrev()); //heap2.first.prev = this.last

        heap2.first.getPrev().setNext(this.first); //heap2.last.next = this.first
        this.first.setPrev(heap2.first.getPrev()); //this.first.prev = heap2.last

        this.numOfRoots += heap2.numOfRoots;
        this.size += heap2.size;
        this.mrkedCnt += heap2.mrkedCnt;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *
    * Time complexity: O(1)   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    * Time complexity:
    * Worst case - O(n) (if only inserts without deleteMin).
    * after deleteMin() time complexity should be O(log(n)).
    */
    public int[] countersRep()
    {
    	int[] arr = new int[(int)(Math.log(size) / Math.log(GOLDEN)) + 1];
        HeapNode node = this.first;
        for (int i = 0; i<numOfRoots; i++) {
            arr[node.getRank()]++;
            node = node.getNext();
        }
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
        if (this.min == x) {
            this.deleteMin();
            return;
        }
        this.decreaseKey(x, (x.getKey() - this.min.getKey() + 1));
        this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    * 
    * Amortized complexity: O(1)
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
        x.decKey(delta); //O(1)
        if (x.getParent() != null && x.getKey() < x.getParent().getKey()) {
            HeapNode parent = x.getParent();
            cut(x); //remove node x from his tree and make it a root node. O(1)
            while (parent.isMarked()) { //cascading cuts 
                HeapNode p = parent.getParent();
                cut(parent);
                parent = p;
            }
            if (parent.getParent() != null) {
                parent.setMark(true);
                this.mrkedCnt++;
            }

        }
        if (x.getKey() < this.min.getKey()) {
            this.min = x;
        }
    }

   /**
    * public void cut(HeapNode x)
    *
    * cut heapNode from subtree to heap roots.
    * 
    * Time complexity: O(1)
    */
    private void cut(HeapNode x) 
    {    
        FibonacciHeap.cutCnt++;
        if (x.isMarked()) {
            x.setMark(false);
            mrkedCnt--;
        }
        HeapNode parent = x.getParent();
        parent.setRank(parent.getRank() - 1);

        if (parent.getChild() == x) { // if x is parent child
            if (x.getNext() != x) { // if x is NOT the only child then:
                parent.setChild(x.getNext());
                x.getNext().setPrev(x.getPrev());
                x.getPrev().setNext(x.getNext());
            }
            else { // if x is the only child:
                parent.setChild(null);
            }
        }
        else {
            x.getNext().setPrev(x.getPrev());
            x.getPrev().setNext(x.getNext());
        }

        x.setParent(null);
        this.first.getPrev().setNext(x);
        x.setPrev(this.first.getPrev());
        this.first.setPrev(x);
        x.setNext(this.first);
        this.first = x;
        this.numOfRoots++;
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    *
    * Time complexity: O(1)
    */
    public int potential() 
    {    
    	return this.numOfRoots + 2*this.mrkedCnt;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    *
    * Time complexity: O(1)
    */
    public static int totalLinks()
    {    
    	return FibonacciHeap.linkCnt; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    *
    * Time complexity: O(1)
    */
    public static int totalCuts()
    {    
    	return FibonacciHeap.cutCnt; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    public static int[] kMin(FibonacciHeap H, int k) //NOTE: deg(H) = log(n)
    {
        if (k <= 0 || H.isEmpty()) {
            return new int[0];
        }
        int[] kMinArray = new int[k];
        FibonacciHeap kHeap = new FibonacciHeap();
        HeapNode originNode = H.findMin();
        for (int i = 0; i < k; i++) {
            HeapNode firstNode = originNode;
            do {
                if (originNode == null) break;
                HeapNode xNode = kHeap.insert(originNode.getKey());
                xNode.setData(originNode);
                originNode = originNode.getNext();
            } while (originNode != firstNode);
            originNode = kHeap.findMin().getData().getChild();
            kMinArray[i] = kHeap.findMin().getKey();
            kHeap.deleteMin();
        }
        return kMinArray;
    }

   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

    	public int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;
        private HeapNode dataPointer; // used for kMin

      	public HeapNode(int key) {
    	    this.key = key;
            this.mark = false;
            this.rank = 0;
          }

      	public int getKey() {
    	    return this.key;
          }

        public void setKey(int k) {
            this.key = k;
        }

        public HeapNode getChild() {
            return this.child;
        }

        public void setChild(HeapNode x) {
            this.child = x;
        }

        public HeapNode getParent() {
            return this.parent;
        }

        public void setParent(HeapNode x) {
            this.parent = x;
        }

        public HeapNode getNext() {
            return this.next;
        }

        public void setNext(HeapNode x) {
            this.next = x;
        }

        public HeapNode getPrev() {
            return this.prev;
        }

        public void setPrev(HeapNode x) {
            this.prev = x;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public HeapNode getData() {
            return this.dataPointer;
        }
        public void setData(HeapNode node) {
            this.dataPointer = node;
        }

        public boolean isMarked() {
            return this.mark;
        }

        public void setMark(boolean val) {
            this.mark = val;
        }

        public void decKey(int delta) {
            this.key -= delta;
        }

        public void detach() {
            this.next = null;
            this.prev = null;
            this.child = null;
            this.parent = null;
        }
    }
}
