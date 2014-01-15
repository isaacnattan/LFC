package testes;

import automato.Automato;
import reconhecedorCadeias.ReconhecedorCadeia;

public class ExemploAFD {

    public static void main(String args[]) {
        Automato meuAutomato = new Automato();
        String cadeia = "ababaababaa";
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
        new ReconhecedorCadeia(meuAutomato, cadeia);
    }// Fim main
}// Fim ExemploAFD
