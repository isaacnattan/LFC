package testes;

import conversoes.ConversaoAFNParaAFD;
import automato.Automato;
import reconhecedorCadeias.ReconhecedorCadeia;

/**
 * @author Isaac_Nattan
 */
public class ExemploAFN3 {
    // automato equivalente a expressao regular (abUa)*

    public static void main(String[] args) {
        Automato AFN = new Automato();
        // cria estados
        AFN.setEstado("1");
        AFN.setEstado("2");
        AFN.setEstado("3");
        AFN.setEstado("4");
        AFN.setEstado("5");
        AFN.setEstado("6");
        AFN.setEstado("7");
        AFN.setEstado("8");
        // definicao do estado inicial
        AFN.setEstadoInicial("1");
        // definicao do estado final
        AFN.setEstadoFinal("1");
        AFN.setEstadoFinal("4");
        AFN.setEstadoFinal("8");
        // definicao das transicoes
        AFN.setTransicao("1", "2", "*");
        AFN.setTransicao("2", "5", "*");
        AFN.setTransicao("2", "3", "*");
        AFN.setTransicao("3", "4", "a");
        AFN.setTransicao("4", "2", "*");
        AFN.setTransicao("5", "6", "a");
        AFN.setTransicao("6", "7", "*");
        AFN.setTransicao("7", "8", "b");
        AFN.setTransicao("8", "2", "*");
        // testar conversao ExemploAFN para AFD
        ConversaoAFNParaAFD conversao = new ConversaoAFNParaAFD(AFN);
        new ReconhecedorCadeia(conversao.getAFD(), "b");
    }
}
