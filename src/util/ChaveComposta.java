package util;

import java.util.Objects;

/**
 * @author Isaac_Nattan
 */
public class ChaveComposta {
    private String origem;
    private String simbolo;

    public ChaveComposta(String origem, String simbolo) {
        this.origem = origem;
        this.simbolo = simbolo;
    }

    public String getOrigem() {
        return origem;
    }

    public String getSimbolo() {
        return simbolo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.origem);
        hash = 17 * hash + Objects.hashCode(this.simbolo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChaveComposta other = (ChaveComposta) obj;
        if (!Objects.equals(this.origem, other.origem)) {
            return false;
        }
        if (!Objects.equals(this.simbolo, other.simbolo)) {
            return false;
        }
        return true;
    }
}
