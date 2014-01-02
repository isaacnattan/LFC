package automato;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class Automato {
    // Pode haver um conjunto de estados e estados finais
    private HashMap<Integer, Estado> estados, estadosFinais;
    // Um comjunto de transicoes
    private HashSet<Transicao> transicoes;
    // Mas somente um estado inicial
    private Estado estadoInicial;

    public Automato() {
        estados = new HashMap<>();
        estadosFinais = new HashMap<>();
        transicoes = new HashSet<>();
    }

    public void setEstado(int identificador) {
        estados.put(identificador, new Estado(identificador));
    }

    public void setEstadoFinal(int identificador) {
        estadosFinais.put(identificador, new Estado(identificador));
    }

    public void setEstadoInicial(int identificador) {
        estadoInicial = new Estado(identificador);
    }

    public void removeEstado(int identificador) {
        if (estados.containsKey(identificador)) {
            estados.remove(identificador);
        } else if (estadosFinais.containsKey(identificador)) {
            if (mensagemDeConfirmacao(
                    "Você está prestes a excluir um estado final.")
                    == JOptionPane.YES_OPTION) {
                estadosFinais.remove(identificador);
            }
        } else {
            if (estadoInicial.getID() == identificador) {
                if (mensagemDeConfirmacao(
                        "Você está prestes a excluir o estado inicial.")
                        == JOptionPane.YES_OPTION) {
                    int novoIdentificador = Integer.parseInt(javax.swing.JOptionPane.showInputDialog(null,
                            "Você deve escolher outro estado inicial."
                            + "\nInsira um identificador válido: "));
                    setEstadoInicial(novoIdentificador);
                }
            }
        }
    }

    public Transicao getTransicao(int origem, String simbolo) {
        Iterator<Transicao> it = transicoes.iterator();
        while (it.hasNext()) {
            Transicao trans = it.next();
            if (trans.getOrigem().getID() == origem 
                    && trans.getSimbolo().equals(simbolo)) {
                return trans;
            }
        }
        return null;
    }

    public boolean ehEstadoFinal(int idEstado) {
        if (estadosFinais.containsKey(idEstado)) {
            return true;
        }
        return false;
    }

    public void setTransicao(int idEstadoInicial, int idEstadoFinal, String simbolo) {
        if (estados.containsKey(idEstadoInicial) && estados.containsKey(idEstadoFinal)) {
            // Deve adicionar a transicao se os estados referenciados existirem
            transicoes.add(new Transicao(idEstadoInicial, idEstadoFinal, simbolo));
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "");
        }
    }

    public void removeTransicao() {
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
    public HashMap<Integer, Estado> getEstados() {
        return estados;
    }

    public HashMap<Integer, Estado> getEstadosFinais() {
        return estadosFinais;
    }

    public HashSet<Transicao> getTransicoes() {
        return transicoes;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }
    
    // testes
    public static void main(String[] args) {
        Automato meuAutomato = new Automato();
        String cadeia = "abababb";
        Transicao trans;
        int origem = 0;
        Estado destino;
        //criação os estados
        meuAutomato.setEstado(0);
        meuAutomato.setEstado(1);
        //definição do estado Inicial
        meuAutomato.setEstadoInicial(0);
        //definição do estado Final
        meuAutomato.setEstadoFinal(0);
        //criação das Transições
        meuAutomato.setTransicao(0, 0, "a");
        meuAutomato.setTransicao(0, 1, "b");
        meuAutomato.setTransicao(1, 1, "a");
        meuAutomato.setTransicao(1, 0, "b");
        meuAutomato.getTransicao(0, "a");
    }
}
