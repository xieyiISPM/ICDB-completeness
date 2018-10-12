import conn.MySQLConn;
import dbOp.DBPrepare;
import dbOp.DBQuery;

import java.sql.Connection;
import java.sql.SQLException;

public class TestMyQL {
    public static void main(String[] args){
        String schemaName = "employees";
        String password = "113071";
        String fileName = "data/employees_sorted.csv";
        try{
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();
            DBQuery dbQuery = new DBQuery(schemaName, conn);
            dbQuery.useDB();
           // String sql="SELECT * FROM " +schemaName + " ORDER BY hire_date, emp_no;";
            //dbQuery.outputTuple(dbQuery.queryDB(sql));
            //System.out.println("Creating csv file...");
            //DBPrepare dbPrepare = new DBPrepare();
            //dbPrepare.writeCSVFile(dbQuery.queryDB(sql), fileName);
            dbQuery.importDataToDB(fileName, "employees_sorted");
            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }
}
