package ica;

import conn.MySQLConn;
import cryto.GenSig;
import dbOp.DBOperation;
import dbOp.DBPrepare;
import dbOp.DBQuery;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class InsertICDBManager {
    public static void main(String[] ags) throws SQLException, NoSuchAlgorithmException {
        ArrayList<ArrayList<String>> preTupleList, succTupleList;
        String schemaName = "employees_icdb_completeness";
        String tableName = "salaries_comp_withPreSucc";
        String origTableName="salaries";
        String password = "113071";
        String ocaAttr = "salary";
        String[] primaryAttr = {"emp_no", "from_date"};
        String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        String insertFileName = "data/salariesDeletedTuples.csv";
        //int testNum = 200000;

        ArrayList<String> sigList = new ArrayList<>();
        MySQLConn connection = new MySQLConn(schemaName, password);
        Connection conn= connection.getConn();
        DBOperation dbOperation = new DBOperation(conn, schemaName);

        DBPrepare dbPrepare = new DBPrepare();
        ArrayList<ArrayList<String>> inserTupleList = dbPrepare.readCSVFile(insertFileName);
        InsertAICG insertAICG = new InsertAICG(dbOperation,schemaName,tableName,ocaAttr,primaryAttr,inserTupleList);
       // insertAICG.rawDataInsert();
        insertAICG.getPreSuccTuple();

        preTupleList = insertAICG.getPreTupleList();
        succTupleList = insertAICG.getSuccTupleList();

        for(int i = 0; i < inserTupleList.size();i++){
            ArrayList<String> prePrimaryKey = new ArrayList<>();
            prePrimaryKey.add(preTupleList.get(i).get(0));
            prePrimaryKey.add(preTupleList.get(i).get(2));
            ArrayList<String> succPrimaryKey = new ArrayList<>();
            succPrimaryKey.add(succTupleList.get(i).get(0));
            succPrimaryKey.add(succTupleList.get(i).get(2));
            ArrayList<String> insertedPrimaryKey = new ArrayList<>();
            insertedPrimaryKey.add(inserTupleList.get(i).get(0));
            insertedPrimaryKey.add(inserTupleList.get(i).get(2));
            GenSig genSig = new GenSig();
            String insertedSig = genSig.genSigInsertion(preTupleList.get(i), inserTupleList.get(i), ocaAttr, origTableName, keyFile);
            String succsig = genSig.genSigInsertion(inserTupleList.get(i),succTupleList.get(i),ocaAttr, origTableName,keyFile);
            insertAICG.updatePreCurrSuccTuple(succPrimaryKey, prePrimaryKey, insertedPrimaryKey, insertedSig, succsig, dbOperation);

        }
    }
}
