package dbOp;
import java.sql.*;
import java.util.*;

public class DBQuery {
    private Connection conn;
    private String schemaName;

    public DBQuery(String schemaName, Connection conn){
        this.schemaName = schemaName;
        this.conn = conn;
    }

    public void useDB() throws SQLException{
        String sql;
        Statement stmt = null;
        stmt = conn.createStatement();
        sql = "USE " + schemaName+ ";";
        stmt.executeQuery(sql);
        System.out.println("Using database " + schemaName + "...");
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



    public ArrayList<ArrayList<String>> getOrderedTuple(String sql) throws SQLException {
        return queryDB(sql);
    }

    public void importDataToDB(String fileName, String tableName) {
        try{
            String sql = "LOAD DATA LOCAL INFILE '"  +fileName
                        + "' INTO TABLE " + tableName
                        + " FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n';";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeQuery(sql);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Open csv file failed!");
        }
    }

    public void outputTuple(ArrayList<ArrayList<String>> tupleList){
        for(ArrayList<String> tuple: tupleList){
            String tupleString="";
            for(String field: tuple){
                tupleString += field + ",";
            }
            System.out.println(tupleString);
        }
    }

    public void outputTupleWithPreTuple(ArrayList<ArrayList<String>> tupleList){
        for(int i=0; i< tupleList.size(); i++){
            String tupleString="";
            ArrayList<String> tuple = tupleList.get(i);
            for(int j = 0; j< tuple.size();i++){
                String field = tuple.get(j);
                tupleString += field + ",";
            }
            System.out.println(tupleString);
        }
    }

}
