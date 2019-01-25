package edu.mcw.rgd.dao;

import edu.mcw.rgd.dao.spring.XmlBeanFactoryManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author jdepons
 * @since Jan 18, 2008
 * Singleton factory class that returns a DataSource object.
 * This class requires a data source definition in the spring configuration file.
 * The file path must be set as a system property.
 * <p>
 * ex.<pre>
 *    -Dspring.config=/my/path
 * </pre>
 * The required spring config...
 * <pre>
 * &lt;bean id="DataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close"&gt;
 *		&lt;property name="driverClassName" value="oracle.jdbc.driver.OracleDriver" /&gt;
 *	    &lt;property name="url" value="jdbc:oracle:thin:@taylor.brc.mcw.edu:1521:rgd1" /&gt;
 *		&lt;property name="password" value="asdf" /&gt;
 *      &lt;property name="username" value="asdf" /&gt;
 *	&lt;/bean&gt;
 * </pre>
 */

public class DataSourceFactory {

    private static DataSourceFactory dsf = new DataSourceFactory();


    /**
     * private constructor to ensure only one instance of factory.
     */
    private DataSourceFactory() {
    }


    /**
     * Get the DataSourceFactory instance.
     * @return
     */
    public static DataSourceFactory getInstance() {
        return dsf;
    }


    /**
     * Returns a DataSource object for primary RGD database on that server
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getDataSource() throws Exception{
        return getDataSource(""); // get default data source
    }

    /**
     * Returns a DataSource object for specific domain RGD database on that server
     * @param domain domain name, f.e. 'Carpe', 'Dss', 'Ensembl', 'Umls'
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getDataSource(String domain) throws Exception{
        try {
            if (System.getProperty("spring.config") != null && !System.getProperty("spring.config").equals("")) {
                return (DataSource) (XmlBeanFactoryManager.getInstance().getBean(domain+"DataSource"));
            }else {
                Thread l_thread = Thread.currentThread();
                l_thread.setContextClassLoader(this.getClass().getClassLoader());
                Context initContext = new InitialContext();
                Context envContext  = (Context)initContext.lookup("java:/comp/env");
                String jdniContext = domain.isEmpty() ? "jdbc/rgd2" : "jdbc/"+domain.toLowerCase();
                return (DataSource)envContext.lookup(jdniContext);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return (DataSource) (XmlBeanFactoryManager.getInstance().getBean(domain+"DataSource"));
        }
    }

    /**
     * Returns a DataSource object for Rat CarpeNovo database
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getCarpeNovoDataSource() throws Exception{
        return getDataSource("Carpe");
    }

    /**
     * Returns a DataSource object for Curation database
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getCurationDataSource() throws Exception{
        return getDataSource("Curation");
    }

    /**
     * Returns a DataSource object for skynet database
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getSkynetDataSource() throws Exception{
        return getDataSource("skygen");
    }

    /**
     * Returns a DataSource object for DSS database
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getDssDataSource() throws Exception{
        return getDataSource("Dss");
    }


    // /**
     // * Returns a textMining DataSource object
     // * @return
     // * @throws Exception
     // */
    // public DataSource getTextMiningDataSource() throws Exception{
        //return getDataSource("TextM");
    // }

    /**
     * Returns DataSource object for Ensembl database
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getEnsemblDataSource() throws Exception{
        return getDataSource("Ensembl");
    }
}
