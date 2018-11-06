package ica;

import dbOp.DBOperation;

import java.sql.SQLException;
import java.util.ArrayList;

public class InsertAICG {
    private String schemaName;
    private String tableName;
    private String ocaAttr;
    private String[] primaryAttr;
    private DBOperation dbOperation;
    private ArrayList<ArrayList<String>> insertTupleList;
    private ArrayList<ArrayList<String>> preTupleList = new ArrayList<>();
    private ArrayList<ArrayList<String>> succTupleList = new ArrayList<>();


    public InsertAICG(DBOperation dbOperation, String schemaName, String tableName,
                      String ocaAttr, String[]primaryAttr,
                      ArrayList<ArrayList<String>> insertTupleList) throws SQLException {
        this.dbOperation =dbOperation;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.ocaAttr =ocaAttr;
        this.primaryAttr = primaryAttr;
        this.insertTupleList = insertTupleList;
    }

    public void updatePreCurrSuccTuple(ArrayList<String> succPrimaryKey, ArrayList<String> prePrimaryKey,
                                       ArrayList<String> insertedPrimaryKey, String insertedSig,
                                       String succSig, DBOperation dbOperation) throws SQLException {
        String preSql = "UPDATE " + tableName + " SET succ_emp_no = " + insertedPrimaryKey.get(0) + " ,"
                + " succ_from_date = '" + insertedPrimaryKey.get(1) + "' WHERE emp_no = " + prePrimaryKey.get(0)
                + " AND from_date = '" + prePrimaryKey.get(1) + "'; " ;


        String insertedSql = "UPDATE " + tableName + " SET pre_emp_no = " + prePrimaryKey.get(0) + " ,"
                + " pre_from_date = '" + prePrimaryKey.get(1) + "' , " + "succ_emp_no =" + succPrimaryKey.get(0) + " ,"
                + " succ_from_date = '" + succPrimaryKey.get(1) + "' ," + "ic_comp = '" + insertedSig +
                "' WHERE emp_no = " + insertedPrimaryKey.get(0)
                + " AND from_date = '" + insertedPrimaryKey.get(1) + "'; " ;

        String succSql = "UPDATE " + tableName + " SET pre_emp_no = " + insertedPrimaryKey.get(0) + " ,"
                + " pre_from_date = '" + insertedPrimaryKey.get(1) + "' , ic_comp = '" + succSig +
                "' WHERE emp_no = " + succPrimaryKey.get(0) + " AND from_date = '" + succPrimaryKey.get(1) + "'; " ;

        dbOperation.updateDB(preSql);
        dbOperation.updateDB(succSql);
        dbOperation.updateDB(insertedSql);
    }



    public void rawDataInsert() throws SQLException {
        ArrayList<String> insertSqlList = new ArrayList<>();
        for(ArrayList<String>  tuple: insertTupleList){
            String updateSql = "INSERT INTO " + tableName + " (emp_no, salary, from_date, to_date, serials) VALUES (" +
                    tuple.get(0) + ", " + tuple.get(1) + ", '" + tuple.get(2) + "', '"
                    + tuple.get(3) + "', " + tuple.get(4) + ");";
            insertSqlList.add(updateSql);
        }
        for(String insertSql: insertSqlList){
            dbOperation.updateDB(insertSql);
        }
    }

    public void getPreSuccTuple() throws SQLException {
        String orderedSql = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  ocaAttr +", "+ primaryAttr[0] + ", " + primaryAttr[1]+ " ;" ;
        ArrayList<ArrayList<String>> tupleList ;
        tupleList = dbOperation.queryInsert(orderedSql);
        int tupleListSize = tupleList.size();
        for(ArrayList<String> insertTuple: insertTupleList){
            if(tupleList.get(0).get(0).equals(insertTuple.get(0)) && tupleList.get(0).get(1).equals(insertTuple.get(1))
                    && tupleList.get(0).get(2).equals(insertTuple.get(2)) && tupleList.get(0).get(4).equals(insertTuple.get(4))){
                ArrayList<String> preTuple = new ArrayList<>();
                ArrayList<String> succTuple = new ArrayList<>();
                for(int j = 0; j < 5; j++){
                    preTuple.add("");
                    succTuple.add("");
                }
                preTupleList.add(preTuple);
                succTupleList.add(succTuple);
                continue;
            }
            if(tupleList.get(tupleListSize-1).get(0).equals(insertTuple.get(0)) && tupleList.get(tupleListSize-1).get(1).equals(insertTuple.get(1))
                    && tupleList.get(tupleListSize-1).get(2).equals(insertTuple.get(2)) && tupleList.get(tupleListSize-1).get(4).equals(insertTuple.get(4))){
                ArrayList<String> preTuple = new ArrayList<>();
                ArrayList<String> succTuple = new ArrayList<>();
                for(int j = 0; j < 5; j++){
                    preTuple.add("");
                    succTuple.add("");
                }
                preTupleList.add(preTuple);
                succTupleList.add(succTuple);
                continue;
            }
            for(int i = 1; i< tupleList.size() - 1;i++){
                if(tupleList.get(i).get(0).equals(insertTuple.get(0)) && tupleList.get(i).get(1).equals(insertTuple.get(1))
                    && tupleList.get(i).get(2).equals(insertTuple.get(2)) && tupleList.get(i).get(4).equals(insertTuple.get(4))){
                    ArrayList<String> preTuple = new ArrayList<>();
                    ArrayList<String> succTuple = new ArrayList<>();
                    for(int j = 0; j < 5; j++){
                        preTuple.add(tupleList.get(i-1).get(j));
                        succTuple.add(tupleList.get(i+1).get(j));
                    }
                    preTupleList.add(preTuple);
                    succTupleList.add(succTuple);
                    break;
                }
            }
        }


    }

    public ArrayList<ArrayList<String>> getPreTupleList(){
        return preTupleList;
    }

    public ArrayList<ArrayList<String>> getSuccTupleList(){
        return succTupleList;
    }


}
