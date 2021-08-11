import java.util.Vector;

public class Node {
    private int numberNodeInKey; //Atributo que guarda a quantidade de chaves no nó
    private Vector<Integer> keyVector; //vetor das chaves
    private Vector<Node> sonVector;//vetor dos filhos
    private boolean itsLeaf;//Atributo que indica se a nó eh folha ou nao
    private int X;//Atributo que guarda a posicao X onde o Nó deve aparecer na tela
    private int Y;//Atributo que guarda a posicao Y onde o Nó deve aparecer na tela
    private int widthSon;
    final int HEIGHT_DIFF = 30;
    final int BROTHERS_DIFF = 5;

    public Node(int n) {
        this.keyVector = new Vector<Integer>(n - 1);
        for (int i = 0; i < n - 1; i++) {
            this.keyVector.add(null);
        }
        this.sonVector = new Vector<Node>(n);
        for (int i = 0; i < n; i++) {
            this.sonVector.add(null);
        }
        this.itsLeaf = true;
        this.numberNodeInKey = 0;
    }

    public Vector<Integer> getKeyVector() {
        return keyVector;
    }

    public void setKeyVector(Vector<Integer> keyVector) {
        this.keyVector = keyVector;
    }

    public Vector<Node> getSonVector() {
        return sonVector;
    }

    public void setSonVector(Vector<Node> sonVector) {
        this.sonVector = sonVector;
    }

    public boolean isItsLeaf() {
        return itsLeaf;
    }

    public void setItsLeaf(boolean itsLeaf) {
        this.itsLeaf = itsLeaf;
    }

    public int getNumberNodeInKey() {
        return numberNodeInKey;
    }

    public void setNumberNodeInKey(int numberNodeInKey) {
        this.numberNodeInKey = numberNodeInKey;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int computeSize() {
        return numberNodeInKey * 28 + 12;
    }


    public void updateCoordenates(Node parent, int x) {
        if (parent == null) {
            if (x == 0) {
                UpdateLFilho();
            }
            Y = 0;
        } else {
            Y = parent.getY() + HEIGHT_DIFF;
        }
        if (!itsLeaf) {
            X = (widthSon / 2) + x;
            int xAcumuladoLocal = x;
            for (int i = 0; i < numberNodeInKey + 1; i++) {
                sonVector.get(i).updateCoordenates(this, xAcumuladoLocal);
                xAcumuladoLocal += sonVector.get(i).widthSon + BROTHERS_DIFF;
            }
        } else {
            X = x;
        }
    }

    public int UpdateLFilho() {
        widthSon = 0;
        if (!itsLeaf) {
            for (int i = 0; i < numberNodeInKey + 1; i++) {
                widthSon += sonVector.get(i).UpdateLFilho();
            }
        } else {
            widthSon = computeSize() + BROTHERS_DIFF;
        }
        return widthSon;
    }
}
