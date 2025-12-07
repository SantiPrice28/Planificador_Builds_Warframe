package controlador.factory;

import java.sql.Connection;
import java.sql.SQLException;
import controlador.pool.BasicConnectionPool;
import modelo.dao.*;

public class MySQLDAOFactory extends DAOFactory {

    final static String user = "root";
    final static String password = "pricefield28";
    final static String BD = "planificador_warframe"; //Indica aqui la BD 
    final static String IP = "localhost"; //Indica aqui la IP 
    final static String url = "jdbc:mysql://" + IP + ":3306/" + BD;

    static BasicConnectionPool bcp;


    public MySQLDAOFactory() {

        try {
            bcp = BasicConnectionPool.create(url, user, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
        return bcp.getConnection();
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        return bcp.releaseConnection(connection);
    }

    @Override
    public int getSize() {
        return bcp.getSize();
    }
    //add getUser, getURL....

    @Override
    public void shutdown() throws SQLException {
        bcp.shutdown();
    }
   //implementamos los métodos abstractos

    @Override
    public ArmaDAO getArmaDAO() {
        return new ArmaDAO();
    }

    @Override
    public WarframeDAO getWarframeDAO() {
        return new WarframeDAO();
    }
    
    @Override
    public ModDAO getModDAO() {
        return new ModDAO();
    }

    @Override
    public TipoArmaDAO getTipoArmaDAO() {
        return new TipoArmaDAO();
    }

    @Override
    public BuildDAO getBuildDAO() {
        return new BuildDAO();
    }
    
}
