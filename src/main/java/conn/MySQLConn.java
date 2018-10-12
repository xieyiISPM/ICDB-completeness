package conn;

import java.sql.*;

public class MySQLConn {

    private String schemaName;
    private Connection conn;
    private String port="3306";

    private String user ="root";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public MySQLConn(String schemaName, String password){
        this.schemaName = schemaName;
        String url = "jdbc:mysql://localhost" + ":" + this.port + "/" + schemaName + "?" + "user=" + this.user + "&password=" + password + "&maxAllowedPacket=1000000000&autoReconnect=true&useSSL=false" +
                "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Denver";
        try{
            Class.forName(JDBC_DRIVER);
            this.conn = DriverManager.getConnection(url);
            System.out.println("Connection successes!");
        }
        catch(SQLException sqlEx){
            sqlEx.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Connection fails!");
        }
    }

    public String getSchemaName(){
        return schemaName;
    }

    public Connection getConn(){
        return conn;
    }

    public void closeConn(){
        try {
            conn.close();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.print(sqlEx);
        }
    }

}
