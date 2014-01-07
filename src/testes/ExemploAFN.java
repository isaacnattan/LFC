package testes;

import AFNParaAFD.AFNParaAFD;
import automato.Automato;

/**
 * @author Isaac_Nattan
 */
public class ExemploAFN {

    public static void main(String[] args) {
        Automato AFN = new Automato();
        // cria estados
        AFN.setEstado("1");
        AFN.setEstado("2");
        AFN.setEstado("3");
        // definicao do estado inicial
        AFN.setEstadoInicial("1");
        // definicao do estado final
        AFN.setEstadoFinal("1");
        // definicao das transicoes
        AFN.setTransicao("1", "2", "b");
        AFN.setTransicao("1", "3", "*");
        AFN.setTransicao("2", "2", "a");
        AFN.setTransicao("2", "3", "a");
        AFN.setTransicao("2", "3", "b");
        AFN.setTransicao("3", "1", "a");
        // testar conversao ExemploAFN para AFD
        AFNParaAFD conversao = new AFNParaAFD(AFN);
        Automato AFD = conversao.getAFD();
    }
}
