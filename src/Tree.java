/** *************************************************
 * 2 *                                                  *
 * 3 * Júlio Cézar Lossavaro                            *
 * 4 * 2018.0743.029.4                                  *
 * 5 * Implementacão 3                                  *
 * 6 * Disciplina: Estruturas de Dados e Programação I  *
 * 7 * Professor: Ronaldo Fiorilo                       *
 * 8 *                                                  *
 * 9 ************************************************** */

import java.util.Collections;

public class Tree {

    private Node root;

    public void recursiveAdd(String[] elementos) {
        root = recursiveAdd(this.root, elementos[2], elementos[1].charAt(0), elementos[4]);
        root = recursiveAdd(this.root, elementos[4], elementos[3].charAt(0), elementos[2]);
    }

    //Função que percorre a estrutura recursivamente e adiciona a palavra e seus sinonimos a estrutura
    private Node recursiveAdd(Node node, String element, char elem_lang, String sym) {
        //ao encontrar o nó vazio 
        if (node == null) {
            Node newNode = new Node(element, elem_lang);
            newNode.synonyms.add(sym);
            node = newNode;
            return node;

        } else {//ao encontrar a palavra na estrutura
            if (node.value.equals(element) && node.language == elem_lang) {
                //verifica sua lista de sinonimos e a atualiza, caso  necessário
                if (node.synonyms.contains(sym) == false) {
                    node.synonyms.add(sym);
                    Collections.sort(node.synonyms);
                }
                return node;
            } else if (node.value.compareTo(element) > 0) {
                node.left = recursiveAdd(node.left, element, elem_lang, sym);

            } else if (node.value.compareTo(element) <= 0) {
                node.right = recursiveAdd(node.right, element, elem_lang, sym);
            } else {
                return node;
            }
        }
        return updateBalance(node);
    }

    //recebe um nó e verifica a altura de suas sub arvores
    private Node updateBalance(Node node) {

        if (node == null) {
            return node;
        }
        //atualiza a altura do nó
        node.updateSize();
        //calcula o fator de balanceamento
        int balance = node.getLeftSize() - node.getRightSize();

        if (balance < -1) {
            if (node.right.getRightSize() > node.right.getLeftSize()) {
                node = rotateLeft(node);
            } else {
                node.right = (rotateRight(node.right));
                node = rotateLeft(node);
            }
        } else if (balance > 1) {

            if (node.left.getLeftSize() > node.left.getRightSize()) {

                node = rotateRight(node);

            } else {

                node.left = (rotateLeft(node.left));
                node = rotateRight(node);

            }
        }
        return node;
    }

    //rotação a esquerda
    private Node rotateLeft(Node node) {

        Node newRoot = node.right;
        Node newRight = newRoot.left;
        newRoot.left = node;
        node.right = newRight;
        node.updateSize();
        newRoot.updateSize();
        return newRoot;
    }

    //rotação a direita
    private Node rotateRight(Node node) {

        Node newRoot = node.left;
        Node newLeft = newRoot.right;
        newRoot.right = node;
        node.left = newLeft;
        node.updateSize();
        newRoot.updateSize();
        return newRoot;
    }

    //Procura maior valor a direita de um nó
    private Node getHighestNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    //Procura menor valor a esquerda de um nó
    private Node getLowestNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void remove(String[] elements) {
        if (elements.length == 2) {
            root = remove(this.root, elements[1]);
        } else {
            removeSyns(elements[1], elements[2]);
        }

    }

    //Função que percorre a estrutura recursivamente e remove uma palavra
    public Node remove(Node node, String value) {
        if (node == null) {
            return node;
        }
        if (node.value.compareTo(value) < 0) {
            node.right = remove(node.right, value);
        } else if (node.value.compareTo(value) > 0) {
            node.left = remove(node.left, value);
        } else {
            //caso encontrada
            node = removeRoot(node);
        }
        return updateBalance(node);
    }

    public Node removeRoot(Node node) {
        // Se o no nao tem filhos
        if (node.size == 0) {
            return null;
        }
        Node newNode;
        Node aux;
        // Busca um substituto na ramificacao de maior altura para nao desbalancear a arvore
        if (node.getLeftSize() > node.getRightSize()) {
            aux = getHighestNode(node.left);
            newNode = new Node(aux.value, aux.language);
            newNode.synonyms = aux.synonyms;
            //remove recursivamente o antigo nó e associa a nova arvore a esquerda do novo nó
            newNode.left = remove(node.left, newNode.value);
            newNode.right = node.right;
        } else {
            aux = getLowestNode(node.right);
            newNode = new Node(aux.value, aux.language);
            newNode.synonyms = aux.synonyms;
            //remove recursivamente o antigo nó e associa a nova arvore a esquerda do novo nó
            newNode.left = node.left;
            newNode.right = remove(node.right, newNode.value);
        }

        return newNode;
    }

    //Remove sinonimos, caso vazio, remove a palavra
    public void removeSyns(String wordOne, String wordTwo) {
        Node w = findWord(this.root, wordOne);
        Node w2 = findWord(this.root, wordTwo);

        if (w != null) {
            w.synonyms.remove(wordTwo);
            if (w.synonyms.isEmpty()) {
                root = remove(this.root, w.value);
            }
        }
        if (w2 != null) {
            w2.synonyms.remove(wordOne);
            if (w2.synonyms.isEmpty()) {
                root = remove(this.root, w2.value);//n ta conseguindo remover a raiz
            }
        }

    }

    //Procura uma palavra na estrutura
    public void find(String element) {
        Node n = findWord(this.root, element);

        if (n != null) {
            for (int i = 0; i < n.synonyms.size(); i++) {
                System.out.println(n.synonyms.get(i));
            }
        } else {
            System.out.println("hein?");
        }

    }

    //Procura uma palavra na estrutura
    public Node findWord(Node node, String element) {
        if (node == null) {
            return null;
        }
        if (element.compareTo(node.value) == 0) {
            return node;
        }
        if (node.value.compareTo(element) > 0) {
            return findWord(node.left, element);
        } else {
            return findWord(node.right, element);
        }
    }

    //Percorre a arvore de maneira decrescente(Percurso em ordem)
    public void inOrder(char lang) {
        inOrder(this.root, lang);
    }

    //Percorre a arvore de maneira decrescente(Percurso em ordem)
    private void inOrder(Node node, char lang) {
        if (node != null) {
            inOrder(node.left, lang);
            if (node.language == lang) {
                System.out.print(node.value + " : ");
                for (int i = 0; i < node.synonyms.size(); i++) {
                    if (i + 1 < node.synonyms.size()) {
                        System.out.print(node.synonyms.get(i) + ", ");

                    } else {
                        System.out.println(node.synonyms.get(i));
                    }
                }

            }
            inOrder(node.right, lang);
        }
    }

    //metodo responsavel por chamar e alimentar a função inOrderBetween
    public void inOrderBetwen(String[] letters) {
        char c[] = new char[2];
        c[0] = letters[2].charAt(0);
        c[1] = letters[3].charAt(0);
        inOrderBetwen(this.root, letters[1].charAt(0), c);
    }

    //Percorre a arvore de maneira decrescente(Percurso em ordem), e retorna somente os valores entre dois caracteres especificados
    private void inOrderBetwen(Node node, char lang, char[] letter) {
        if (node != null) {
            inOrderBetwen(node.left, lang, letter);
            if (node.language == lang && (letter[0] <= node.value.charAt(0) && letter[1] >= node.value.charAt(0))) {
                System.out.print(node.value + " : ");
                for (int i = 0; i < node.synonyms.size(); i++) {
                    if (i + 1 < node.synonyms.size()) {
                        System.out.print(node.synonyms.get(i) + ", ");
                    } else {
                        System.out.println(node.synonyms.get(i));
                    }
                }
            }
            inOrderBetwen(node.right, lang, letter);
        }
    }

}
