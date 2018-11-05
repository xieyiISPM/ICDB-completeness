package ica;

import conn.MySQLConn;
import cryto.GenSig;
import dbOp.DBOperation;
import dbOp.DBPrepare;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DeleteICDBManager {
    public static void main(String[] ags) throws SQLException, NoSuchAlgorithmException {
        ArrayList<ArrayList<String>> tsList, tnList;
        String schemaName = "employees_icdb_completeness";
        String tableName = "salaries_comp_withPreSucc";
        String origTableName="salaries";
        String password = "113071";
        String ocaAttr = "salary";
        String deletedTupleFile = "data/salariesDeletedTuples.csv";
        String[] primaryAttr = {"emp_no", "from_date"};
        String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        int testNum = 200000;

        ArrayList<String> sigList = new ArrayList<>();
        MySQLConn connection = new MySQLConn(schemaName, password);
        Connection conn= connection.getConn();
        DBOperation dbOperation = new DBOperation(conn, schemaName);

        //String selectSql = "SELECT *  FROM " + tableName + " WHERE salary = 38850 LIMIT 1;";
        String selectSql = "SELECT *  FROM " + tableName + " WHERE salary = 38850 AND emp_no = 50419 AND from_date='1996-09-22' ;";
        deleteTuples(selectSql, dbOperation, schemaName, tableName, origTableName, ocaAttr, keyFile, primaryAttr, deletedTupleFile);
    }

    private static void deleteTuples(String selectSql, DBOperation dbOperation, String schemaName,
                                     String tableName, String origTableName, String attrName,
                                     String keyFile, String[] primmaryAttr, String deletedTupleFile ) throws SQLException, NoSuchAlgorithmException {

        ArrayList<ArrayList<String>> tupleList = dbOperation.queryDB(selectSql);
        if(tupleList.size() == 0){
            System.out.println("No records found!");
            return;
        }
        DBPrepare dbPrepare = new DBPrepare();
        dbPrepare.writeCSVFile(tupleList, deletedTupleFile);

        for(ArrayList<String> fieldList: tupleList){
            ArrayList<String> prePrimaryKey = new ArrayList<>();
            prePrimaryKey.add(fieldList.get(5));
            prePrimaryKey.add(fieldList.get(6));
            ArrayList<String> succPrimaryKey = new ArrayList<>();
            succPrimaryKey.add(fieldList.get(7));
            succPrimaryKey.add(fieldList.get(8));
            ArrayList<String> currentPrimaryKey = new ArrayList<>();
            currentPrimaryKey.add(fieldList.get(0));
            currentPrimaryKey.add(fieldList.get(2));
            String deleteSql = "DELETE FROM " + tableName + " WHERE emp_no = " + currentPrimaryKey.get(0)
                    + " AND from_date= '" + currentPrimaryKey.get(1) + "' ;";
            String newSig = verifyTuple(fieldList, dbOperation, attrName, tableName, origTableName, keyFile);
            if(newSig!= null){
                DeleteAICG delAICG = new DeleteAICG(dbOperation, schemaName, tableName, primmaryAttr);
                delAICG.updateSuccTuple(succPrimaryKey, prePrimaryKey, newSig, dbOperation, deleteSql);
            }
            else {
                System.out.println("Verification Fails");
                return;
            }
        }
        System.out.println("Delete operation successes!");
    }

    private static String verifyTuple(ArrayList<String> tuple, DBOperation dbOperation,
                                       String attrName, String tableName,String origTableName, String keyFile
                                       ) throws SQLException, NoSuchAlgorithmException {
        String preEmp_no = tuple.get(5);
        String preFrom_date = tuple.get(6);
        String succEmp_no = tuple.get(7);
        String succFrom_date = tuple.get(8);

        String preSql = "SELECT * FROM " + tableName + " WHERE emp_no = " + preEmp_no + " AND from_date ='" + preFrom_date + "';";
        String succSql = "SELECT * FROM " + tableName + " WHERE emp_no = " + succEmp_no + " AND from_date ='" + succFrom_date + "';";

        ArrayList<String> preTuple = dbOperation.queryDB(preSql).get(0);
        ArrayList<String> succTuple = dbOperation.queryDB(succSql).get(0);
        GenSig genSig = new GenSig();
        String reSig = genSig.genSig(preTuple, tuple, attrName, origTableName, keyFile);
        String newSig = genSig.genSig(preTuple,succTuple,attrName, origTableName,keyFile);

        if(reSig.equals(tuple.get(9))){
            return newSig;
        }
        else
            return null;
    }



}
