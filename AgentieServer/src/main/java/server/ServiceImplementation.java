package server;

import model.*;
import repository.AgentiJdbcRepository;
import repository.ClientJdbcRepository;
import repository.ExcursieJdbcRepository;
import repository.RezervareJdbcRepository;
import services.AgentieException;
import services.IObserver;
import services.IService;
import validator.ClientValidator;
import validator.RezervareValidator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IService {
    private ClientJdbcRepository clientJdbcRepository;
    private RezervareJdbcRepository rezervareJdbcRepository;
    private ExcursieJdbcRepository excursieJdbcRepository;
    private AgentiJdbcRepository agentiJdbcRepository;
    private ClientValidator clientValidator;
    private RezervareValidator rezervareValidator;
    private Map<Integer,IObserver> loggedAgenti;

    public ServiceImplementation(ClientJdbcRepository clientJdbcRepository, RezervareJdbcRepository rezervareJdbcRepository,
                                 ExcursieJdbcRepository excursieJdbcRepository, AgentiJdbcRepository agentiJdbcRepository,
                                 ClientValidator clientValidator, RezervareValidator rezervareValidator) {
        this.clientJdbcRepository = clientJdbcRepository;
        this.rezervareJdbcRepository = rezervareJdbcRepository;
        this.excursieJdbcRepository = excursieJdbcRepository;
        this.agentiJdbcRepository = agentiJdbcRepository;
        this.clientValidator = clientValidator;
        this.rezervareValidator = rezervareValidator;
        loggedAgenti = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(Agent agent, IObserver observer) {
        Agent a = agentiJdbcRepository.findUserPass(agent.getUser(), agent.getPassword());
        if (a!=null){
            if(loggedAgenti.get(a.getId())!=null)
                throw new AgentieException("User already logged in.");
            loggedAgenti.put(a.getId(), observer);
        }else
            throw new AgentieException("Authentication failed.");
    }

    @Override
    public synchronized void logout(Agent agent, IObserver observer) {
        Agent a = agentiJdbcRepository.findUserPass(agent.getUser(), agent.getPassword());
        IObserver localClient=loggedAgenti.remove(a.getId());
        if (localClient==null)
            throw new AgentieException("User "+a.getId()+" is not logged in.");
    }

    @Override
    public synchronized Iterable<Excursie> cautaObiectivInterval(String obv, Integer ora1, Integer ora2) {
        return excursieJdbcRepository.findInterval(obv, ora1, ora2);
    }

    @Override
    public synchronized Iterable<Excursie> findAll() {
        return excursieJdbcRepository.findAll();
    }

    @Override
    public synchronized void rezerva(Excursie excursie, Client client, Integer bilete) {
        clientValidator.validate(client);
        //if(clientJdbcRepository.findOne(client.getId())==null)
        //    clientJdbcRepository.save(client);
        client.setId(clientJdbcRepository.getIdMax()+1);
        clientJdbcRepository.save(client);
        IdRezervare idRezervare=new IdRezervare(excursie.getId(),client.getId());
        Rezervare rezervare=new Rezervare(bilete);
        rezervareValidator.validate(rezervare);
        rezervare.setId(idRezervare);
        Integer locuriDisponibile=excursie.getLocuri();
        if(locuriDisponibile-bilete<0)
            throw new AgentieException("Nu mai exista suficiente locuri pentru a finaliza rezervarea!");
        excursie.setLocuri(locuriDisponibile-bilete);
        excursieJdbcRepository.update(excursie.getId(),excursie);
        rezervareJdbcRepository.save(rezervare);
        notifyUpdateExcursii();
    }

    private final int defaultThreadsNo=5;

    private void notifyUpdateExcursii() {
        System.out.println("Update excursie dupa rezervare ");

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver client : loggedAgenti.values()){
            if (client!=null)
                executor.execute(() -> {
                    try {
                        List<Excursie> excursii = new ArrayList<>();
                        excursieJdbcRepository.findAll().forEach(x->excursii.add(x));
                        client.update(excursii);
                    } catch (AgentieException | RemoteException e) {
                        System.err.println("Error update " + e);
                    }
                });
        }

        executor.shutdown();
    }
}
