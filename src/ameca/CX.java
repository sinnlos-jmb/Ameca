package ameca;

/**
 * @author manu
 *     
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to           
 * Window>Preferences>Java>Code Generation.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import org.apache.commons.dbcp.BasicDataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.DataSource;




public class CX {

    private static PoolProperties p=null;
    private static DataSource datasource = null;  
    
    public static Boolean init=false;

    public CX()
        {
            System.out.println("Cx. cargaClase.\n--");
        }


    public static Connection getCx() throws SQLException
	{
            //System.out.println("chequeo cx xa ver si bd is running ");
            Connection conn= null;

            try{
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+HTML.dbase,"manu", "1234"); //properties
                    }
            catch(Exception e)
                    {System.out.println("exception en nueva cx: "+e.toString());}

            return conn;
	}


        
    public static Connection getCx_pool() 
        {
            
        if (!CX.init)  //poner en init de tomcat
           {init=true;
            p = new PoolProperties();
            p.setUrl("jdbc:mysql://localhost:3306/"+HTML.dbase);
            p.setDriverClassName("com.mysql.jdbc.Driver");
            p.setUsername( "vaio3" );
            p.setPassword( "1234" );
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(15);
            p.setMaxIdle(10);
            p.setMinIdle(4);
            p.setInitialSize(5);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors(
              "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
              "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            
            datasource = new org.apache.tomcat.jdbc.pool.DataSource();
            datasource.setPoolProperties(p);
            }            
            
            
            Connection conn= null;
            try{
                conn= datasource.getConnection("manu", "1234");
                }
            catch(Exception e)
                {System.out.println("exception en nueva cx: "+e.toString());}

            return conn;
        }



}
