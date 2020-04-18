package repository;


import model.IdRezervare;
import model.Rezervare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RezervareJdbcRepository implements IRepository<IdRezervare, Rezervare> {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RezervareJdbcRepository(Properties props){
        logger.info("Initializing RezervareJdbcRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }



    @Override
    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Rezervari")) {
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
    public void save(Rezervare entity) {
        logger.traceEntry("saving rezervare {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Rezervari values (?,?,?)")){
            preStmt.setInt(1,entity.getId().getIdExcursie());
            preStmt.setInt(2,entity.getId().getIdClient());
            preStmt.setInt(3,entity.getBilete());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(IdRezervare id) {
        logger.traceEntry("deleting rezervare with {} {}",id.getIdExcursie(),id.getIdClient());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Rezervari where idExcursie=? and idClient=?")){
            preStmt.setInt(1,id.getIdExcursie());
            preStmt.setInt(2,id.getIdClient());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(IdRezervare id, Rezervare entity) {
        logger.traceEntry("update rezervare with {} {}", id.getIdExcursie(), id.getIdClient());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("UPDATE Rezervari\n" +
                "SET bilete=?" +
                "WHERE idExcursie=? and idClient=?")){
            preStmt.setInt(1,entity.getBilete());
            preStmt.setInt(2,id.getIdExcursie());
            preStmt.setInt(3,id.getIdClient());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Rezervare findOne(IdRezervare id) {
        logger.traceEntry("finding rezervare with id {} {}", id.getIdExcursie(),id.getIdClient());
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervari where idExcursie=? and idClient=?")){
            preStmt.setInt(1, id.getIdExcursie());
            preStmt.setInt(2, id.getIdClient());
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int bilete=result.getInt("bilete");
                    Rezervare rezervare=new Rezervare(bilete);
                    rezervare.setId(id);
                    logger.traceExit(rezervare);
                    return rezervare;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No rezervare found with id "+ id.getIdExcursie()+ " "+ id.getIdClient());

        return null;
    }

    @Override
    public Iterable<Rezervare> findAll() {
        Connection con=dbUtils.getConnection();
        List<Rezervare> Rezervari=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervari ")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int bilete=result.getInt("bilete");
                    IdRezervare id=new IdRezervare(result.getInt("idExcursie"),result.getInt("idClient"));
                    Rezervare rezervare=new Rezervare(bilete);
                    rezervare.setId(id);
                    Rezervari.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(Rezervari);
        return Rezervari;
    }
}
