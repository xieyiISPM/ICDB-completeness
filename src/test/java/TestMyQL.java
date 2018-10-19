import com.google.common.base.Stopwatch;
import conn.MySQLConn;
import cryto.GenSig;
import dbOp.DBOperation;
import dbOp.DBPrepare;
import dbOp.DBQuery;

import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestMyQL {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String schemaName = "employees_icdb_RSA";
        String tableName = "salaries";
        String password = "113071";
        String fileName = "data/employees_sorted.csv";
        String attrName = "salary";
        String primaryKey = "emp_no, from_date";
        String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        try{
            Stopwatch stopwatch = Stopwatch.createStarted();
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
            String sql="SELECT * FROM " +schemaName +"." +tableName  + " LIMIT 50000;";
            String sqlOrdered = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  attrName +", "+ primaryKey + ";" ;


            ArrayList<ArrayList<String>> orderedTupleList =  dbOp.getOrderedTupleList(sqlOrdered);

            long orderedTupleListTime = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Create ordered tuple list done in: " + orderedTupleListTime+ "ms" );
            stopwatch.reset();
            stopwatch.start();


            ArrayList<ArrayList<String>> ocaFieldList = dbOp.getOCAfield(sql);

            GenSig genSig = new GenSig();
            /*ArrayList<String> ocaList = genSig.ocaConcatenate(ocaFieldList, orderedTupleList, attrName, tableName);
            for(String oca: ocaList){
                System.out.println(oca);
            }

            long concatenatedTime = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Concatenated oca time: " + concatenatedTime+ "ms" );
            System.out.println("ConcatenatedTime / orderedTuplelistTime= " + concatenatedTime/orderedTupleListTime);*/

            ArrayList<byte[]> sigList = genSig.genSignature(ocaFieldList, orderedTupleList, attrName, tableName, keyFile);
            for(byte[] sig: sigList){
                System.out.println(new String(sig));
            }
            long signingTime = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Signing time: " + signingTime+ "ms" );






            connection.closeConn();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println(sqlEx);
        }
    }
}
