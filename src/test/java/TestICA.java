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
        String tableName = "salaries_comp_withPre";
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
            SelectAICG aicg = new SelectAICG(conn, schemaName, tableName, primaryAttr, ocaAttr);
            String sql="SELECT * FROM " +schemaName +"." +tableName  + " WHERE salary >= 38888 AND salary <=38928 ORDER BY salary, emp_no, from_date; ";
            //String sql="SELECT * FROM " +schemaName +"." +tableName  + " WHERE salary >= 38888 ORDER BY salary, emp_no, from_date LIMIT 200000; ";

            //String sqlOrdered = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  ocaAttr +", "+ primaryAttr[0] + ", " + primaryAttr[1]+ " ;" ;
            ArrayList<ArrayList<String>> tsList = aicg.getTsList(sql);


            DBQuery dbQuery = new DBQuery(schemaName,conn);
            System.out.println("Ts list:");
            dbQuery.outputTuple(tsList);

            long tsTimer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Ts list retrieving time: " + tsTimer);
            System.out.println();
            stopwatch.reset();
            stopwatch.start();
            System.out.println("Tn list:");
            //ArrayList<ArrayList<String>> tnList = aicg.getTnList(primaryAttr,sqlOrdered);
            ArrayList<ArrayList<String>> tnList = aicg.getTnList2();

            dbQuery.outputTuple(tnList);

            long tnTimer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Tn time: " + tnTimer+ "ms" );

            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }

}
