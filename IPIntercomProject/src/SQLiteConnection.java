import java.sql.*;
import java.lang.Class;


public class SQLiteConnection {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("SQLite JDBC sürücüsü bulunamadı.", ex);
        }
    }

    private static final String URL = "jdbc:sqlite:messages.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }


    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Bağlantı kapatıldı.");
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı kapatılamadı: " + e.getMessage());
        }
    }

    public static void deleteTables(String tableName ) {
        String sql = "DROP TABLE IF EXISTS" + tableName;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Table deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertRecord(String tableName, String column1, String value1, String column2, String value2) {
        String sql = "INSERT INTO " + tableName + " (" + column1 + ", " + column2 + ") VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, value1);
            pstmt.setString(2, value2);
            pstmt.executeUpdate();

            System.out.println("Record has been inserted into " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createNewTables() {
        String messageTable = "CREATE TABLE IF NOT EXISTS message (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    content TEXT NOT NULL\n"
                + ");";

        String subjectTable = "CREATE TABLE IF NOT EXISTS subject (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    title TEXT NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(messageTable);
            stmt.execute(subjectTable);
            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
       // createNewTables();

        Connection conn = SQLiteConnection.connect();
        insertRecord("message","subject","zil","message","arıza");


        SQLiteConnection.close(conn);

       // deleteTables();
    }
}


