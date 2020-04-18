package repository;

import model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientJdbcRepository implements IClientRepository<Integer> {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ClientJdbcRepository(Properties props){
        logger.info("Initializing ClientJdbcRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }



    @Override
    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Clienti")) {
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
    public void save(Client entity) {
        logger.traceEntry("saving client {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Clienti values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setString(3,entity.getTelefon());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting client with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Clienti where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Client entity) {
        logger.traceEntry("update client with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("UPDATE Clienti\n" +
                "SET nume = ?, telefon = ?" +
                "WHERE id = ?")){
            preStmt.setString(1,entity.getNume());
            preStmt.setString(2,entity.getTelefon());
            preStmt.setInt(3,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Client findOne(Integer integer) {
        logger.traceEntry("finding client with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Clienti where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String telefon = result.getString("telefon");
                    Client client=new Client(nume, telefon);
                    client.setId(id);
                    logger.traceExit(client);
                    return client;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No client found with id {}", integer);

        return null;
    }



    @Override
    public Iterable<Client> findAll() {
        Connection con=dbUtils.getConnection();
        List<Client> clienti=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Clienti")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String telefon = result.getString("telefon");
                    Client client=new Client(nume, telefon);
                    client.setId(id);
                    clienti.add(client);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(clienti);
        return clienti;
    }

    @Override
    public Integer getIdMax() {
        System.out.println("intr");
        logger.traceEntry("finding max id ");
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select MAX(id) as id from Clienti")){
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    logger.traceExit(id);
                    return id;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No found max id");

        return null;
    }
}
