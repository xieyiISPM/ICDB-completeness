import com.google.common.base.Stopwatch;
import conn.MySQLConn;
import dbOp.DBQuery;
import ica.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestICA {
    public static void main(String[] args){
        String schemaName = "employees_icdb_completeness";
        String tableName = "salaries_comp";
        String password = "113071";
        String ocaAttr = "salary";
        String[] primaryAttr = {"emp_no", "from_date"};
        //String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        try{
            Stopwatch stopwatch = Stopwatch.createStarted();
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();

            /* Test getOCAfield*/
            AICG aicg = new AICG(conn, schemaName, tableName, primaryAttr, ocaAttr);
            String sql="SELECT * FROM " +schemaName +"." +tableName  + " WHERE salary >= 38888 AND salary <=38928 ORDER BY salary, emp_no, from_date; ";
            String sqlOrdered = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  ocaAttr +", "+ primaryAttr[0] + ", " + primaryAttr[1]+ " ;" ;
            ArrayList<ArrayList<String>> tsList = aicg.getTsList(sql);


            DBQuery dbQuery = new DBQuery(schemaName,conn);
            System.out.println("Ts list:");
            dbQuery.outputTuple(tsList);
            System.out.println("Tn list:");
            ArrayList<ArrayList<String>> tnList = aicg.getTnList(primaryAttr,sqlOrdered);
            dbQuery.outputTuple(tnList);

            long timer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Signing time: " + timer+ "ms" );

            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }

}
