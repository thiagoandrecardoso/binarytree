public class BTree {

    private Node root;

    /**
     *
     * @param currentNode nó atual que será repassado para a árvore
     * @param value valor a ser inserido no nó atual
     * @return O Nó que inicializará a raiz
     * O método insere por recursividade um nó
     * Caso o Nó repassado seja nulo, ele cria um nó novo já com o valor repassado e devolve.
     * Caso contrário, ele verifica se o valor é manor ou maior que o do nó atual, e assim
     * chamando novamente o método mas repassando o nó da direita ou da esquerda.
     * Se o valor fir igual ele apenas retorna o mesmo nó repassado!
     */
    public Node insertRecursive(Node currentNode, int value) {
        if (currentNode == null) {
            return new Node(value);
        }

        if (value < currentNode.getValue()) {
            currentNode.setLeft(insertRecursive(currentNode.getLeft(), value));
        } else if (value > currentNode.getValue()) {
            currentNode.setRight(insertRecursive(currentNode.getRight(), value));
        } else {
            return currentNode;
        }

        return currentNode;
    }

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    /**
     * 
     * @param temp Nó atual
     * @param value Valor a pesquisado nos nós
     * @return se o valor foi encontrado ou não
     * Retorna falso se o Nó repassado não existir (for nulo)
     * Caso o valor do nó atual seja igual o repassado, já retorna que encontrou (verdadeiro)
     * Se nenhuma das condições aceima forem aceitas, acontece a recursividade, mas agora repassando o nó da
     * esquerda ou da direita, dependendo do valor repassado (maior ou menor)
     */
    public boolean hasNodeRecursive(Node temp, int value) {
        if (temp == null) return false;
        if (temp.getValue() == value) return true;

        return value < temp.getValue() ? hasNodeRecursive(temp.getLeft(), value) :
                hasNodeRecursive(temp.getRight(), value);
    }

    public boolean hasNode(int value) {
        return hasNodeRecursive(root, value);
    }


}
