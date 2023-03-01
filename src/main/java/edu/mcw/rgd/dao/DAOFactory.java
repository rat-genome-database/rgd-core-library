package edu.mcw.rgd.dao;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 28, 2007
 * Time: 9:26:27 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a data access object based on the key passed in.  The object key is the name of the DAO class.
 * All objects must be in the edu.mcw.rgd.dao.impl package.
 *
 */
public class DAOFactory {

    private static DAOFactory factory = new DAOFactory();

    private DAOFactory() {

    }

    public static DAOFactory getInstance() {
        return factory;
    }

    /**
     *  Returns a data access object based on the key passed in.  Key must be the class name.  This
     *  method will attempt find the class in edu.mcw.rgd.dao.impl and return an instance.
     * @param key
     * @return
     * @throws Exception
     */
    public DAO getDAO(String key) throws Exception{
        return (DAO) Class.forName("edu.mcw.rgd.dao.impl." + key).newInstance();
    }
    
}
