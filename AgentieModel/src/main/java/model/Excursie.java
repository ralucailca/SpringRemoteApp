package model;

import java.io.Serializable;

public class Excursie extends Entity<Integer> implements Serializable {
    private String obiectiv;
    private String firma;
    private int oraPlecare;
    private float pret;
    private int locuri;

    public Excursie(String obiectiv, String firma, int oraPlecare, float pret, int locuri) {
        this.obiectiv = obiectiv;
        this.firma = firma;
        this.oraPlecare = oraPlecare;
        this.pret = pret;
        this.locuri = locuri;
    }

    public String getObiectiv() {
        return obiectiv;
    }

    public void setObiectiv(String obiectiv) {
        this.obiectiv = obiectiv;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public int getOraPlecare() {
        return oraPlecare;
    }

    public void setOraPlecare(int oraPlecare) {
        this.oraPlecare = oraPlecare;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    public int getLocuri() {
        return locuri;
    }

    public void setLocuri(int locuri) {
        this.locuri = locuri;
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
        return "Excursie{" +
                "id="+this.getId()+
                " obiectiv='" + obiectiv + '\'' +
                ", firma='" + firma + '\'' +
                ", oraPlecare=" + oraPlecare +
                ", pret=" + pret +
                ", locuri=" + locuri +
                '}';
    }
}
