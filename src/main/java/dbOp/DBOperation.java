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
    public ArrayList<ArrayList<String>> getOrderedTupleList(String sql) throws SQLException {

        ArrayList<ArrayList<String>> orderedTupleList = new ArrayList<ArrayList<String>>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();
        String preEmp_no = "";
        String preFrom_date ="";

        while(resultSet.next()) {
            ArrayList fieldList = new ArrayList<String>();
            /*for (int i = 1; i <= columnNum; i++) {
                if(resultSet.getString(i) !=null) {
                    fieldList.add(resultSet.getString(i).trim());
                }
            }*/

            String currentEmp_no = resultSet.getString(1).trim();
            String currentFrom_date = resultSet.getString(3).trim();


            fieldList.add(currentEmp_no); //"emp_no" column
            fieldList.add(resultSet.getString(2).trim());//"salary" column
            fieldList.add(currentFrom_date);//"from_date" column
            fieldList.add(resultSet.getString(4).trim()); //"to_date" column
            fieldList.add(resultSet.getString(6).trim()); //"serial" column
            fieldList.add(preEmp_no);
            fieldList.add(preFrom_date);
            orderedTupleList.add(fieldList);
            preEmp_no = currentEmp_no;
            preFrom_date = currentFrom_date;
        }
        return orderedTupleList;
    }



    @SuppressWarnings("Duplicates")
    public ArrayList<ArrayList<String>> getOCAfield(String sql) throws SQLException{
        ArrayList<ArrayList<String>> tupleOCAList = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        //ResultSetMetaData rsmd = resultSet.getMetaData();
        //int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            ArrayList<String> fieldList = new ArrayList<String>();

            fieldList.add(resultSet.getString(2).trim()); //"salary" column
            fieldList.add(resultSet.getString(1).trim());//"emp_no" column
            fieldList.add(resultSet.getString(3).trim());//"from_date" column
            fieldList.add(resultSet.getString(7).trim()); //"serial" column

            tupleOCAList.add(fieldList);
        }
        return tupleOCAList;

    }

    @SuppressWarnings("Duplicates")
    public ArrayList<ArrayList<String>> queryDB(String sql) throws SQLException{
        ArrayList<ArrayList<String>> tupleList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            ArrayList fieldList = new ArrayList<String>();
            for (int i = 1; i <= columnNum; i++) {
                fieldList.add(resultSet.getString(i).trim());
            }
            tupleList.add(fieldList);
        }
        return tupleList;
    }

    public ArrayList<ArrayList<String>> sortTable(String sql) throws SQLException{
        ArrayList<ArrayList<String>> tupleList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            ArrayList fieldList = new ArrayList<String>();
            for (int i = 1; i <= columnNum-1; i++) {
                fieldList.add(resultSet.getString(i).trim());
            }
            tupleList.add(fieldList);
        }
        return tupleList;
    }




}
