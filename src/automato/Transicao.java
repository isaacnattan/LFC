package automato;

import java.util.Random;

public class Transicao {

    private Estado origem;
    private Estado destino;
    private String simbolo;
    private String identificador;

    public Transicao(int origem, int destino, String simbolo) {
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    // Sobrescricoes necesarias com a utilizacao de estrutura do tipo hash
    @Override
    public int hashCode() {
        return new Random().nextInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.toString().equals(this.toString())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + this.hashCode();
    }

    public static void main(String[] args) {
        //Transicao t = new Transicao(null, null, null);
        //t.toString();
    }
}
