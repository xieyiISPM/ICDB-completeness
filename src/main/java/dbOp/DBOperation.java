package dbOp;

import java.sql.*;
import java.util.ArrayList;

public class DBOperation {

    private Connection conn = null;
    private String schema = null;

    public DBOperation(Connection conn, String schema) throws SQLException {
        this.conn = conn;
        this.schema = schema;
        DBQuery dbQuery = new DBQuery(schema, conn);
        dbQuery.useDB();
    }

    /**
     * This is to simulate ordered tuple has been stored in the database.
     * @param sql
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("Duplicates")
    public ArrayList<ArrayList> getOrderedTupleList(String sql) throws SQLException {

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

    @SuppressWarnings("Duplicates")
    public ArrayList<ArrayList> getOCAfield(String sql) throws SQLException{
        ArrayList<ArrayList> tupleOCAList = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        //ResultSetMetaData rsmd = resultSet.getMetaData();
        //int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            ArrayList fieldList = new ArrayList<String>();

            fieldList.add(resultSet.getString(2).trim()); //"salary" column
            fieldList.add(resultSet.getString(1).trim());//"emp_no" column
            fieldList.add(resultSet.getString(3).trim());//"from_date" column
            fieldList.add(resultSet.getString(7).trim()); //"serial" column

            tupleOCAList.add(fieldList);
        }
        return tupleOCAList;

    }




}
