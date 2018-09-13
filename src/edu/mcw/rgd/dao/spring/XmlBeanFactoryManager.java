package edu.mcw.rgd.dao.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 13, 2008
 * Time: 8:51:29 AM
 * <p>
 * create a bean
 * </p>
 */
public class XmlBeanFactoryManager {

    private DefaultListableBeanFactory xbf= null;
    private static XmlBeanFactoryManager xbfm= new XmlBeanFactoryManager();
    protected final Log logger = LogFactory.getLog(this.getClass());

    private XmlBeanFactoryManager() {

    }

    public static XmlBeanFactoryManager getInstance() {
        return xbfm;
    }

    public synchronized DefaultListableBeanFactory getXmlBeanFactory() {

        if (xbf == null) {
            String path = System.getProperty("spring.config");
            logger.info("SPRING_CONFIG: "+(path==null?"(null)":path));

            if (path == null) {
                
                String catalinaPath = System.getProperty("catalina.home");
                logger.info("CATALINA_HOME: "+(catalinaPath==null?"(null)":catalinaPath));

                if( catalinaPath == null )
                    path = "properties/AppConfigure.xml";
                else
                    path = catalinaPath + "/properties/AppConfigure.xml";
            }

            logger.info("creating XmlBeanFactory from "+path);

            xbf = new DefaultListableBeanFactory();
            new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(new FileSystemResource(path));
        }

        return xbf;
    }

    public Object getBean(String beanName) {
        return getXmlBeanFactory().getBean(beanName);
    }
}
