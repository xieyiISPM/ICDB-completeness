import conn.MySQLConn;
import dbOp.DBOperation;
import dbOp.DBPrepare;
import dbOp.DBQuery;

import java.sql.Connection;
import java.sql.SQLException;

public class TestMyQL {
    public static void main(String[] args){
        String schemaName = "employees_icdb_RSA";
        String tableName = "salaries";
        String password = "113071";
        String fileName = "data/employees_sorted.csv";
        try{
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();

            /*Test DBPrepare class
            DBQuery dbQuery = new DBQuery(schemaName, conn);
            dbQuery.useDB();
            String sql="SELECT * FROM " +schemaName + " ORDER BY hire_date, emp_no;"; //Warning: missed table name
            dbQuery.outputTuple(dbQuery.queryDB(sql));
            System.out.println("Creating csv file...");
            DBPrepare dbPrepare = new DBPrepare();
            dbPrepare.writeCSVFile(dbQuery.queryDB(sql), fileName);
            dbQuery.importDataToDB(fileName, "employees_sorted");
            */
            

            /* Test getOCAfield*/
            DBOperation dbOp = new DBOperation(conn, schemaName);
            String sql="SELECT * FROM " +schemaName +"." +tableName  + " LIMIT 5;";
            dbOp.getOCAfield(sql);



            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }
}
