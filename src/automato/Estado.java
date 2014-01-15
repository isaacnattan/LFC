package automato;

import java.util.Objects;

public class Estado {

    private String identificador;

    public Estado(String identificador) {
        this.identificador = identificador;
    }

    public void setID(String identificador) {
    }

    public String getID() {
        return identificador;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.identificador);
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
        final Estado other = (Estado) obj;
        if (!Objects.equals(this.identificador, other.identificador)) {
            return false;
        }
        return true;
    }
}
