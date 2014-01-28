
package testes;

import automato.Automato;
import conversoes.ConversaoAFNParaAFD;
import conversoes.ConversaoERParaAFN;
import reconhecedorCadeias.ReconhecedorCadeia;

public class ER {
    public static void main(String[] args) {
        ConversaoERParaAFN er = new ConversaoERParaAFN("(a.b+a)*");
        // (a.b+a)*
        Automato autA = er.a("a");
        Automato autB = er.a("b");
        Automato autConc = er.concatenacao(autA, autB);
        Automato autA2 = er.a("a");
        Automato autUniao = er.uniao(autConc, autA2);
        Automato autFinal = er.fechoKleene(autUniao);
        ConversaoAFNParaAFD conversao = new ConversaoAFNParaAFD(autFinal);
        // adicione a cadeia a ser verificada no segundo parametro
        new ReconhecedorCadeia(conversao.getAFD(), "aaaa");
    }
}
