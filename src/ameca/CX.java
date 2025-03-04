package ameca;

/*
 * @author manu
 */

import java.sql.Connection;
import java.sql.SQLException;

import org.mariadb.jdbc.MariaDbPoolDataSource;


public class CX {

    static MariaDbPoolDataSource pool;
    public static Boolean init = false;

    public CX() {
        try { Class.forName("org.mariadb.jdbc.Driver"); }
        catch (ClassNotFoundException e) { System.out.printf("exception al registrar el paquete mariadb: %s%n", e.toString()); }
        System.out.println("Cx. cargaClase.\n--");
    }


    public static Connection getCx_pool() {
        if (!CX.init)  //poner en init de tomcat
        {
            init = true;
            pool = new MariaDbPoolDataSource("jdbc:mariadb://localhost:3306/dbAmeca");
            try {
                pool.setUser("manu");
                pool.setPassword("1234");
                pool.setMaxPoolSize(9);
                pool.setMinPoolSize(3);
                pool.setStaticGlobal(true);
                pool.setMaxIdleTime(900);  // 15 mins inactiva => eliminada.
                }
            catch (SQLException e) { System.out.println("exception al crear el pool: " + e.toString());  }
        }

        try { return pool.getConnection(); }
        catch (SQLException e) { System.out.println("exception en get cx del pool: " + e.toString()); }

        return null;
    }


}