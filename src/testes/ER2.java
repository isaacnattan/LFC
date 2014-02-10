package testes;

import automato.Automato;
import conversoes.ConversaoAFNParaAFD;
import conversoes.ConversaoERParaAFN;
import reconhecedorCadeias.ReconhecedorCadeia;

/**
 * @author Isaac_Nattan
 */
public class ER2 {
     public static void main(String[] args) {
        ConversaoERParaAFN er = new ConversaoERParaAFN("(0+1)*.0.0.1.(0+1)*");
        // (0+1)*.0.0.1.(0+1)*
        /*Automato autUniao = er.uniao(er.a("0"), er.a("1"));
        Automato autFech = er.fechoKleene(autUniao);
        Automato autConc = er.concatenacao(er.a("0"), er.a("0"));
        Automato autConc2 = er.concatenacao(autConc, er.a("1"));
        Automato autConc3 = er.concatenacao(autFech, autConc2);
        Automato autUniao2 = er.uniao(er.a("0"), er.a("1"));
        Automato autFech2 = er.fechoKleene(autUniao2);
        Automato autFinal = er.concatenacao(autConc3, autFech2);
        ConversaoAFNParaAFD conversao = new ConversaoAFNParaAFD(autFinal);
        // adicione a cadeia a ser verificada no segundo parametro
        new ReconhecedorCadeia(conversao.getAFD(), "");*/
    }
}
