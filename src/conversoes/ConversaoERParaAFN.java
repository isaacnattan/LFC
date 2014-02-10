package conversoes;

import automato.Automato;
import automato.Estado;
import automato.Transicao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import util.ChaveComposta;

/**
 * @author Isaac_Nattan
 */
public class ConversaoERParaAFN {

    private Automato AFN;
    private ArrayList expressaoRegular;
    private ArrayList<String> cadeias = new ArrayList<>();

    public ConversaoERParaAFN(String pathEntrada) {
        expressaoRegular = leituraInicialER(processaEntrada(pathEntrada).get(0));
        AFN = resolveER(expressaoRegular);
    }
    
    private Automato resolveER(ArrayList erPreProcessada) {
        Automato afnFinal;
        while (erPreProcessada.contains("(")) {                          // enquanto expressao regular contiver parenteses
            ArrayList erInicial = getERDentroParenteses(erPreProcessada);// obter a expressao dentro do primeiro parenteses que encontrar
            Automato autParcial = resolveERSemParenteses(erInicial);      // resolve expressao dentro do parenteses
            erPreProcessada = integraResultadoParenteses(autParcial, erPreProcessada);// integra resultado na expressao inicial
        }
        // Nesse ponto todos os parenteses ja devem ter sido resolvidos, logo
        // resolve a expressao final
        afnFinal = resolveERSemParenteses(erPreProcessada);
        return afnFinal;
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
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAut = aut.getEstadosFinais().entrySet().iterator();
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

    private Automato palavraVazia() {
        Automato autTemp = new Automato();
        String rotulo = getNextSimbol();
        // Cria um estado
        autTemp.setEstado(rotulo);
        // que eh inicial
        autTemp.setEstadoInicial(rotulo);
        // e tambem final
        autTemp.setEstadoFinal(rotulo);
        return autTemp;
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
     * Retorna uma string com um inteiro randomico para rotular o estado
     *
     * @param simboloInicial
     * @return
     */
    private String getNextSimbol() {
        return String.valueOf(new Random().nextInt());
    }

    private ArrayList<String> processaEntrada(String arquivoEntrada) {
        File input = new File(arquivoEntrada);
        ArrayList<String> erCadeias = new ArrayList<>();
        try {
            if (input.exists()) {
                FileReader fr = new FileReader(input);
                BufferedReader br = new BufferedReader(fr);
                //equanto houver mais linhas
                while (br.ready()) {
                    //lê a proxima linha
                    String linha = br.readLine();
                    erCadeias.add(linha);
                }
                br.close();
                fr.close();
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Arquivo de entrada não encontrado.");
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Problemas com a localização do arquivo de entrada. " + ex);
            return null;
        }
        // guarda as cadeias de entrada em uma outra variavel -> Isso ta feio 
        // pra caramba, mas tem coisas mais urgentes pra cuidar
        for (int i = 0; i < erCadeias.size(); i++) {
            cadeias.add((String) erCadeias.get(i));
        }
        // remove a posicao inicial (ER)
        cadeias.remove(0);
        return erCadeias;
    }

    /**
     * Retira todos os símbolos da ER, substituindo-os por seus respectivos
     * AFN's.
     */
    private ArrayList leituraInicialER(String ER) {
        ArrayList ERInicial = new ArrayList();
        for (int i = 0; i < ER.length(); i++) {
            if (ER.substring(i, i + 1).equals(".")) {
                ERInicial.add(".");
            } else if (ER.substring(i, i + 1).equals("+")) {
                ERInicial.add("+");
            } else if (ER.substring(i, i + 1).equals("*")) {
                ERInicial.add("*");
            } else if (ER.substring(i, i + 1).matches("[a-z_0-9]")) {
                ERInicial.add(a(ER.substring(i, i + 1)));
            } else if (ER.substring(i, i + 1).equals("(")) {
                ERInicial.add("(");
            } else if (ER.substring(i, i + 1).equals(")")) {
                ERInicial.add(")");
            } else if (ER.substring(i, i + 1).equals("E")) {
                ERInicial.add(palavraVazia());
            } else if (ER.substring(i, i + 1).equals("V")) {
                ERInicial.add(linguagemVazia());
            }
        }
        return ERInicial;
    }

    private ArrayList getERDentroParenteses(ArrayList ER) {
        ArrayList erDoParenteses = new ArrayList();
        for (int i = 0; i < ER.size(); i++) {
            if (ER.get(i).equals("(")) {
                i++;
                while (!ER.get(i).equals(")")) {
                    erDoParenteses.add(ER.get(i));
                    i++;
                }
                break;
            }
        }
        return erDoParenteses;
    }

    /**
     * Resolve a ER contida no parenteses.
     *
     * @param erInicial
     * @return Automato
     */
    private Automato resolveERSemParenteses(ArrayList erInicial) {
        Automato aut;
        ArrayList aux = new ArrayList();
        ArrayList<Integer> marcacoes = new ArrayList<>();
        // precedencia inicial: fecho de kleene
        for (int j = 0; j < erInicial.size(); j++) {
            aux.add(erInicial.get(j));
            if (erInicial.get(j).equals("*")) {
                aut = fechoKleene((Automato) erInicial.get(j - 1));
                // marca o anterior para apagar depois, o resultado
                // vai sobrescrever a posicao atual, tambem descartavel, o "*"
                marcacoes.add(j - 1);
                // adiciona aut
                aux.add(j, aut);
                // remove o elemento que foi empurrado pra frente 
                aux.remove(j + 1);
            }
        }
        // ordena as marcacoes
        Collections.sort(marcacoes);
        Collections.reverse(marcacoes);
        // elimina posicoes marcadas
        for (int j = 0; j < marcacoes.size(); j++) {
            aux.remove(marcacoes.get(j).intValue());
        }
        // copia completamente o conteudo do aux na erSemParenteses
        erInicial = aux;
        // limpa aux
        aux = new ArrayList();
        // limpa as marcacoes
        marcacoes = new ArrayList<>();
        // precedencia seguinte: concatenacao
        for (int j = 0; j < erInicial.size(); j++) {
            aux.add(erInicial.get(j));
            if (erInicial.get(j).equals(".")) {
                aut = concatenacao((Automato) aux.get(j - 1),
                        (Automato) erInicial.get(j + 1));
                // marca o atual e o anterior do auxiliar (A1 + ), para apagar 
                // posteriormente
                marcacoes.add(j);
                marcacoes.add(j - 1);
                // pula o proximo, porque fez parte da computacao anterior
                j++;
                // adiciona aut no lugar do proximo
                aux.add(j, aut);
            }
        }
        // ordena as marcacoes
        Collections.sort(marcacoes);
        Collections.reverse(marcacoes);
        // elimina posicoes marcadas
        for (int j = 0; j < marcacoes.size(); j++) {
            aux.remove(marcacoes.get(j).intValue());
        }
        // copia completamente o conteudo do aux na erSemParenteses
        erInicial = aux;
        // limpa aux
        aux = new ArrayList();
        // limpa as marcacoes
        marcacoes = new ArrayList<>();
        // precedencia seguinte: uniao
        for (int j = 0; j < erInicial.size(); j++) {
            aux.add(erInicial.get(j));
            if (erInicial.get(j).equals("+")) {
                aut = uniao((Automato) aux.get(j - 1),
                        (Automato) erInicial.get(j + 1));
                // apaga o atual e o anterior do auxiliar (A1 + )
                marcacoes.add(j);
                marcacoes.add(j - 1);
                // pula o proximo, porque fez parte da computacao anterior
                j++;
                // adiciona aut
                aux.add(aut);
            }
        }
        // ordena as marcacoes
        Collections.sort(marcacoes);
        Collections.reverse(marcacoes);
        // elimina posicoes marcadas
        for (int j = 0; j < marcacoes.size(); j++) {
            aux.remove(marcacoes.get(j).intValue());
        }
        // copia completamente o conteudo do aux na erSemParenteses
        erInicial = aux;
        return (Automato) erInicial.get(0);
    }

    private ArrayList integraResultadoParenteses(Automato automatoDoParenteses, ArrayList erInicial) {
        ArrayList erFinal = new ArrayList();
        for (int i = 0; i < erInicial.size(); i++) {
            if (erInicial.get(i).equals("(")) {
                erFinal.add(automatoDoParenteses);
                while (!erInicial.get(i).equals(")")) {
                    i++;
                }
                // pula o parenteses 
                i++;
                // copia todo o resto da ER
                for (int j = i; j < erInicial.size(); j++) {
                    erFinal.add(erInicial.get(j));
                }
                break;      // abandona o loop 
            } else {
                erFinal.add(erInicial.get(i));
            }
        }
        return erFinal;
    }
	
    public Automato getAFN() {
        return AFN;
    }

    public ArrayList<String> getCadeias() {
        return cadeias;
    }
}
