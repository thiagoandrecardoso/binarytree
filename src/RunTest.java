

public class RunTest {
    public static void main(String[] args) {
        BTree bTree = new BTree();

        bTree.insert(6);
        bTree.insert(4);
        bTree.insert(8);
        bTree.insert(3);
        bTree.insert(5);
        bTree.insert(7);
        bTree.insert(0);
        
        System.out.println("Check if node was added into our Tree");
        System.out.println(bTree.hasNode(7));
        
        System.out.println("Binary Tree Pre-order");
        bTree.printNodePreorder(bTree.getRoot());
        System.out.println();
        
        System.out.println("Binary Tree In-order");
        bTree.printNodeInorder(bTree.getRoot());
        System.out.println();
        
        System.out.println("Binary Tree Post-order");
        bTree.printNodePostorder(bTree.getRoot());
    }
}
