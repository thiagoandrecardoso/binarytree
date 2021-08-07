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
        System.out.println(bTree.hasNode(7));

    }
}
