package testes;

import automato.Automato;
import conversoes.ConversaoAFNParaAFD;
import conversoes.ConversaoERParaAFN;
import reconhecedorCadeias.ReconhecedorCadeia;

/**
 * @author Isaac_Nattan
 */
public class ER1 {
    public static void main(String[] args) {
        ConversaoERParaAFN er = new ConversaoERParaAFN("(0+1)*");
        // (0+1)*
        /*Automato autA = er.a("0");
        Automato autB = er.a("1");
        Automato autConc = er.uniao(autA, autB);
        Automato autFinal = er.fechoKleene(autConc);
        ConversaoAFNParaAFD conversao = new ConversaoAFNParaAFD(autFinal);
        // adicione a cadeia a ser verificada no segundo parametro
        new ReconhecedorCadeia(conversao.getAFD(), "");*/
    }
}
