package ica;

import dbOp.DBOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteAICG {
    private Connection conn;
    private String schemaName;
    private String tableName;
    private String[] primaryAttr;
    private DBOperation dbOperation;

    public DeleteAICG(DBOperation dbOperation, String schemaName, String tableName, String[]primaryAttr) throws SQLException {
        this.conn =conn;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.primaryAttr = primaryAttr;
    }




    public void updateSuccTuple(ArrayList<String> succPrimaryKey, ArrayList<String> prePrimaryKey,
                                String sig, DBOperation dbOperation, String delSql) throws SQLException {
        String preSql = "UPDATE " + tableName + " SET succ_emp_no = " + succPrimaryKey.get(0) + " ,"
                        + " succ_from_date = '" + succPrimaryKey.get(1) + "' WHERE emp_no = " + prePrimaryKey.get(0)
                        + " AND from_date = '" + prePrimaryKey.get(1) + "'; " ;

        String succSql = "UPDATE " + tableName + " SET pre_emp_no = " + prePrimaryKey.get(0) + " ,"
                        + " pre_from_date = '" + prePrimaryKey.get(1) + "' , ic_comp = '" + sig +
                        "' WHERE emp_no = " + succPrimaryKey.get(0)
                        + " AND from_date = '" + succPrimaryKey.get(1) + "'; " ;

        dbOperation.updateDB(preSql);
        dbOperation.updateDB(succSql);
        dbOperation.updateDB(delSql);


    }


}
