package repository;


import model.Excursie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExcursieJdbcRepository implements IExcursieRepository<Integer> {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ExcursieJdbcRepository(Properties props){
        logger.info("Initializing ExcursieJdbcRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }



    @Override
    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Excursii")) {
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
    public void save(Excursie entity) {
        logger.traceEntry("saving excursie {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Excursii values (?,?,?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getObiectiv());
            preStmt.setString(3,entity.getFirma());
            preStmt.setInt(4,entity.getOraPlecare());
            preStmt.setFloat(5,entity.getPret());
            preStmt.setInt(6,entity.getLocuri());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting excursie with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Excursii where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Excursie entity) {
        logger.traceEntry("update excursie with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("UPDATE Excursii\n" +
                "SET obiectiv = ?, firma = ?, oraPlecare = ?, pret = ?, locuri = ? \n" +
                "WHERE id = ?")){

            preStmt.setString(1,entity.getObiectiv());
            preStmt.setString(2,entity.getFirma());
            preStmt.setInt(3,entity.getOraPlecare());
            preStmt.setFloat(4,entity.getPret());
            preStmt.setInt(5,entity.getLocuri());
            preStmt.setInt(6,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Excursie findOne(Integer integer) {
        logger.traceEntry("finding excursie with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Excursii where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String obiectiv = result.getString("obiectiv");
                    String firma = result.getString("firma");
                    Integer ora = result.getInt("oraPlecare");
                    Float pret = result.getFloat("pret");
                    Integer locuri = result.getInt("locuri");
                    Excursie excursie=new Excursie(obiectiv, firma, ora, pret, locuri);
                    excursie.setId(id);
                    logger.traceExit(excursie);
                    return excursie;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No excursie found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Excursie> findAll() {
        Connection con=dbUtils.getConnection();
        List<Excursie> excursii=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Excursii")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String obiectiv = result.getString("obiectiv");
                    String firma = result.getString("firma");
                    Integer ora = result.getInt("oraPlecare");
                    Float pret = result.getFloat("pret");
                    Integer locuri = result.getInt("locuri");
                    Excursie excursie=new Excursie(obiectiv, firma, ora, pret, locuri);
                    excursie.setId(id);
                    excursii.add(excursie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(excursii);
        return excursii;
    }

    @Override
    public Iterable<Excursie> findInterval(String obiectiv, Integer ora1, Integer ora2) {
        Connection con=dbUtils.getConnection();
        List<Excursie> excursii=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Excursii where obiectiv=? and oraPlecare between ? and ?")) {
            preStmt.setString(1, obiectiv);
            preStmt.setInt(2, ora1);
            preStmt.setInt(3, ora2);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String obiectivr = result.getString("obiectiv");
                    String firma = result.getString("firma");
                    Integer ora = result.getInt("oraPlecare");
                    Float pret = result.getFloat("pret");
                    Integer locuri = result.getInt("locuri");
                    Excursie excursie=new Excursie(obiectivr, firma, ora, pret, locuri);
                    excursie.setId(id);
                    excursii.add(excursie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(excursii);
        return excursii;
    }
}
