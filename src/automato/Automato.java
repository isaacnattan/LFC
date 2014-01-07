package automato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Automato {
    // -Pode haver um conjunto de estados e estados finais
    // -No aplicativo o caracter * tera um significado especial:
    // representara a transicao epsilon

    private HashMap<String, Estado> estados, estadosFinais;
    // Um comjunto de transicoes
    private ArrayList<Transicao> transicoes;
    // Mas somente um estado inicial
    private Estado estadoInicial;

    public Automato() {
        estados = new HashMap<>();
        estadosFinais = new HashMap<>();
        transicoes = new ArrayList<>();
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
     *
     * @param origem
     * @param simbolo
     * @return
     */
    public ArrayList<Transicao> getTransicao(String origem, String simbolo) {
        Iterator<Transicao> it = transicoes.iterator();
        ArrayList<Transicao> transicao = new ArrayList<>();
        while (it.hasNext()) {
            Transicao trans = it.next();
            if (trans.getOrigem().getID().equals(origem)
                    && trans.getSimbolo().equals(simbolo)) {
                transicao.add(trans);
            }
        }
        return transicao;
    }

    public boolean ehEstadoFinal(String idEstado) {
        if (estadosFinais.containsKey(idEstado)) {
            return true;
        }
        return false;
    }

    public void setTransicao(String idEstadoInicial, String idEstadoFinal, String simbolo) {
        if (estados.containsKey(idEstadoInicial) && estados.containsKey(idEstadoFinal)) {
            // Deve adicionar a transicao se os estados referenciados existirem
            transicoes.add(new Transicao(idEstadoInicial, idEstadoFinal, simbolo));
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "");
        }
    }

    public void removeTransicao(Transicao t) {
        for (int i = 0; i < transicoes.size(); i++) {
            if (t.getDestino().equals(transicoes.get(i).getDestino())
                    && t.getOrigem().equals(transicoes.get(i).getOrigem())
                    && t.getSimbolo().equals(transicoes.get(i).getSimbolo())) {
                transicoes.remove(transicoes.get(i));
            }
        }
    }

    // getters dos atributos
    public HashMap<String, Estado> getEstados() {
        return estados;
    }

    public HashMap<String, Estado> getEstadosFinais() {
        return estadosFinais;
    }

    public ArrayList<Transicao> getTransicoes() {
        return transicoes;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }
}
