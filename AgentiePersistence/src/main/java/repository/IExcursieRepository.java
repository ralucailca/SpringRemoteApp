package repository;

import model.Excursie;

public interface IExcursieRepository<ID> extends IRepository<ID, Excursie> {
    Iterable<Excursie> findInterval(String obiectiv, Integer ora1, Integer ora2);
}
