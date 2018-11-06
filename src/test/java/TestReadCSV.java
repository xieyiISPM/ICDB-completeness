import conn.MySQLConn;
import dbOp.DBPrepare;
import dbOp.DBQuery;

import java.sql.Connection;
import java.util.ArrayList;

public class TestReadCSV {
    public static void main(String[] args){
        String fileName  = "data/salariesDeletedTuples.csv";
        String schemaName = "employees_icdb_completeness";
        String password = "113071";
        DBPrepare dbPrepare = new DBPrepare();
        ArrayList<ArrayList<String>> tupleList = dbPrepare.readCSVFile(fileName);
        MySQLConn connection = new MySQLConn(schemaName, password);
        Connection conn= connection.getConn();
        DBQuery dbQuery = new DBQuery(schemaName, conn);
        dbQuery.outputTuple(tupleList);

    }

}
