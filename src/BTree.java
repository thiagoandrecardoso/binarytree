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

    //Método de Remoção de uma determinada chave da arvoreB
    public void Remove(int k) {
        //verifica se a chave a ser removida existe
        if (BuscaChave(this.raiz, k) != null) {
            //N é o nó onde se encontra k
            Node N = BuscaChave(this.raiz, k);
            int i = 1;

            //adquire a posição correta da chave em N
            while (N.getKeyVector().get(i - 1) < k) {
                i++;
            }

            //se N for folha, remove ela e deve se balancear N
            if (N.isItsLeaf()) {
                for (int j = i + 1; j <= N.getNumberNodeInKey(); j++) {
                    N.getKeyVector().set(j - 2, N.getKeyVector().get(j - 1));//desloca chaves quando tem mais de uma
                }
                N.setNumberNodeInKey(N.getNumberNodeInKey() - 1);
                if (N != this.raiz) {
                    Balanceia_Folha(N);//Balanceia N
                }
            } else {//caso contrário(N nao é folha), substitui a chave por seu antecessor e balanceia a folha onde se encontrava o ancecessor
                Node S = Antecessor(this.raiz, k);//S eh onde se encontra o antecessor de k
                int y = S.getKeyVector().get(S.getNumberNodeInKey() - 1);//y é o antecessor de k
                S.setNumberNodeInKey(S.getNumberNodeInKey() - 1);
                N.getKeyVector().set(i - 1, y);//substitui a chave por y
                Balanceia_Folha(S);//balanceia S
            }
            nElementos--;//decrementa o numero de elementos na arvoreB
        }
    }

    //Metodo que retorna o nó onde a chave antecessora de K se encontra
    //Parâmetros: N - Nó onde começa a busca, k - chave a ser buscada
    private Node Antecessor(Node N, int k) {
        int i = 1;
        while (i <= N.getNumberNodeInKey() && N.getKeyVector().get(i - 1) < k) {
            i++;
        }
        if (N.isItsLeaf()) {
            return N;
        } else {
            return Antecessor(N.getSonVector().get(i - 1), k);
        }
    }

    //Métode de Balancear um nó folha
    //Parâmetros: F - nó Folha a ser balanceada
    private void Balanceia_Folha(Node F) {
        //verifica se F está cheio
        if (F.getNumberNodeInKey() < Math.floor((ordem - 1) / 2)) {
            Node P = getPai(raiz, F);//P é o pai de F
            int j = 1;

            //adquire a posição de F em P
            while (P.getSonVector().get(j - 1) != F) {
                j++;
            }

            //verifica se o irmão à esquerda de F não tem chaves para emprestar
            if (j == 1 || (P.getSonVector().get(j - 2)).getNumberNodeInKey() == Math.floor((ordem - 1) / 2)) {
                //verifica se o irmão à direita de F não tem chaves para emprestar
                if (j == P.getNumberNodeInKey() + 1 || (P.getSonVector().get(j).getNumberNodeInKey() == Math.floor((ordem - 1) / 2))) {
                    Diminui_Altura(F); //nenhum irmão tem chaves para emprestar eh necessario diminuir a altura
                } else {//caso contrario (tem chaves para emprestar) executa Balanceia_Dir_Esq
                    Balanceia_Dir_Esq(P, j - 1, P.getSonVector().get(j), F);
                }
            } else {//caso contrario (tem chaves para emprestar) executa Balanceia_Esq_Dir
                Balanceia_Esq_Dir(P, j - 2, P.getSonVector().get(j - 2), F);
            }
        }
    }

    //Mótodo de Balancear da esquerda para a direita
    //Parâmetros: P - Nó pai, e - indica que Esq é o e-ésimo filho de P, Esq - Nó da esquerda, Dir - Nó da direita
    private void Balanceia_Esq_Dir(Node P, int e, Node Esq, Node Dir) {
        //Desloca as chave de Dir uma posicao para a direita
        for (int i = 0; i < Dir.getNumberNodeInKey(); i++) {
            Dir.getKeyVector().set(i + 1, Dir.getKeyVector().get(i));
        }

        //Se Dir nao for folha descola seu filhos uma posicao para a direita
        if (!Dir.isItsLeaf()) {//nao foi testado, mas teoricamente funciona
            for (int i = 0; i > Dir.getNumberNodeInKey(); i++) {
                Dir.getSonVector().set(i + 1, Dir.getSonVector().get(i));
            }
        }
        Dir.setNumberNodeInKey(Dir.getNumberNodeInKey() + 1);//Incrementa a quantidades de chaves em Dir
        Dir.getKeyVector().set(0, P.getKeyVector().get(e));//"desce" uma chave de P para Dir
        P.getKeyVector().set(e, Esq.getKeyVector().get(Esq.getNumberNodeInKey() - 1));//"sobe" uma chave de Esq para P
        Dir.getSonVector().set(0, Esq.getSonVector().get(Esq.getNumberNodeInKey()));//Seta o ultimo filho de Esq como primeiro filho de Dir
        Esq.setNumberNodeInKey(Esq.getNumberNodeInKey() - 1);//Decrementa a quantidade de chaves em Esq

    }


    //Método de Balancear da direita para a esquerda
    //Parâmetros: P - Nó pai, e - indica que Dir é o e-ésimo filho de P, Dir - Nó da direita, Esq - Nó da esquerda
    private void Balanceia_Dir_Esq(Node P, int e, Node Dir, Node Esq) {

        Esq.setNumberNodeInKey(Esq.getNumberNodeInKey() + 1);//Incrementa a quantidade de chaves em Esq
        Esq.getKeyVector().set(Esq.getNumberNodeInKey() - 1, P.getKeyVector().get(e));//"desce" uma chave de P para Esq
        P.getKeyVector().set(e, Dir.getKeyVector().get(0));//"sobe" uma chave de Dir para P
        Esq.getSonVector().set(Esq.getNumberNodeInKey(), Dir.getSonVector().get(0));//Seta o primeiro filho de Dir como último filho de Esq

        //descola as chaves de Dir uma posição para a esquerda
        for (int j = 1; j < Dir.getNumberNodeInKey(); j++) {
            Dir.getKeyVector().set(j - 1, Dir.getKeyVector().get(j));
        }

        //se Dir nao for folha, desloca todos os filhos de Dir uma posicao a esquerda
        if (!Dir.isItsLeaf()) {//nao foi testado, mas teoricamente funciona
            for (int i = 1; i < Dir.getNumberNodeInKey()+1 ; i++) {
                Dir.getSonVector().set(i - 1, Dir.getSonVector().get(i));
            }
        }

        //descrementa a quantidade de chaves de Dir
        Dir.setNumberNodeInKey(Dir.getNumberNodeInKey() - 1);

    }

    //Método para diminuir a altura
    //Parâmetros: X - nó onde vai ser diminuido a altura
    private void Diminui_Altura(Node X) {
        int j;
        Node P = new Node(ordem);

        //verifica se X é a raiz
        if (X == this.raiz) {
            //verifica se X está vazio
            if (X.getNumberNodeInKey() == 0) {
                this.raiz = X.getSonVector().get(0);//o filho o de x passa a ser raiz
                X.getSonVector().set(0, null); // desaloca o filho de x
            }
        } else {//caso contrario(X nao é raiz)
            int t = (int) Math.floor((ordem - 1) / 2);
            //verifica se o numero de chaves de X é menor que o permitido
            if (X.getNumberNodeInKey() < t) {
                P = getPai(raiz, X);//P é pai de X
                j = 1;

                //adquire a posicao correta do filho X em P
                while (P.getSonVector().get(j - 1) != X) {
                    j++;
                }

                //junta os nós
                if (j > 1) {
                    Juncao_No(getPai(raiz, X), j - 1);
                } else {
                    Juncao_No(getPai(raiz, X), j);
                }
                Diminui_Altura(getPai(raiz, X));
            }
        }
    }

    //Método para junção do nó
    //Parâmetros: X - No pai, i - posicao do filho de X onde vai ser juntado
    private void Juncao_No(Node X, int i) {
        Node Y = X.getSonVector().get(i - 1); //cria Y
        Node Z = X.getSonVector().get(i);//cria Z

        int k = Y.getNumberNodeInKey();
        Y.getKeyVector().set(k, X.getKeyVector().get(i - 1));//"desce" uma chave de X para X

        //descola as de chaves de Z para Y
        for (int j = 1; j <= Z.getNumberNodeInKey(); j++) {
            Y.getKeyVector().set(j + k, Z.getKeyVector().get(j - 1));
        }

        //se Z nao for folha, descola seus filhos tbm
        if (!Z.isItsLeaf()) {
            for (int j = 1; j <= Z.getNumberNodeInKey(); j++) {
                Y.getSonVector().set(j + k, Z.getSonVector().get(j - 1));
            }
        }

        //seta a quantidades de chaves em Y
        Y.setNumberNodeInKey(Y.getNumberNodeInKey() + Z.getNumberNodeInKey() + 1);

        X.getSonVector().set(i, null);//desaloca o Z setando o filho de X que apontava pra Z pra null

        //descola os filhos e as chaves de X uma para a esquera, para "fechar o buraco"
        for (int j = i; j <= X.getNumberNodeInKey() - 1; j++) {//ainda nao
            X.getKeyVector().set(j - 1, X.getKeyVector().get(j));
            X.getSonVector().set(j, X.getSonVector().get(j + 1));
        }

        //decrementa a quantidade de chaves em X
        X.setNumberNodeInKey(X.getNumberNodeInKey() - 1);
    }

    //Metodo que retorna o nó pai de N
    //Parâmetros: T - Nó onde começa a busca, N - nó que deve se buscar o pai
    private Node getPai(Node T, Node N) {
        if (this.raiz == N) {
            return null;
        }
        for (int j = 0; j <= T.getNumberNodeInKey(); j++) {
            if (T.getSonVector().get(j) == N) {
                return T;
            }
            if (!T.getSonVector().get(j).isItsLeaf()) {
                Node X = getPai(T.getSonVector().get(j), N);
                if (X != null) {
                    return X;
                }
            }
        }
        return null;
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
