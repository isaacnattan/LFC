package conversoes;

import automato.Automato;
import automato.Estado;
import automato.Transicao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import reconhecedorCadeias.ReconhecedorCadeia;
import util.ChaveComposta;

/**
 * @author Isaac_Nattan
 */
public class ConversaoERParaAFN {

    private final String ER;
    private Automato AFN;
    private ArrayList andamentoTransformacao;

    public ConversaoERParaAFN(String ER) {
        this.ER = ER;
        lerER();
    }

    private Automato uniao(Automato autEsq, Automato autDir) {
        Automato autTemp = new Automato();
        ArrayList<String> simbolos = autEsq.getSimbolosTrasicoes();
        simbolos.addAll(autDir.getSimbolosTrasicoes());
        // adiciona novo estado    
        String idNovoEstadoInicial = getNextSimbol();
        autTemp.setEstado(idNovoEstadoInicial);
        // seta o novo estado como inicial
        autTemp.setEstadoInicial(idNovoEstadoInicial);
        // Adiciona os estados do automato da esquerda
        Iterator<Entry<String, Estado>> iteratorAutEsq = autEsq.getEstados().entrySet().iterator();
        while (iteratorAutEsq.hasNext()) {
            String estadoEsq = iteratorAutEsq.next().getKey();
            autTemp.setEstado(estadoEsq);
        }
        // Adiciona estados do automato da direita
        Iterator<Entry<String, Estado>> iteratorAutDir = autDir.getEstados().entrySet().iterator();
        while (iteratorAutDir.hasNext()) {
            String estadoDir = iteratorAutDir.next().getKey();
            autTemp.setEstado(estadoDir);
        }
        // seta estados finais do automato da esquerda como finais do novo automato
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAutEsq = autEsq.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinaisAutEsq.hasNext()) {
            String estadoFinalAutEsq = iteratorEstadosFinaisAutEsq.next().getKey();
            autTemp.setEstadoFinal(estadoFinalAutEsq);
        }
        // seta estados finais do automato da direita como finais do novo automato
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAutDir = autDir.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinaisAutDir.hasNext()) {
            String estadoFinalAutDir = iteratorEstadosFinaisAutDir.next().getKey();
            autTemp.setEstadoFinal(estadoFinalAutDir);
        }
        // adiciona transicoes do automato da esquerda
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoesAutEsq =
                autEsq.getTransicoes().entrySet().iterator();
        while (iteratorTransicoesAutEsq.hasNext()) {
            ArrayList<Transicao> t = iteratorTransicoesAutEsq.next().getValue();
            for (int i = 0; i < t.size(); i++) {
                autTemp.setTransicao(t.get(i).getOrigem().getID(),
                        t.get(i).getDestino().getID(), t.get(i).getSimbolo());
            }
        }
        // adiciona transicoes do automato da direita
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoesAutDir =
                autDir.getTransicoes().entrySet().iterator();
        while (iteratorTransicoesAutDir.hasNext()) {
            ArrayList<Transicao> t = iteratorTransicoesAutDir.next().getValue();
            for (int i = 0; i < t.size(); i++) {
                autTemp.setTransicao(t.get(i).getOrigem().getID(),
                        t.get(i).getDestino().getID(), t.get(i).getSimbolo());
            }
        }
        // adiciona transicoes epsilons para o antigo inicial do automato da esquerda
        autTemp.setTransicao(idNovoEstadoInicial, autEsq.getEstadoInicial().getID(), "*");
        // adiciona transicoes epsilons para o antigo inicial do automato da direita
        autTemp.setTransicao(idNovoEstadoInicial, autDir.getEstadoInicial().getID(), "*");
        return autTemp;
    }

    private Automato concatenacao(Automato autEsq, Automato autDir) {
        Automato autTemp = new Automato();
        // Adiciona estados dos afn da esquerda
        Iterator<Entry<String, Estado>> iteratorEstadosEsq =
                autEsq.getEstados().entrySet().iterator();
        while (iteratorEstadosEsq.hasNext()) {
            String estado = iteratorEstadosEsq.next().getKey();
            autTemp.setEstado(estado);
        }
        // Adiciona estados dos afn da direita
        Iterator<Entry<String, Estado>> iteratorEstadosDir =
                autDir.getEstados().entrySet().iterator();
        while (iteratorEstadosDir.hasNext()) {
            String estado = iteratorEstadosDir.next().getKey();
            autTemp.setEstado(estado);
        }
        // adiciona como estado inicial o estado inicial do automato da esquerda
        autTemp.setEstadoInicial(autEsq.getEstadoInicial().getID());
        // adiciona como estados finais os estados finis do automato da direta
        Iterator<Entry<String, Estado>> iteratorEstadosFinais =
                autDir.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinais.hasNext()) {
            String estado = iteratorEstadosFinais.next().getKey();
            autTemp.setEstadoFinal(estado);
        }
        // adiciona transicoes do automato da esquerda
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoesAutEsq =
                autEsq.getTransicoes().entrySet().iterator();
        while (iteratorTransicoesAutEsq.hasNext()) {
            ArrayList<Transicao> transicoes = iteratorTransicoesAutEsq.next().getValue();
            for (int i = 0; i < transicoes.size(); i++) {
                autTemp.setTransicao(transicoes.get(i).getOrigem().getID(),
                        transicoes.get(i).getDestino().getID(),
                        transicoes.get(i).getSimbolo());
            }
        }
        // adiciona transicoes do automato da esquerda
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoesAutDir =
                autDir.getTransicoes().entrySet().iterator();
        while (iteratorTransicoesAutDir.hasNext()) {
            ArrayList<Transicao> transicoes = iteratorTransicoesAutDir.next().getValue();
            for (int i = 0; i < transicoes.size(); i++) {
                autTemp.setTransicao(transicoes.get(i).getOrigem().getID(),
                        transicoes.get(i).getDestino().getID(),
                        transicoes.get(i).getSimbolo());
            }
        }
        // adiciona transicao epsilon dos estados finais do automato da esquerda para o 
        // inicial do automato da direita
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAutEsq =
                autEsq.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinaisAutEsq.hasNext()) {
            String estadoFinal = iteratorEstadosFinaisAutEsq.next().getKey();
            autTemp.setTransicao(estadoFinal, autDir.getEstadoInicial().getID(), "*");
        }
        return autTemp;
    }

    private Automato fechoKleene(Automato aut) {
        Automato autTemp = new Automato();
        // Adiciona estados do afn de entrada
        Iterator<Entry<String, Estado>> iteratorEstadosEsq =
                aut.getEstados().entrySet().iterator();
        while (iteratorEstadosEsq.hasNext()) {
            String estado = iteratorEstadosEsq.next().getKey();
            autTemp.setEstado(estado);
        }
        // cria novo estado inicial
        String estadoInicial = getNextSimbol();
        autTemp.setEstado(estadoInicial);
        autTemp.setEstadoInicial(estadoInicial);
        // seta esse estado inicial como final tambem
        autTemp.setEstadoFinal(estadoInicial);
        // adiciona transicao epsilon do inicial para o antigo inicial
        // do automato de entrada
        autTemp.setTransicao(autTemp.getEstadoInicial().getID(),
                aut.getEstadoInicial().getID(), "*");
        // adiciona transicoes do automato de entrada
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoesAut =
                aut.getTransicoes().entrySet().iterator();
        while (iteratorTransicoesAut.hasNext()) {
            ArrayList<Transicao> transicoes = iteratorTransicoesAut.next().getValue();
            for (int i = 0; i < transicoes.size(); i++) {
                autTemp.setTransicao(transicoes.get(i).getOrigem().getID(),
                        transicoes.get(i).getDestino().getID(),
                        transicoes.get(i).getSimbolo());
            }
        }
        // adiciona transicao epsilon dos estados finais do afn de entrada
        // para o antigo estado inicial dele
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAut =
                aut.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinaisAut.hasNext()) {
            String estadoFinal = iteratorEstadosFinaisAut.next().getKey();
            autTemp.setTransicao(estadoFinal, aut.getEstadoInicial().getID(), "*");
        }
        // seta estados finais do afn
        Iterator<Entry<String, Estado>> iteratorEstadosFinais =
                aut.getEstadosFinais().entrySet().iterator();
        while (iteratorEstadosFinais.hasNext()) {
            String estado = iteratorEstadosFinais.next().getKey();
            autTemp.setEstadoFinal(estado);
        }
        return autTemp;
    }

    private Automato epsilon(Automato aut) {
        // seta estado inicial do afn como final
        aut.setEstadoFinal(aut.getEstadoInicial().getID());
        return aut;
    }

    private Automato linguagemVazia() {
        Automato aut = new Automato();
        String estado = getNextSimbol();
        aut.setEstado(estado);
        // Adiciona uma estado inicial
        aut.setEstadoInicial(estado);
        return aut;
    }

    private Automato a(String caracter) {
        Automato autTemp = new Automato();
        String idEstadoInicial = getNextSimbol();
        String idEstadoFinal = getNextSimbol();
        autTemp.setEstadoInicial(idEstadoInicial);      // cria estado inicial
        autTemp.setEstadoFinal(idEstadoFinal);          // cria estado final
        autTemp.setEstado(idEstadoFinal);               // adiciona-os como estados do afn
        autTemp.setEstado(idEstadoInicial);
        autTemp.setTransicao(idEstadoInicial, idEstadoFinal, caracter);     // seta a transicao
        return autTemp;
    }

    /**
     * Retorna um inteiro randomico para rotular o estado
     *
     * @param simboloInicial
     * @return
     */
    private String getNextSimbol() {
        return String.valueOf(new Random().nextInt());
    }

    private void lerER() {
        for (int i = 0; i < ER.length(); i++) {
            if (ER.substring(i, i + 1).equals(".")) {
                if(!andamentoTransformacao.isEmpty()){
                    andamentoTransformacao.add(".");
                }
            } else if (ER.substring(i, i + 1).equals("+")) {
                //uniao(ER.substring(i - 1, i), ER.substring(i + 1, i + 2));
            } else if (ER.substring(i, i + 1).equals("*")) {
                //fechoKleene(ER.substring(i - 1, i));
            } else if (ER.substring(i, i + 1).matches("[a-zA-Z_0-9]")) {
                AFN = a(ER.substring(i, i + 1));
                andamentoTransformacao.add(AFN);
            } else if (ER.substring(i, i + 1).equals("E")) {
                //epsilon(ER.substring(i, i + 1));
            } else if (ER.substring(i, i + 1).equals("V")) {
                //linguagemVazia(ER.substring(i, i + 1));
            } else if (ER.substring(i, i + 1).equals("(")) {    // verifica a precedencia de parenteses
                Automato automatoAux = new Automato();

            }
        }
    }

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
        new ReconhecedorCadeia(conversao.getAFD(), "b");
    }
}
