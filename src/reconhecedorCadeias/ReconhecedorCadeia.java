package reconhecedorCadeias;

import automato.Automato;
import automato.Transicao;
import java.util.ArrayList;
import util.ChaveComposta;

/**
 * @author Isaac_Nattan
 */
public final class ReconhecedorCadeia {

    private Automato automato;
    private final String cadeia;
    private boolean aceitacao;

    public ReconhecedorCadeia(Automato automato, String cadeia) {
        this.automato = automato;
        this.cadeia = cadeia;
        aceitacao = processaCadeia();
        ehAceito();
    }

    private boolean processaCadeia() {
        ArrayList<Transicao> transicaoSeguinte;
        String estadoAtual = automato.getEstadoInicial().getID();
        if(cadeia.equals("")){      // verifica se a cadeia vazia eh aceita
            if(automato.getEstadosFinais().containsKey(automato.getEstadoInicial().getID())){
                // se o estado inicial eh tambem final, aceita a cadeia vazia
                return true;
            }
        }
        for(int i=0; i<cadeia.length(); i++){
             transicaoSeguinte = automato.getTransicao(new ChaveComposta(
                    estadoAtual, cadeia.substring(i, i+1)));
             estadoAtual = transicaoSeguinte.get(0).getDestino().getID();
             if(automato.ehEstadoFinal(estadoAtual) 
                     && i == cadeia.length() - 1){
                 return true;
             }
        }
        return false;
    }

    public boolean ehAceito() {
        if(aceitacao == true) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
        return aceitacao;
    }
}
