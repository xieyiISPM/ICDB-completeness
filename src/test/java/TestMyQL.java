import conn.MySQLConn;
import dbOp.DBQuery;

import java.sql.Connection;
import java.sql.SQLException;

public class TestMyQL {
    public static void main(String[] args){
        String schemaName = "employees";
        String password = "113071";
        try{
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();
            DBQuery dbQuery = new DBQuery(schemaName, conn);
            dbQuery.useDB();
            String sql="SELECT * FROM " +schemaName + " LIMIT 10";
            dbQuery.outputTuple(dbQuery.queryDB(sql));

            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }
}
