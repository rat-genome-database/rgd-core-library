package edu.mcw.rgd.dao;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 28, 2007
 * Time: 9:16:41 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interface for an RGD Data Access Object
 */
public interface DAO {

    /**
     * Returns a database connection to the back end data store
     * @return
     * @throws Exception
     */
    public Connection getConnection() throws Exception;
}
