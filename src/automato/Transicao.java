package automato;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Transicao {

    private Estado origem;
    private Estado destino;
    private String simbolo;
    //private String identificador;

    public Transicao(String origem, String destino, String simbolo) {
        this.origem = new Estado(origem);
        this.destino = new Estado(destino);
        this.simbolo = simbolo;
    }

    public Estado getOrigem() {
        return origem;
    }

    public void setOrigem(Estado origem) {
        this.origem = origem;
    }

    public Estado getDestino() {
        return destino;
    }

    public void setDestino(Estado destino) {
        this.destino = destino;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    /*public String getIdentificador() {
     return identificador;
     }

     public void setIdentificador(String identificador) {
     this.identificador = identificador;
     }*/
    // Sobrescricoes necesarias com a utilizacao de estrutura do tipo hash
    @Override
    public int hashCode() {
        return getRandomPrimo();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.toString().equals(this.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gera uma sequência de 78700 números primos e escolhe
     * pseudo-aleatoriamente um deles.
     *
     * @return int
     */
    private int getRandomPrimo() {
        ArrayList<Integer> numerosPrimos = new ArrayList<>();
        numerosPrimos.add(2);
        numerosPrimos.add(3);
        for (int i = 5; i < 1000000; i += 2) {     // gera numeros impares
            if (primo(i)) {
                numerosPrimos.add(i);
            }
        }
        // Embaralha os numeros 
        Collections.shuffle(numerosPrimos);
        // e retorna sempre a posicao 37
        return numerosPrimos.get(new Random().nextInt(77999));
    }

    // Complexidade linear O(n). o.O'
    private boolean primo(int num) {
        // fonte: http://www.niee.ufrgs.br/eventos/CBCOMP/2004/pdf/Algoritmos/t170100018_3.pdf
        double k = Math.sqrt((double) num);
        for (int i = 3; i < (int) k; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + this.hashCode();
    }

    public static void main(String[] args) {
        Transicao t = new Transicao(null, null, null);
        t.testeDesempenho();
    }
    
    private boolean existeValoresIguais(ArrayList<Integer> lista) {
        Collections.sort(lista);
        for (int i = 0; i < lista.size() - 2; i++) {    // n-1 comparacoes
            if (lista.get(i + 1) == lista.get(i)) {
                return true;
            }
        }
        return false;
    }
    
    private void testeDesempenho() {
        for (int c=0; c<10; c++) {
            System.out.print("\n" + "Lista" + c + " :");
            ArrayList<Integer> lista = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                lista.add(getRandomPrimo());
                if (i % 10 == 0) {
                    System.out.println();
                }
                System.out.print(lista.get(i) + ", ");
            }
            System.out.println("");
            if (existeValoresIguais(lista)) {
                System.out.println("A lista contem valores iguais. x(");
            } else {
                System.out.println("A lista nao contem valores iguais. xD");
            }
        }
    }
}

/**
 * Ps.: Realizei o teste de desempenho várias vezes. O teste consiste em gerar uma lista
 * de 1000 números aletórios (um autômato de 1000 transições) 10x de forma que não deve
 * haver elementos repetidos dentro dessas listas e todos os elementos sejam números
 * primos. Em várias execuções nenhuma lista constou elementos iguais. Parece consistente
 * para o meu propósito.
 */