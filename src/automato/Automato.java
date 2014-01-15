package automato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import util.ChaveComposta;

/**
 * Classe que representa um automato finito.
 * @author Isaac_Nattan
 */
public class Automato {
    // Pode haver um conjunto de estados e estados finais
    // No aplicativo o caracter * tera um significado especial: representara a transicao epsilon
    // Mas somente um estado inicial

    // Um HashMap de estados, para todos os estados e os estados finais
    private HashMap<String, Estado> estados, estadosFinais;
    // Um conjunto de transicoes. A ultilizacao de um tipo como HashMap<ChaveComposta, ArrayList<Transicao>>
    // eh justificada pelo seguinte raciocinio: nao preciso de ordem ao guardar as transicoes e preciso de 
    // tempo constante para recuperar e remover, logo a estrutura de dados que me serve bem a esse proposito
    // eh um HashMap. A chave utilizada eh a origem e o simbolo da transicao, so que uma chave pode me retornar 
    // mais de uma transicao e uma estrutura de dados que me proporciona guardar elemento de chaves iguais, 
    // embora mesmo assim, eu nao precise de ordem eh o ArrayList, logo o tipo ideal pra mim veio a ser
    // HashMap<ChaveComposta, Arraylist<Transicao>>
    private HashMap<ChaveComposta, ArrayList<Transicao>> transicoes;       // Uma chave composta pode ter mais de um estado atrelado a ela
    // Um estado final
    private Estado estadoInicial;

    public Automato() {
        estados = new HashMap<>();
        estadosFinais = new HashMap<>();
        transicoes = new HashMap<>();
    }

    public void setEstado(String identificador) {
        estados.put(identificador, new Estado(identificador));
    }

    public void setEstadoFinal(String identificador) {
        estadosFinais.put(identificador, new Estado(identificador));
    }

    public void setEstadoInicial(String identificador) {
        estadoInicial = new Estado(identificador);
    }

    public void removeEstado(String identificador) {
        if (estados.containsKey(identificador)) {
            estados.remove(identificador);
        }
    }

    /**
     * Uma mesma chave composta pode retornar mais de uma transicao, diferentes
     * entre si.
     *
     * @param chave
     * @return ArrayList<Transicao>
     */
    public ArrayList<Transicao> getTransicao(ChaveComposta chave) {
        ArrayList<Transicao> trans = new ArrayList<>();
        if (transicoes.containsKey(chave)) {
            trans.addAll(transicoes.get(chave));
        }
        return trans;
    }

    public boolean ehEstadoFinal(String idEstado) {
        if (estadosFinais.containsKey(idEstado)) {
            return true;
        }
        return false;
    }
    
    public ArrayList<String> getSimbolosTrasicoes() {
        ArrayList<String> simbolos = new ArrayList<>();
        Iterator<Entry<ChaveComposta, ArrayList<Transicao>>> it = getTransicoes().entrySet().iterator();
        while (it.hasNext()) {
            String simbolo = it.next().getKey().getSimbolo();
            if (!simbolos.contains(simbolo)) {
                simbolos.add(simbolo);
            }
        }
        return simbolos;
    }

    public void setTransicao(String idEstadoInicial, String idEstadoFinal, String simbolo) {
        if (estados.containsKey(idEstadoInicial) && estados.containsKey(idEstadoFinal)) {
            ChaveComposta chave = new ChaveComposta(idEstadoInicial, simbolo);
            if (transicoes.containsKey(chave)) {     // Se ja existe uma chave para essa transicao, adiciona-a a chave
                transicoes.get(chave).add(new Transicao(idEstadoInicial, idEstadoFinal, simbolo));
                System.out.println("");
            } else {    // se nao existe adiciona-a normalmente
                ArrayList<Transicao> novaTransicao = new ArrayList<>();
                novaTransicao.add(new Transicao(idEstadoInicial, idEstadoFinal, simbolo));
                transicoes.put(chave, novaTransicao);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Estado não presente no conjunto de estados.");
        }
    }

    public void removeTransicao(ChaveComposta chave) {
        transicoes.remove(chave);
    }
    
    /**
     * 
     * @param estadoInicial
     * @param delta
     * @param estados
     * @param estadosFinais 
     */
    public void montarAFD(Estado estadoInicial, HashMap<ChaveComposta, ArrayList<Transicao>> delta,
            HashMap<String, Estado> estados, HashMap<String, Estado> estadosFinais) {
        // seta transicoes
        transicoes.putAll(delta);
        // seta estado inicial
        setEstadoInicial(estadoInicial.getID());
        // seta estados finais
        Iterator<Entry<String, Estado>> itEstadosFinais = estadosFinais.entrySet().iterator();
        while (itEstadosFinais.hasNext()) {
            Estado estadoFinal = itEstadosFinais.next().getValue();
            setEstadoFinal(estadoFinal.getID());
        }
        // seta estados
        Iterator<Entry<String, Estado>> itEstados = estados.entrySet().iterator();
        while (itEstados.hasNext()) {
            Estado estado = itEstados.next().getValue();
            setEstado(estado.getID());
        }
    }

    // getters dos atributos
    public HashMap<String, Estado> getEstados() {
        return estados;
    }

    public HashMap<String, Estado> getEstadosFinais() {
        return estadosFinais;
    }

    public HashMap<ChaveComposta, ArrayList<Transicao>> getTransicoes() {
        return transicoes;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }
}
