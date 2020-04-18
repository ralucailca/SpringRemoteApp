package model;

import java.io.Serializable;

public class Rezervare extends Entity<IdRezervare> implements Serializable {
    private int bilete;

    public Rezervare(int bilete) {
        this.bilete = bilete;
    }

    public int getBilete() {
        return bilete;
    }

    public void setBilete(int bilete) {
        this.bilete = bilete;
    }

    @Override
    public IdRezervare getId() {
        return super.getId();
    }

    @Override
    public void setId(IdRezervare idRezervare) {
        super.setId(idRezervare);
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "excursie="+this.getId().getIdExcursie()+
                " client="+this.getId().getIdClient()+
                " bilete=" + bilete +
                '}';
    }
}
