package dbOp;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public LinkedList<LinkedList> queryDB(String sql) throws SQLException{
        LinkedList<LinkedList> tupleList = new LinkedList<LinkedList>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();

        while(resultSet.next()) {
            LinkedList fieldList = new LinkedList<String>();
            for (int i = 1; i <= columnNum; i++) {
                fieldList.add(resultSet.getString(i).trim());
            }
            tupleList.add(fieldList);
        }
        return tupleList;
    }

    public void outputTuple(LinkedList<LinkedList> tupleList){
        /*for(LinkedList<String> tuple: tupleList){
            String tupleString="";
            for(String field: tuple){
                tupleString += field + ",";
            }
            System.out.println(tupleString);
        }*/

        tupleList
                .stream()
                .collect(Collectors.toList())
                .forEach(e->System.out.println(e.get(2)));


    }

}
