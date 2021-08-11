public class BTree {

    //Atributos da Classe ArvoreB
    private Node raiz; //Atributo do Nó raiz;
    private int ordem; //Ordem da Arvore-B;
    private int nElementos; //Contador para a quantidade de elementos na arvore B;

    public BTree(int n) {
        this.raiz = new Node(n);
        this.ordem = n;
        nElementos = 0;
    }

    //Metodo de Inserção na ArvoreB
    //parametros: k - chave a ser inserida
    public void insere(int k) {
        //Verifica se a chave a ser inserida existe
        if (BuscaChave(raiz, k) == null) { //só insere se não houver, para evitar duplicação de chaves
            //verifica se a chave está vazia
            if (raiz.getNumberNodeInKey() == 0) {
                raiz.getKeyVector().set(0, k);//seta a chave na primeira posição da raiz
                raiz.setNumberNodeInKey(raiz.getNumberNodeInKey() + 1);
            } else { //caso nao estaja vazia
                Node r = raiz;
                //verifica se a raiz está cheia
                if (r.getNumberNodeInKey() == ordem - 1) {//há necessidade de dividir a raiz
                    Node s = new Node(ordem);
                    raiz = s;
                    s.setItsLeaf(false);
                    s.setNumberNodeInKey(0);
                    s.getSonVector().set(0, r);
                    divideNo(s, 0, r);//divide nó
                    insereNoNaoCheio(s, k);//depois de dividir a raiz começa inserindo apartir da raiz

                } else {//caso contrario começa inserindo apartir da raiz
                    insereNoNaoCheio(r, k);
                }
            }
            nElementos++;//incrementa o numero de elemantos na arvore
        }
    }

    //Método de busca de uma chave, retorna um nó onde a chave buscada se encontra
    //Parâmetros: X - nó por onde começar a busca, k - chave a ser buscada
    public Node BuscaChave(Node X, int k) {
        int i = 1;
        //procura ate nao estourar o tamanho e ate o valor nao ser maior q o procurado
        while ((i <= X.getNumberNodeInKey()) && (k > X.getKeyVector().get(i - 1))) {
            i++;
        }
        //se o tamanho nao tiver excedido e for o valor procurado..
        if ((i <= X.getNumberNodeInKey()) && (k == X.getKeyVector().get(i - 1))) {
            return X;
        }
        //se nao foi é igual, entao foi o tamanho q excedeu. ai procura no filho se nao for folha
        if (X.isItsLeaf()) { //se o no X for folha
            return null;
        } else {
            return (BuscaChave(X.getSonVector().get(i - 1), k));
        }
    }

    //Método de divisão de nó
    //Parâmetros: x - nó Pai, y - nó Filho e i - índice i que indica que y é o i-ésimo filho de x.
    public void divideNo(Node x, int i, Node y) {
        int t = (int) Math.floor((ordem - 1) / 2);
        //cria nó z
        Node z = new Node(ordem);
        z.setItsLeaf(y.isItsLeaf());
        z.setNumberNodeInKey(t);

        //passa as t ultimas chaves de y para z
        for (int j = 0; j < t; j++) {
            if ((ordem - 1) % 2 == 0) {
                z.getKeyVector().set(j, y.getKeyVector().get(j + t));
            } else {
                z.getKeyVector().set(j, y.getKeyVector().get(j + t + 1));
            }
            y.setNumberNodeInKey(y.getNumberNodeInKey() - 1);
        }

        //se y nao for folha, pasa os t+1 últimos flhos de y para z
        if (!y.isItsLeaf()) {
            for (int j = 0; j < t + 1; j++) {
                if ((ordem - 1) % 2 == 0) {
                    z.getSonVector().set(j, y.getSonVector().get(j + t));
                } else {
                    z.getSonVector().set(j, y.getSonVector().get(j + t + 1));
                }

            }
        }

        y.setNumberNodeInKey(t);//seta a nova quantidade de chaves de y

        //descola os filhos de x uma posição para a direita
        for (int j = x.getNumberNodeInKey(); j > i; j--) {
            x.getSonVector().set(j + 1, x.getSonVector().get(j));
        }

        x.getSonVector().set(i + 1, z);//seta z como filho de x na posição i+1

        //desloca as chaves de x uma posição para a direita, para podermos subir uma chave de y
        for (int j = x.getNumberNodeInKey(); j > i; j--) {
            x.getKeyVector().set(j, x.getKeyVector().get(j - 1));
        }

        //"sobe" uma chave de y para z
        if ((ordem - 1) % 2 == 0) {
            x.getKeyVector().set(i, y.getKeyVector().get(t - 1));
            y.setNumberNodeInKey(y.getNumberNodeInKey() - 1);

        } else {
            x.getKeyVector().set(i, y.getKeyVector().get(t));
        }

        //incrementa o numero de chaves de x
        x.setNumberNodeInKey(x.getNumberNodeInKey() + 1);

    }

    //Método para inserir uma chave em um nó não cheio
    //Paâmetros: x - nó a ser inserido, k - chave a ser inserida no nó x
    public void insereNoNaoCheio(Node x, int k) {
        int i = x.getNumberNodeInKey() - 1;
        //verifica se x é um nó folha
        if (x.isItsLeaf()) {
            //adquire a posição correta para ser inserido a chave
            while (i >= 0 && k < x.getKeyVector().get(i)) {
                x.getKeyVector().set(i + 1, x.getKeyVector().get(i));
                i--;
            }
            i++;
            x.getKeyVector().set(i, k);//insere a chave na posição i
            x.setNumberNodeInKey(x.getNumberNodeInKey() + 1);

        } else {//caso x não for folha
            //adquire a posição correta para ser inserido a chave
            while ((i >= 0 && k < x.getKeyVector().get(i))) {
                i--;
            }
            i++;
            //se o filho i de x estiver cheio, divide o mesmo
            if ((x.getSonVector().get(i)).getNumberNodeInKey() == ordem - 1) {
                divideNo(x, i, x.getSonVector().get(i));
                if (k > x.getKeyVector().get(i)) {
                    i++;
                }
            }
            //insere a chave no filho i de x
            insereNoNaoCheio(x.getSonVector().get(i), k);
        }

    }

    public Node getRaiz() {
        return raiz;
    }

    public void setRaiz(Node raiz) {
        this.raiz = raiz;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getnElementos() {
        return nElementos;
    }

    public void setnElementos(int nElementos) {
        this.nElementos = nElementos;
    }
}
