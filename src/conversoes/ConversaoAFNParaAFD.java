package conversoes;

import automato.Automato;
import automato.Estado;
import automato.Transicao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import util.ChaveComposta;

/**
 * @author Isaac_Nattan
 */
public class ConversaoAFNParaAFD {

    private Automato AFD;
    private Automato AFN;
    private HashMap<String, Estado> fechos;
    private HashMap<ChaveComposta, ArrayList<Transicao>> delta;
    private Estado estadoInicial;
    private HashMap<String, Estado> estadosFinais;

    public ConversaoAFNParaAFD(Automato AFN) {
        this.AFN = rotularAFN(AFN);
        AFD = new Automato();                   // Inicializa AFD
        fechos = calculaFechoTodosEstados();
        eliminaEstadosContidos();
        delta = calculaDelta();
        recalculaDeltaSePreciso();
        estadoInicial = getEstadoInicial();
        estadosFinais = getEstadosFinais();
        //simplificaAFD();
        // Monta AFD, finalmente
        AFD.montarAFD(estadoInicial, delta, fechos, estadosFinais);
    }

    /**
     * Rotula os estados do AFN para estados com nomes mais amigaveis. A
     * conversao de ER para AFD gera rotulos de estados randomicos.
     */
    private Automato rotularAFN(Automato afn) {
        Automato afnAux = new Automato();
        HashMap<String, String> mapeamenteEstados = new HashMap<>();
        String simboloCorrente = "a";
        Iterator<Entry<String, Estado>> iteratorEstados = afn.getEstados().
                entrySet().iterator();
        //ArrayList<String> simbolos = afn.getSimbolosTrasicoes();
        // Adiciona mesmos estados e transicoes com rotulos diferentes
        while (iteratorEstados.hasNext()) {
            String estado = iteratorEstados.next().getKey();
            if (afn.getEstadoInicial().getID().equals(estado) // se eh estado inicial e final ao mesmo tempo
                    && afn.getEstadosFinais().containsKey(estado)) {
                simboloCorrente = getNextSimbolo(simboloCorrente);
                afnAux.setEstadoInicial(simboloCorrente);
                afnAux.setEstadoFinal(simboloCorrente);
                afnAux.setEstado(simboloCorrente);
                mapeamenteEstados.put(estado, simboloCorrente);
            } else if (afn.getEstadoInicial().getID().equals(estado)) {     // se eh somente estado inicial
                simboloCorrente = getNextSimbolo(simboloCorrente);
                afnAux.setEstadoInicial(simboloCorrente);
                afnAux.setEstado(simboloCorrente);
                mapeamenteEstados.put(estado, simboloCorrente);
            } else if (afn.getEstadosFinais().containsKey(estado)) {        // se eh somente estado final
                simboloCorrente = getNextSimbolo(simboloCorrente);
                afnAux.setEstadoInicial(simboloCorrente);
                afnAux.setEstadoFinal(simboloCorrente);
                afnAux.setEstado(simboloCorrente);
                mapeamenteEstados.put(estado, simboloCorrente);
            } else {                                                    // se eh somente um estado intermediario
                simboloCorrente = getNextSimbolo(simboloCorrente);
                afnAux.setEstado(simboloCorrente);
                mapeamenteEstados.put(estado, simboloCorrente);
            }
        }
        // verificar as transicoes de cada estado
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorTransicoes =
                afn.getTransicoes().entrySet().iterator();
        while (iteratorTransicoes.hasNext()) {      // itera sobre as transicoes 
            ArrayList<Transicao> trans = iteratorTransicoes.next().getValue();
            for (int i = 0; i < trans.size(); i++) {      // verifica o caso de haver mais de uma transicao a partir do mesmo estado
                afnAux.setTransicao(mapeamenteEstados.get(trans.get(i).getOrigem().getID()),
                        mapeamenteEstados.get(trans.get(i).getDestino().getID()), trans.get(i).getSimbolo());
            }
        }
        return afnAux;
    }

    /**
     * Metodo auxiliar que retorna o caracter seguinte do simboloInicial. Ex.:
     * entrada: "a", saida: "b"
     *
     * @param simboloInicial
     * @return
     */
    private String getNextSimbolo(String simboloInicial) {
        String inicial = simboloInicial;
        int seguinte = (int) inicial.toCharArray()[0] + 1;
        return "" + (char) seguinte;
    }

    private Estado calculaFecho(String idEstado) {
        String fecho = idEstado;
        ArrayList<Transicao> caminhos = AFN.getTransicao(new ChaveComposta(idEstado, "*"));
        for (int i = 0; i < caminhos.size(); i++) {
            ArrayList<Transicao> novosCaminhos = AFN.getTransicao(new ChaveComposta(caminhos.get(i).getDestino().getID(), "*"));
            if (!novosCaminhos.isEmpty()) {
                caminhos.addAll(novosCaminhos);
            }
            fecho += caminhos.get(i).getDestino().getID();
        }
        return new Estado(eliminaElementosIguais(fecho));
    }

    private HashMap<String, Estado> calculaFechoTodosEstados() {
        HashMap<String, Estado> fechosAFD = new HashMap<>();
        HashMap<String, Estado> estadosAFN = AFN.getEstados();
        Iterator<Entry<String, Estado>> iteratorAFD = estadosAFN.entrySet().iterator();
        while (iteratorAFD.hasNext()) {
            String estadoCorrente = iteratorAFD.next().getKey();
            if (fechosAFD.isEmpty()) {
                fechosAFD.put(estadoCorrente, calculaFecho(estadoCorrente));
            } else {    // se ja existem fechos, verificar se o estado corrente ja nao faz parte de um deles
                if (!fechosAFD.containsKey(estadoCorrente)) {
                    fechosAFD.put(estadoCorrente, calculaFecho(estadoCorrente));
                }
            }
        }
        return fechosAFD;
    }

    private HashMap<ChaveComposta, ArrayList<Transicao>> calculaDelta() {
        HashMap<ChaveComposta, ArrayList<Transicao>> transicoesAFN = AFN.getTransicoes();
        HashMap<ChaveComposta, ArrayList<Transicao>> deltaAFD = new HashMap<>();
        Iterator<Entry<String, Estado>> iteratorFechos = fechos.entrySet().iterator();
        ArrayList<String> simbolos = AFN.getSimbolosTrasicoes();
        simbolos.remove("*");
        String idDestino = "";
        while (iteratorFechos.hasNext()) {
            Estado estadoCorrente = iteratorFechos.next().getValue();   // Obtem um estado
            for (int j = 0; j < simbolos.size(); j++) {                     // verifica-o para cada um dos simbolos
                idDestino = "";
                for (int i = 0; i < estadoCorrente.getID().length(); i++) { // cada pedaco do estado
                    ArrayList<Transicao> transicoesAFD =
                            transicoesAFN.get(new ChaveComposta(
                            estadoCorrente.getID().substring(i, i + 1),
                            simbolos.get(j)));
                    if (transicoesAFD != null) {
                        for (int k = 0; k < transicoesAFD.size(); k++) {
                            idDestino += calculaFecho(transicoesAFD.get(k).getDestino().getID()).getID();
                        }
                    } else {    // se nao retornar nenhuma transicao vai pro limbo
                    }
                }
                ArrayList<Transicao> temp = new ArrayList<>();
                temp.add(new Transicao(estadoCorrente.getID(), eliminaElementosIguais(idDestino), simbolos.get(j)));
                deltaAFD.put(new ChaveComposta(eliminaElementosIguais(estadoCorrente.getID()), simbolos.get(j)), temp);
            }
        }
        return deltaAFD;
    }

    private boolean verificaSurgimentoNovosEstados() {
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iterator = delta.entrySet().iterator();
        boolean ok = false;
        while (iterator.hasNext()) {
            ArrayList<Transicao> destinos = iterator.next().getValue();
            String provavelNovoEstado = "";
            for (int i = 0; i < destinos.size(); i++) {
                provavelNovoEstado += destinos.get(i).getDestino().getID();
            }
            if (!fechos.containsValue(new Estado(eliminaElementosIguais(provavelNovoEstado)))) {
                fechos.put(eliminaElementosIguais(provavelNovoEstado),
                        new Estado(eliminaElementosIguais(provavelNovoEstado)));
                ok = true;
            }
        }
        return ok;
    }

    private void recalculaDeltaSePreciso() {
        while (verificaSurgimentoNovosEstados()) {
            delta = calculaDelta();
        }
    }

    private HashMap<String, Estado> eliminaEstadosContidos() {
        // se um estado esta contido dentro de outro o fecho dele sera igual ao fecho do outro
        Iterator<Entry<String, Estado>> iteratorEstadosAFN = AFN.getEstados().entrySet().iterator();
        Iterator<Entry<String, Estado>> iteratorEstadosAFD = fechos.entrySet().iterator();
        HashMap<String, Estado> removerEstadosAFD = new HashMap<>();
        while (iteratorEstadosAFN.hasNext()) {
            String estadoAFN = iteratorEstadosAFN.next().getKey();
            while (iteratorEstadosAFD.hasNext()) {
                String estadoAFD = iteratorEstadosAFD.next().getKey();
                if (calculaFecho(estadoAFN).getID().contains(estadoAFD)
                        && !calculaFecho(estadoAFN).getID().equals(calculaFecho(estadoAFD).getID())) {
                    removerEstadosAFD.put(estadoAFD, calculaFecho(estadoAFN));
                }
            }
            iteratorEstadosAFD = fechos.entrySet().iterator();
        }
        Iterator<Entry<String, Estado>> itRemoverFechos = removerEstadosAFD.entrySet().iterator();
        while (itRemoverFechos.hasNext()) {
            String estadoCorrente = itRemoverFechos.next().getKey();
            fechos.remove(estadoCorrente);
        }
        return removerEstadosAFD;
    }

    private Estado getEstadoInicial() {
        Estado estadoInicialAFN = AFN.getEstadoInicial();
        return calculaFecho(estadoInicialAFN.getID());
    }
    
    private HashMap<String, Estado> getEstadosFinais() {
        HashMap<String, Estado> estadosFinaisAFN = AFN.getEstadosFinais();
        HashMap<String, Estado> estadosFinaisAFD = new HashMap<>();
        Iterator<Entry<String, Estado>> iteratorEstadosFinaisAFN = estadosFinaisAFN.entrySet().iterator();
        while (iteratorEstadosFinaisAFN.hasNext()) {     // adiciona somente os estados que ja eram finais no afn
            String estadoAFN = iteratorEstadosFinaisAFN.next().getKey();
            estadosFinaisAFD.put(calculaFecho(estadoAFN).getID(), calculaFecho(estadoAFN));
        }
        Iterator<Entry<String, Estado>> iteratorEstadoAFD = fechos.entrySet().iterator();
        iteratorEstadosFinaisAFN = estadosFinaisAFN.entrySet().iterator();
        while (iteratorEstadoAFD.hasNext()) {       // adiciona estados novos AFD que contem estado final do AFN
            String estadoCorrente = iteratorEstadoAFD.next().getKey();
            while (iteratorEstadosFinaisAFN.hasNext()) {
                if (estadoCorrente.contains(iteratorEstadosFinaisAFN.next().getKey())) {
                    estadosFinaisAFD.put(estadoCorrente, new Estado(estadoCorrente));
                }
            }
            iteratorEstadosFinaisAFN = estadosFinaisAFN.entrySet().iterator();
        }
        return estadosFinaisAFD;
    }

    /**
     * Metodo auxiliar que dada uma string de entrada, retorna a string sem
     * caracteres repetidos.
     *
     * @param subconjunto
     * @return
     */
    private String eliminaElementosIguais(String subconjunto) {
        ArrayList<Character> lista = new ArrayList<>();
        for (int i = 0; i < subconjunto.length(); i++) {
            lista.add(subconjunto.substring(i, i + 1).toCharArray()[0]);
        }
        Collections.sort(lista);
        subconjunto = "";
        for (int j = 0; j < lista.size(); j++) {
            subconjunto += lista.toArray()[j];
        }
        return subconjunto.replaceAll("(([A-Za-z0-9])(\\2)+)", "$2");
    }

    private void simplificaAFD() {
        // elimina transicoes inacessiveis do AFD
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> iteratorDeltaAFD =
                delta.entrySet().iterator();
        HashMap<String, Estado> estadosQueSeraoRemovidos = new HashMap<>();
        HashMap<ChaveComposta, ArrayList<Transicao>> transicoesQueSeraoRemovidas = new HashMap<>();
        // todos os destinos das transicoes ja existentes sao os estados que continuarao
        while (iteratorDeltaAFD.hasNext()) {
            ArrayList<Transicao> destinos = iteratorDeltaAFD.next().getValue();
            for (int i = 0; i < destinos.size(); i++) {
                estadosQueSeraoRemovidos.put(calculaFecho(destinos.get(i).getDestino().getID()).getID(),
                        calculaFecho(destinos.get(i).getDestino().getID()));
            }
        }
        // remove as transicoes que nao sao dos estados que vao continuar
        Iterator<Entry<String, Estado>> itEstadosQueSeraoRemovidos =
                estadosQueSeraoRemovidos.entrySet().iterator();
        ArrayList<String> simbolos = AFN.getSimbolosTrasicoes();
        simbolos.remove("*");
        while (itEstadosQueSeraoRemovidos.hasNext()) {
            String estadoCorrente = itEstadosQueSeraoRemovidos.next().getKey();
            for (int i = 0; i < simbolos.size(); i++) {
                ArrayList<Transicao> destinos = AFD.getTransicao(
                        new ChaveComposta(estadoCorrente, simbolos.get(i)));
                transicoesQueSeraoRemovidas.put(new ChaveComposta(estadoCorrente, simbolos.get(i)),
                        destinos);
            }
        }
        itEstadosQueSeraoRemovidos =
                estadosQueSeraoRemovidos.entrySet().iterator();
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> itTransicoesQueSeraoRemovidas =
                transicoesQueSeraoRemovidas.entrySet().iterator();
        while (itEstadosQueSeraoRemovidos.hasNext()) {
            String estadoCorrente = itEstadosQueSeraoRemovidos.next().getKey();
            fechos.remove(estadoCorrente);
        }
        while (itTransicoesQueSeraoRemovidas.hasNext()) {
            ChaveComposta chave = itTransicoesQueSeraoRemovidas.next().getKey();
            delta.remove(chave);
        }
        System.err.println("");
    }

    public Automato getAFD() {
        return AFD;
    }
}
