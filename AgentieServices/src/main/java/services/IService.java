package services;

import model.Agent;
import model.Client;
import model.Excursie;

public interface IService {
    void login(Agent agent, IObserver observer);
    void logout(Agent agent, IObserver observer);
    Iterable<Excursie> cautaObiectivInterval(String obv, Integer ora1, Integer ora2);
    Iterable<Excursie> findAll();
    void rezerva(Excursie excursie, Client client, Integer bilete);
}
