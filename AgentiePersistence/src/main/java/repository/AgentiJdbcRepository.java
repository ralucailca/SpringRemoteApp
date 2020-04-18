package repository;


import model.Agent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AgentiJdbcRepository implements IAgentRepository<Integer> {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public AgentiJdbcRepository(Properties props){
        logger.info("Initializing AgentJdbcRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }



    @Override
    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Agenti")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public void save(Agent entity) {
        logger.traceEntry("saving agent {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Agenti values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUser());
            preStmt.setString(3,entity.getPassword());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting agent with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Agenti where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Agent entity) {
        logger.traceEntry("update agent with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("UPDATE Agenti\n" +
                "SET user = ?, password = ?" +
                "WHERE id = ?")){
            preStmt.setString(1,entity.getUser());
            preStmt.setString(2,entity.getPassword());
            preStmt.setInt(3,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Agent findOne(Integer integer) {
        logger.traceEntry("finding agent with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Agenti where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String user = result.getString("user");
                    String pass = result.getString("password");
                    Agent agent=new Agent(user, pass);
                    agent.setId(id);
                    logger.traceExit(agent);
                    return agent;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No agent found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Agent> findAll() {
        Connection con=dbUtils.getConnection();
        List<Agent> agenti=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Agenti")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String user = result.getString("user");
                    String pass = result.getString("password");
                    Agent agent=new Agent(user, pass);
                    agent.setId(id);
                    agenti.add(agent);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(agenti);
        return agenti;
    }

    @Override
    public Agent findUserPass(String user, String pass) {
        logger.traceEntry("finding agent with user "+user+" and password"+pass);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Agenti where user=? and password=?")){
            preStmt.setString(1,user);
            preStmt.setString(2,pass);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    Agent agent=new Agent(user, pass);
                    agent.setId(id);
                    logger.traceExit(agent);
                    return agent;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No agent found with id user "+user+" and password"+pass);
        return null;
    }
}
