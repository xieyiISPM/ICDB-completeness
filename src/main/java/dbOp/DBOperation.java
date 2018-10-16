package dbOp;

import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DBOperation {

    private Connection conn = null;
    private String schema = null;

    public DBOperation(Connection conn, String schema){
        this.conn = conn;
        this.schema = schema;
    }

    /**
     * This is to simulate ordered tuple has been stored in the database.
     * @param sql
     * @return
     * @throws SQLException
     */
    public ArrayList<ArrayList> getOrderedTupleList(String sql) throws SQLException {
        DBQuery dbQuery = new DBQuery(schema, conn);
        dbQuery.useDB();
        ArrayList<ArrayList> orderedTupleList = new ArrayList<ArrayList>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            ArrayList fieldList = new ArrayList<String>();
            for (int i = 1; i <= columnNum; i++) {
                fieldList.add(resultSet.getString(i).trim());
            }
            orderedTupleList.add(fieldList);
        }
        return orderedTupleList;
    }

    public int getTupleIndex(ArrayList<ArrayList> orderedTupleList, int primaryKey){
        for(ArrayList<String> tuple: orderedTupleList){
                if(primaryKey == Integer.parseInt(tuple.get(0))){
                    return tuple.indexOf(tuple);
                }
        }
        return -1;
    }

    public ArrayList getPredecessor(int index, ArrayList<ArrayList> orderedTupleList){
        if(index !=0){
            return orderedTupleList.get(index-1);
        }
        else {
            return null;
        }
    }

    public ArrayList getSuccessor(int index, ArrayList<ArrayList> orderedTupleList){
        if(index != orderedTupleList.size() -1 ){
            return orderedTupleList.get(index + 1);
        }
        else{
            return null;
        }

    }


}
