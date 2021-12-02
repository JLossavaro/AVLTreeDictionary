/** *************************************************
 * 2 *                                                  *
 * 3 * Júlio Cézar Lossavaro                            *
 * 4 * 2018.0743.029.4                                  *
 * 5 * Implementacão 3                                  *
 * 6 * Disciplina: Estruturas de Dados e Programação I  *
 * 7 * Professor: Ronaldo Fiorilo                       *
 * 8 *                                                  *
 * 9 ************************************************** */

import java.util.ArrayList;

public class Node {

    char language;
    int size;
    String value;
    ArrayList<String> synonyms;
    Node left;
    Node right;

    public Node(String value, char language) {
        this.language = language;
        this.value = value;
        this.synonyms = new ArrayList<String>();

    }

    //retorna a maior altura entre os seus filhos e soma mais um
    public void updateSize() {
        this.size = 1 + Math.max(getLeftSize(), getRightSize());
    }
//retorna a altura da esquerda de um nó

    public int getLeftSize() {
        return left == null ? -1 : left.size;
    }
//retorna a altura da direita de um nó

    public int getRightSize() {
        return right == null ? -1 : right.size;
    }

}
