package repository;


import model.Agent;

public interface IAgentRepository<ID> extends IRepository<ID, Agent> {
    Agent findUserPass(String user, String pass);
}
