package automato;

import java.util.Stack;

/**
 * @author Isaac_Nattan
 */
public class AutomatoAPilha {

    private Stack<String> pilha;
    private Automato automato;

    public AutomatoAPilha() {
        automato = new Automato();
        pilha = new Stack<>();
    }

    public void escreveNaPilha(String simbolo) {
        pilha.push(simbolo);
    }

    public String leNaPilha() {
        return pilha.pop();
    }

    public Automato getAutomato() {
        return automato;
    }
}
