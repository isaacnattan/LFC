package testes;

import AFNParaAFD.AFNParaAFD;
import automato.Automato;

/**
 * @author Isaac_Nattan
 */
public class ExemploAFN {
    // No aplicativo o caracter * tera um significado especial:
    // representara a transicao epsilon
    public static void main(String [] args) {
        Automato meuAutomato = new Automato();
        // cria estados
        meuAutomato.setEstado("1");
        meuAutomato.setEstado("2");
        meuAutomato.setEstado("3");
        // definicao do estado inicial
        meuAutomato.setEstadoInicial("1");
        // definicao do estado final
        meuAutomato.setEstadoFinal("1");
        // definicao das transicoes
        meuAutomato.setTransicao("1", "2", "b");
        meuAutomato.setTransicao("1", "3", "*");
        meuAutomato.setTransicao("2", "2", "a");
        meuAutomato.setTransicao("2", "3", "a");
        meuAutomato.setTransicao("2", "3", "b");
        meuAutomato.setTransicao("3", "1", "a");
        // testar conversao ExemploAFN para AFD
        AFNParaAFD conversao = new AFNParaAFD(meuAutomato);
        Automato AFD = conversao.getAFD();
    }
}

