package testes;

import automato.Automato;
import automato.Estado;
import automato.Transicao;

public class ExemploAFD {

    public static void main(String args[]) {
        Automato meuAutomato = new Automato();
        String cadeia = "abababaabaa";
        Transicao trans;
        String origem = "0";
        Estado destino;
        //criação os estados
        meuAutomato.setEstado("0");
        meuAutomato.setEstado("1");
        //definição do estado Inicial
        meuAutomato.setEstadoInicial("0");
        //definição do estado Final
        meuAutomato.setEstadoFinal("0");
        //criação das Transições
        meuAutomato.setTransicao("0", "0", "a");
        meuAutomato.setTransicao("0", "1", "b");
        meuAutomato.setTransicao("1", "1", "a");
        meuAutomato.setTransicao("1", "0", "b");
        int i = 0;
        System.out.println("\nAFD reconhecedor"
                + " de palavras com número par de b's");
        System.out.println("Verifica a entrada "
                + cadeia + "\n");
        while (i < cadeia.length()) {
            // O método getTransicao retorna
            // a transição que tem como origem e
            // símbolo os parametros passados.
            trans = meuAutomato.getTransicao(origem, "" + cadeia.charAt(i)).get(0);
            // O método getEstadoDestino
            // retorna o Estado de Destino da
            // transição trans.
            destino = trans.getDestino();
            // O método retornaID retorna o
            // identificador do Estado destino.
            origem = destino.getID();
            System.out.println("Leu o símbolo "
                    + cadeia.charAt(i) + " foi pro estado "
                    + origem);
            i++;
        }// Fim while
        // O método eEstadoFinal retorna true se
        // origem for o identificador de um
        // estado final.
        if (meuAutomato.ehEstadoFinal(origem)) {
            System.out.println("\nA entrada "
                    + cadeia + " foi aceita!!!\n");
        } else {
            System.out.println("\nA entrada "
                    + cadeia + " foi rejeitada!!!\n");
        }
    }// Fim main
}// Fim ExemploAFD
