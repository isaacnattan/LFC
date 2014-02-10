package main;

import conversoes.ConversaoAFNParaAFD;
import conversoes.ConversaoERParaAFN;
import reconhecedorCadeias.ReconhecedorCadeia;
import util.VerificaSeEhAFD;

/**
 * @author Isaac_Nattan
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            // converte a expressao regular para um automato
            ConversaoERParaAFN conversaoER_AFN = new ConversaoERParaAFN(args[0]);
            // verifica se o automato gerado eh deterministico
            VerificaSeEhAFD verificacao = new VerificaSeEhAFD();
            ConversaoAFNParaAFD conversaoAFN_AFD = null;
            if (!verificacao.ehAFD(conversaoER_AFN.getAFN())) {
                // se o automato gerado nao eh deterministico, converte-o
                conversaoAFN_AFD = new ConversaoAFNParaAFD(conversaoER_AFN.getAFN());
            }
            // verifica se as cadeias passadas sao aceitas pelo AFD
            for (int i = 0; i < conversaoER_AFN.getCadeias().size(); i++) {
                if(conversaoAFN_AFD != null){
                    new ReconhecedorCadeia(conversaoAFN_AFD.getAFD(), conversaoER_AFN.getCadeias().get(i));
                } else {
                    new ReconhecedorCadeia(conversaoER_AFN.getAFN(), conversaoER_AFN.getCadeias().get(i));
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Arquivo de entrada não encontrado.");
        }
    }
}
