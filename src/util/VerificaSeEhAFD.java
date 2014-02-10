package util;

import automato.Automato;
import automato.Estado;
import automato.Transicao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Isaac_Natan
 */
public class VerificaSeEhAFD {

    public boolean ehAFD(Automato af) {
        if (verificaSeHaUmaTransicaoParaCadaSimboloDoAlfabeto(af)) {
            if (!verificaSeNaoContemTransicoesEpsilon(af)) {
                return true;
            }
        }
        return false;
    }

    private boolean verificaSeHaUmaTransicaoParaCadaSimboloDoAlfabeto(Automato af) {
        ArrayList<String> simbolos = af.getSimbolosTrasicoes();
        Iterator<Entry<String, Estado>> iteratorEstados = af.getEstados().entrySet().iterator();
        HashMap<ChaveComposta, ArrayList<Transicao>> transicoes = af.getTransicoes();
        while (iteratorEstados.hasNext()) {
            Estado estadoCorrente = iteratorEstados.next().getValue();
            for (int i = 0; i < simbolos.size(); i++) {
                if (!transicoes.containsKey(new ChaveComposta(estadoCorrente.getID(), simbolos.get(i)))) {
                    // se algum estado nao contem transicao com um simbolo qualquer do alfabeto, falso
                    return false;
                }
            }
        }
        return true;
    }

    private boolean verificaSeNaoContemTransicoesEpsilon(Automato af) {
        ArrayList<String> simbolos = af.getSimbolosTrasicoes();
        for (int i = 0; i < simbolos.size(); i++) {
            if(simbolos.get(i).equals("*")){
                return true;
            }
        }
        return false;
    }
}
