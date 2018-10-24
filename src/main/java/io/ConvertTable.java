package io;

import com.google.common.base.Stopwatch;
import conn.MySQLConn;
import cryto.GenSig;
import dbOp.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConvertTable {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String schemaName = "employees_icdb_completeness";
        String tableName = "salaries";
        String password = "113071";
        String fileName = "data/employees_sorted_icdb_completeness.csv";
        String attrName = "salary";
        String primaryKey = "emp_no, from_date";
        String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        try{
            Stopwatch stopwatch = Stopwatch.createStarted();
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();

            DBQuery dbQuery = new DBQuery(schemaName, conn);
            dbQuery.useDB();
            dbQuery.importDataToDB(fileName, "salaries_comp");
            long loadTableTime = stopwatch.elapsed(TIME_UNIT);

            System.out.println("Load table time: " + loadTableTime+ "ms" );



            /*DBOperation dbOp = new DBOperation(conn, schemaName);
            String sqlOrdered = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  attrName +", "+ primaryKey + " ;" ;


            ArrayList<ArrayList<String>> orderedTupleList =  dbOp.getOrderedTupleList(sqlOrdered);

            long orderedTupleListTime = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Create ordered tuple list done in: " + orderedTupleListTime+ "ms" );
            stopwatch.reset();
            stopwatch.start();
            System.out.println("Generating signature...");
            GenSig genSig = new GenSig();
            genSig.updateOrderedList(orderedTupleList,attrName, tableName,keyFile);
            System.out.println("Creating csv file...");
            DBPrepare dbPrepare = new DBPrepare();
            dbPrepare.writeCSVFile(orderedTupleList, fileName);
            long signingTime = stopwatch.elapsed(TIME_UNIT);

            System.out.println("get ICDB-completeness signature time: " + signingTime+ "ms" );
*/
            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }
}
