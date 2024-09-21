import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class ListJDBCDrivers {
    public static void main(String[] args) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        if (!drivers.hasMoreElements()) {
            System.out.println("No JDBC drivers found in the classpath.");
        } else {
            System.out.println("Available JDBC drivers:");
            int count = 1;
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                System.out.println(count + ". " + driver.getClass().getName());
                count++;
            }
        }
    }
}