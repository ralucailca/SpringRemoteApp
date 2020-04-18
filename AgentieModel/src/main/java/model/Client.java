package model;

import java.io.Serializable;

public class Client extends Entity<Integer> implements Serializable {
    private String nume;
    private String telefon;

    public Client(String nume, String telefon) {
        this.nume = nume;
        this.telefon = telefon;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer integer) {
        super.setId(integer);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id="+this.getId()+
                " nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}
