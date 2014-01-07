package automato;

public class Transicao {

    private Estado origem;
    private Estado destino;
    private String simbolo;

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
}