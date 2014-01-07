package AFNParaAFD;

import automato.Automato;
import automato.Estado;
import automato.Transicao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author Isaac Nattan
 * @version 1.0
 */
public class AFNParaAFD {

    private Automato AFD;
    private Automato AFN;
    //desempenho
    private long timeInicial;
    private long timeFinal;

    public AFNParaAFD(Automato afn) {
        this.AFN = afn;
        AFD = new Automato();
        setEstadosAFD();
        setTransicoes();
        imprimeTransicoesAFD();
        tratarTransicoesEpsilons();
        imprimeTransicoesAFD();
        simplificaAFD();
        imprimeTransicoesAFD();
        setEstadoInicial();
        setEstadoFinal();
    }

    /**
     * Rotina que origina todos os estados do AFD. Os estados são representados
     * pelo conjunto dos subconjuntos dos estados do AFN.
     */
    private void setEstadosAFD() {
        timeInicial = System.currentTimeMillis();
        HashMap<String, Estado> estadosAFN = AFN.getEstados();
        ArrayList<String> listaEstadosAFN = new ArrayList<>();
        ArrayList<String> conjuntoSubconjuntos = new ArrayList<>();
        for (Entry<String, Estado> entry : estadosAFN.entrySet()) {
            listaEstadosAFN.add(entry.getKey());
            AFD.setEstado(entry.getKey());
        }
        Collections.sort(listaEstadosAFN);
        // seta elementos iniciais
        conjuntoSubconjuntos.addAll(listaEstadosAFN);
        // algoritmo powerset
        int elementoInicial = 0, proxElemento = elementoInicial + 1;
        String ultimoElemento = listaEstadosAFN.get(listaEstadosAFN.size() - 1);
        while (elementoInicial < conjuntoSubconjuntos.size() - 2) {   // -2 porque o vazio ainda nao foi adicionado
            if (!conjuntoSubconjuntos.get(elementoInicial).contains(ultimoElemento)) {
                conjuntoSubconjuntos.add(conjuntoSubconjuntos.get(elementoInicial)
                        + getLastElement(conjuntoSubconjuntos.get(proxElemento)));
                // cria estado no AFD
                AFD.setEstado(conjuntoSubconjuntos.get(elementoInicial)
                        + getLastElement(conjuntoSubconjuntos.get(proxElemento)));
                if (conjuntoSubconjuntos.get(proxElemento).contains(ultimoElemento)) {
                    elementoInicial++;
                    proxElemento = elementoInicial + 1;
                } else {    // se o segundo elemento contem o elemento final
                    proxElemento++;
                }
            } else {
                elementoInicial++;
                proxElemento = elementoInicial + 1;
            }
        }
        // Adiciona o conjunto vazio e bye bye xD
        AFD.setEstado("");
        timeFinal = System.currentTimeMillis();
        System.out.println("Tempo de execução setEstadosAFD:" + (timeFinal - timeInicial) + " ms");
    }

    /**
     * Rotina que origina todas as transicoes do AFD. As transicoes depende do
     * conjunto de estados do AFD. Aqui constam todas as transicoes. Ex.: se o
     * AFD tem 3 simbolos e 8 estados logo 3x8=24 transicoes. 3 transicoes para
     * cada estado, mesmo que no alfabeto conste transicao epsilon. Um outro
     * metodo mais a frente se encarregara de fazer simplificacoes. Ps.:
     */
    private void setTransicoes() {
        timeInicial = System.currentTimeMillis();
        HashMap<String, Estado> estadosAFD = AFD.getEstados();
        List<String> listaEstadosAFD = new ArrayList<>();
        for (Entry<String, Estado> entry : estadosAFD.entrySet()) {
            listaEstadosAFD.add(entry.getKey());
        }
        // Ordenacao customizada
        Collections.sort(listaEstadosAFD, new Comparator<String>() {
            @Override
            public int compare(String t, String t1) {
                if (t.length() == t1.length() && t.length() > 1) {
                    for (int i = 0; i < t.length(); i++) {
                        if (!t.substring(i, i + 1).equals(t1.substring(i, i + 1))) {
                            // verifica somente o elemento mais a esquerda
                            if ((int) t.substring(i, i + 1).toCharArray()[0]
                                    > (int) t1.substring(i, i + 1).toCharArray()[0]) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    }
                    return 0;
                } else if (t.length() < t1.length()) {
                    return -1;
                } else if (t.length() > t1.length()) {
                    return 1;
                } else {
                    if ((int) t.substring(0, 1).toCharArray()[0] > (int) t1.substring(0, 1).toCharArray()[0]) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });
        ArrayList<String> simbolosTransicoes = getSimbolosTrasicoes();
        simbolosTransicoes.remove("*");     // tratar transicoes epsilon em outra rotina
        // Adiciona transicoes para vazio. Vazio com qualquer coisa vai para o vazio
        for (int z = 0; z < simbolosTransicoes.size(); z++) {
            AFD.setTransicao("", "", simbolosTransicoes.get(z));
        }
        for (int i = 0; i < listaEstadosAFD.size(); i++) {      // verifica cada estado do AFD
            if (listaEstadosAFD.get(i).length() > 1) {      // estados nao unitarios
                for (int l = 0; l < simbolosTransicoes.size(); l++) {         // para cada simbolo
                    String origem = "", destino = "", simbolo = "", estadoCorrente = listaEstadosAFD.get(i);
                    for (int k = 0; k < estadoCorrente.length(); k++) {     // cada estado do conjunto
                        String porcaoEstadoCorrente = estadoCorrente.substring(k, k + 1);
                        ArrayList<Transicao> t = AFN.getTransicao(porcaoEstadoCorrente, simbolosTransicoes.get(l));
                        for (int x = 0; x < t.size(); x++) {    // getTransicao pode retornar uma lista de trans
                            if (t.isEmpty()) {                      // ou nao
                                origem = t.get(x).getOrigem().getID();
                                destino += "";
                                simbolo = t.get(x).getSimbolo();
                            }
                            if (!destino.contains(t.get(x).getDestino().getID())) {
                                destino += t.get(x).getDestino().getID();
                                origem = estadoCorrente;
                                simbolo = t.get(x).getSimbolo();
                            }
                        }
                    }
                    // Caso o estado fique desordenado ordena-o. Ex.: existe 23
                    // mas esse metodo obtem 32, sao iguais, mas daria erro
                    if (destino.length() > 1) {
                        for (int g = 0; g < listaEstadosAFD.size(); g++) {
                            if (listaEstadosAFD.get(g).length() == destino.length()) {
                                String estado = listaEstadosAFD.get(g);
                                for (int f = 0; f < destino.length(); f++) {
                                    if (!estado.contains(destino.substring(f, f + 1))) {
                                        break;
                                    }
                                }
                                destino = listaEstadosAFD.get(g);
                            }
                        }
                    }
                    AFD.setTransicao(origem, destino, simbolo);
                }
            } else {        // Somente estados unitarios
                if (!listaEstadosAFD.get(i).equals("")) {
                    for (int j = 0; j < simbolosTransicoes.size(); j++) { // verifica cada simbolo do alfabeto
                        ArrayList<Transicao> t = AFN.getTransicao(listaEstadosAFD.get(i), simbolosTransicoes.get(j));
                        String origem = "", destino = "", simbolo = "";
                        for (int x = 0; x < t.size(); x++) {    // getTransicao pode retornar uma lista de trans
                            if (!destino.contains(t.get(x).getDestino().getID())) {
                                destino += t.get(x).getDestino().getID();
                                origem = t.get(x).getOrigem().getID();
                                simbolo = t.get(x).getSimbolo();
                            }
                        }
                        if (t.isEmpty()) {                      // ou nao
                            origem = listaEstadosAFD.get(i);
                            destino += "";
                            simbolo = simbolosTransicoes.get(j);
                        }
                        // Caso o estado fique desordenado ordena-o. Ex.: existe 23
                        // mas esse metodo obtem 32, sao iguais, mas daria erro
                        if (destino.length() > 1) {
                            for (int g = 0; g < listaEstadosAFD.size(); g++) {
                                if (listaEstadosAFD.get(g).length() == destino.length()) {
                                    String estado = listaEstadosAFD.get(g);
                                    for (int f = 0; f < destino.length(); f++) {
                                        if (!estado.contains(destino.substring(f, f + 1))) {
                                            break;
                                        }
                                    }
                                    destino = listaEstadosAFD.get(g);
                                }
                            }
                        }
                        AFD.setTransicao(origem, destino, simbolo);
                    }
                }
            }
        }
        timeFinal = System.currentTimeMillis();
        System.out.println("Tempo de execução setTransicao:" + (timeFinal - timeInicial) + " ms");
    }

    /**
     * Rotina que retira estados inacessíveis do AFD.
     */
    private void simplificaAFD() {
        HashMap<String, Estado> map = new HashMap<>();
        map.putAll(AFD.getEstados());   // cria uma copia pra naum da excecao de modificacao em tempo real
        Iterator<Entry<String, Estado>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            boolean ok = true;      // deve ser removido ate que prove o contrario
            Entry estado = it.next();
            for (int i = 0; i < AFD.getTransicoes().size(); i++) {
                if (AFD.getTransicoes().get(i).getDestino().getID().equals((String) estado.getKey())) {
                    // se alguem chega nele, provei o contrario x)
                    ok = false;
                    break;
                }
            }
            if (ok) {
                // esse estado pode ser removido
                AFD.removeEstado((String) estado.getKey());
                // remove tambem as transicoes dele
                for (int i = 0; i < getSimbolosTrasicoes().size(); i++) {
                    ArrayList<Transicao> trans = AFD.getTransicao(
                            (String) estado.getKey(), getSimbolosTrasicoes().get(i));
                    for (int j = 0; j < trans.size(); j++) {
                        AFD.removeTransicao(trans.get(j));
                    }
                }
            }
        }
    }

    /**
     * Rotina auxiliar que exibe todas as transições do autômato. Somente para
     * testes.
     */
    private void imprimeTransicoesAFD() {
        System.out.println("");
        System.out.println("");
        for (int i = 0; i < AFD.getTransicoes().size(); i++) {
            System.out.println("Origem: " + AFD.getTransicoes().get(i).getOrigem().getID() + "  "
                    + "Destino: " + AFD.getTransicoes().get(i).getDestino().getID() + "  "
                    + "Simbolo: " + AFD.getTransicoes().get(i).getSimbolo());
        }
    }

    /**
     * Seta o estado inicial do AFD.
     */
    private void setEstadoInicial() {
        Estado estadoInicialAFN = AFN.getEstadoInicial();
        String estadoInicialAFD = estadoInicialAFN.getID();
        while (!AFN.getTransicao(estadoInicialAFD, "*").isEmpty()) {
            // Nao coloquei o for porque nao vai ter 2 transicoes epsilons
            // saindo do mesmo estado
            estadoInicialAFD += AFN.getTransicao(estadoInicialAFD, "*").get(0).getDestino().getID();
        }
        AFD.setEstadoInicial(estadoInicialAFD);
    }

    /**
     * Seta estado(s) final(is) do AFD.
     */
    private void setEstadoFinal() {
        HashMap<String, Estado> estadosFinaisAFN = AFN.getEstadosFinais();
        Iterator<Entry<String, Estado>> it = estadosFinaisAFN.entrySet().iterator();
        while (it.hasNext()) {
            String estadoAFN = it.next().getKey();
            Iterator<Entry<String, Estado>> itAFD =  AFD.getEstados().entrySet().iterator();
            while(itAFD.hasNext()) {
                String estadoAFD = itAFD.next().getKey();
                if(estadoAFD.contains(estadoAFN)){     // se o estado do AFD contem o do AFN, blz
                    AFD.setEstadoFinal(estadoAFD);
                }
            }
        }
    }

    /**
     * Estados que contém transições epsilons serão removidos e substituídos por
     * um outro estado representado pelo conjunto dele + estados alcaçáveis por
     * transições epsilon a partir da dele. Ex: (1) -*-> (2) -*-> (5) o estado 1
     * dará lugar para o estado (125) e o 2 ao (25).
     */
    private void tratarTransicoesEpsilons() {
        for (int i = 0; i < AFN.getTransicoes().size(); i++) {
            if (AFN.getTransicoes().get(i).getSimbolo().equals("*")) {  // Detectei uma transicao epsilon
                for (int j = 0; j < AFD.getTransicoes().size(); j++) {   // procurar estados que chegam na sua origem
                    if (AFD.getTransicoes().get(j).getDestino().getID().equals(
                            AFN.getTransicoes().get(i).getOrigem().getID())) {  // se alguma transicao do AFD chega nesse cara
                        // readiciona transicao com novo destino
                        AFD.setTransicao(AFD.getTransicoes().get(j).getOrigem().getID(),
                                AFN.getTransicoes().get(i).getOrigem().getID()
                                + AFN.getTransicoes().get(i).getDestino().getID(),
                                AFD.getTransicoes().get(j).getSimbolo());
                        // remove transicao com destino velho
                        AFD.removeTransicao(AFD.getTransicoes().get(j));
                    }
                }
            }
        }
    }

    /**
     * Método auxiliar que retorna uma lista de todos os símbolos das transições
     * de um dado autômato.
     *
     * @return {@link ArrayList<String>}
     */
    private ArrayList<String> getSimbolosTrasicoes() {
        ArrayList<String> simbolos = new ArrayList<>();
        Iterator<Transicao> it = AFN.getTransicoes().iterator();
        while (it.hasNext()) {
            String simbolo = it.next().getSimbolo();
            if (!simbolos.contains(simbolo)) {
                simbolos.add(simbolo);
            }
        }
        return simbolos;
    }

    /**
     * Retorna o último elemento de uma string.
     *
     * @param subConjunto
     * @return String
     */
    private String getLastElement(String subConjunto) {
        return subConjunto.substring(subConjunto.length() - 1);
    }

    /**
     * Retorna o AFD correspondente do AFN.
     *
     * @return {@link Automato}
     */
    public Automato getAFD() {
        return AFD;
    }
}
