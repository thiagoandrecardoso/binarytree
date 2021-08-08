public class Node {
    private Node left;
    private Node right;
    private int value;

    /*
     * Esta classe representa os nós que termos em nossa árvore binária
     * Ela contém dois objetos de si mesma, para representar os lados esquerdo e direito,
     * gerando assim uma árvore de nós, contendo uma raiz que pode-se ter até dois nós.
     * Outro atributo que temos é o de valor.
     * Seguiremos a regra de o valor menor, irá para o lado esquerdo enquanto o maior para o direito.
     */

    public Node(int value) {
        this.value = value;
        left = null;
        right = null;
    }
    
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
