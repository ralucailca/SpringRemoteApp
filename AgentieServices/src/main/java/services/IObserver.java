package services;

import model.Excursie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IObserver extends Remote {
    void update(List<Excursie> excursii) throws RemoteException;
}
