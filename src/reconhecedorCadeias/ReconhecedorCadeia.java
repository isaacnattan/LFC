package reconhecedorCadeias;

import automato.Automato;
import automato.Transicao;
import java.util.ArrayList;
import util.ChaveComposta;

/**
 * @author Isaac_Nattan
 */
public final class ReconhecedorCadeia {

    public ReconhecedorCadeia(Automato automato, String cadeia) {
        imprimeStatus(processaCadeia(automato, cadeia));
    }

    private boolean processaCadeia(Automato automato, String cadeia) {
        ArrayList<Transicao> transicaoSeguinte;
        // verifica se os simbolos da cadeia pertence aos do automato
        ArrayList<String> simbolos = automato.getSimbolosTrasicoes();
        for(int i=0; i<cadeia.length(); i++){   
            if(!simbolos.contains(cadeia.substring(i, i+1))){
                return false;       // se nao existe o simbolo nao aceita
            }
        }
        String estadoAtual = automato.getEstadoInicial().getID();
        if (cadeia.equals("")) {      // verifica se a cadeia vazia eh aceita
            if (automato.getEstadosFinais().containsKey(automato.getEstadoInicial().getID())) {
                // se o estado inicial eh tambem final, aceita a cadeia vazia
                return true;
            }
        }
        for (int i = 0; i < cadeia.length(); i++) {
            transicaoSeguinte = automato.getTransicao(new ChaveComposta(
                    estadoAtual, cadeia.substring(i, i + 1)));
            estadoAtual = transicaoSeguinte.get(0).getDestino().getID();
            if (automato.ehEstadoFinal(estadoAtual)
                    && i == cadeia.length() - 1) {
                return true;
            }
        }
        return false;
    }

    public void imprimeStatus(boolean flag) {
        if (flag == true) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }
}
