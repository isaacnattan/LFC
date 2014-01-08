package testes;

import AFNParaAFD.AFNParaAFD;
import automato.Automato;

/**
 * @author Isaac_Nattan
 */
public class ExemploAFN2 {

    public static void main(String[] args) {
        Automato AFN = new Automato();
        // cria estados
        AFN.setEstado("1");
        AFN.setEstado("2");
        AFN.setEstado("3");
        AFN.setEstado("4");
        // definicao do estado inicial
        AFN.setEstadoInicial("1");
        // definicao do estado final
        AFN.setEstadoFinal("4");
        // definicao das transicoes
        AFN.setTransicao("1", "1", "a");
        AFN.setTransicao("1", "1", "b");
        AFN.setTransicao("1", "2", "b");
        AFN.setTransicao("2", "3", "a");
        AFN.setTransicao("2", "3", "*");
        AFN.setTransicao("3", "4", "b");
        AFN.setTransicao("4", "4", "a");
        AFN.setTransicao("4", "4", "b");
        // testar conversao ExemploAFN para AFD
        AFNParaAFD conversao = new AFNParaAFD(AFN);
        Automato AFD = conversao.getAFD();
    }
}
