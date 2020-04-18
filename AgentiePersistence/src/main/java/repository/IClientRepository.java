package repository;

import model.Client;

public interface IClientRepository<ID> extends IRepository<ID, Client> {
    ID getIdMax();
}
