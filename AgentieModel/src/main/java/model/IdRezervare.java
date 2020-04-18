package model;

import java.io.Serializable;
import java.util.Objects;

public class IdRezervare implements Serializable {
    private int idExcursie;
    private int idClient;

    public IdRezervare(int idExcursie, int idClient) {
        this.idExcursie = idExcursie;
        this.idClient = idClient;
    }

    public int getIdExcursie() {
        return idExcursie;
    }

    public void setIdExcursie(int idExcursie) {
        this.idExcursie = idExcursie;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdRezervare that = (IdRezervare) o;
        return idExcursie == that.idExcursie &&
                idClient == that.idClient;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExcursie, idClient);
    }

    @Override
    public String toString() {
        return "IdRezervare{" +
                "idExcursie=" + idExcursie +
                ", idClient=" + idClient +
                '}';
    }
}
