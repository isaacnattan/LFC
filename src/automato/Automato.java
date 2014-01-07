package automato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class Automato {
    // Pode haver um conjunto de estados e estados finais

    private HashMap<String, Estado> estados, estadosFinais;
    // Um comjunto de transicoes
    private HashSet<Transicao> transicoes;
    // Mas somente um estado inicial
    private Estado estadoInicial;

    public Automato() {
        estados = new HashMap<>();
        estadosFinais = new HashMap<>();
        transicoes = new HashSet<>();
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
        } else if (estadosFinais.containsKey(identificador)) {
            if (mensagemDeConfirmacao(
                    "Você está prestes a excluir um estado final.")
                    == JOptionPane.YES_OPTION) {
                estadosFinais.remove(identificador);
            }
        } else {
            if (estadoInicial.getID().equals(identificador)) {
                if (mensagemDeConfirmacao(
                        "Você está prestes a excluir o estado inicial.")
                        == JOptionPane.YES_OPTION) {
                    String novoIdentificador = javax.swing.JOptionPane.showInputDialog(null,
                            "Você deve escolher outro estado inicial."
                            + "\nInsira um identificador válido: ");
                    setEstadoInicial(novoIdentificador);
                }
            }
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

    public void removeTransicao(ArrayList<Transicao> t) {
        /*Iterator<Transicao> it = transicoes.iterator();
        while (it.hasNext()) {
            Transicao t1 = it.next();
            if (t.getOrigem().equals(t1.getOrigem())
                    && t.getSimbolo().equals(t1.getSimbolo())
                    && t.getDestino().equals(t1.getDestino())) {
                it.remove();
            }
        }*/
        transicoes.removeAll(t);
    }

    // metodos auxiliares
    private int mensagemDeConfirmacao(String mensagem) {
        Object[] options = {"Sim", "Não"};
        int i;
        i = JOptionPane.showOptionDialog(null,
                mensagem
                + "\nDeseja prosseguir?", "Questão",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);
        return i;
    }

    // getters dos atributos
    public HashMap<String, Estado> getEstados() {
        return estados;
    }

    public HashMap<String, Estado> getEstadosFinais() {
        return estadosFinais;
    }

    public HashSet<Transicao> getTransicoes() {
        return transicoes;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }
}
