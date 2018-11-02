package ica;

import com.google.common.base.Stopwatch;
import conn.MySQLConn;
import cryto.RSA;
import dbOp.DBQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ICDBManager {


    public static void main(String[] args){
        ArrayList<ArrayList<String>> tsList, tnList;
        String schemaName = "employees_icdb_completeness";
        String tableName = "salaries_comp_withPre";
        String origTableName="salaries";
        String password = "113071";
        String ocaAttr = "salary";
        String[] primaryAttr = {"emp_no", "from_date"};
        String keyFile ="secret/keyFile.txt";
        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
        ArrayList<String> ocaList;
        int testNum = 160000;

        try{
            Stopwatch stopwatch = Stopwatch.createStarted();
            MySQLConn connection = new MySQLConn(schemaName, password);
            Connection conn= connection.getConn();

            /* Test getOCAfield*/
            AICG aicg = new AICG(conn, schemaName, tableName, primaryAttr, ocaAttr);
            //String sql="SELECT * FROM " +schemaName +"." +tableName  + " WHERE salary >= 38888 AND salary <=38928 ORDER BY salary, emp_no, from_date; ";
            String sql="SELECT * FROM " +schemaName +"." +tableName  + " WHERE salary >= 38888 ORDER BY salary, emp_no, from_date LIMIT  " + testNum + ";";

            System.out.println("Test number of tuples: " + testNum);

            String sqlOrdered = "SELECT * FROM " +schemaName +"." +tableName + " ORDER BY " +  ocaAttr +", "+ primaryAttr[0] + ", " + primaryAttr[1]+ " ;" ;

           // DBQuery dbQuery = new DBQuery(schemaName,conn);

            System.out.print("Collecting Ts (Query return set) list...");
            tsList = aicg.getTsList(sql);
            //dbQuery.outputTuple(tsList);
            System.out.println(" done");
            long timer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("ts (Query Return set) collecting time: " + timer+ "ms" );
            stopwatch.reset();
            stopwatch.start();
            System.out.println();

            System.out.print("Collecting Tn list...");
            //tnList = aicg.getTnList(primaryAttr,sqlOrdered);
            tnList = aicg.getTnList2();

            //dbQuery.outputTuple(tnList);
            System.out.println(" done");
            timer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("tn (predecessor/successor  looking-for time: " + timer+ "ms" );
            System.out.println();


            stopwatch.reset();
            stopwatch.start();
            ocaList = getOCAList(tsList, tnList, ocaAttr, origTableName, primaryAttr);

            /*for(String oca:ocaList){
                System.out.println(oca);
            }*/

            RSA rsa = new RSA();
            byte[] ocaMul = rsa.messageMultiplication(ocaList,keyFile);
            byte[] condensedRSA = aicg.genCondensedRSA(tsList,keyFile);
            boolean verified = rsa.verifyCondensedRSA(condensedRSA,ocaMul,rsa.getPublicKey(keyFile),rsa.getModulus(keyFile));
            System.out.println("Verification result: " + verified);
            timer = stopwatch.elapsed(TIME_UNIT);
            System.out.println("Verification time: " + timer+ "ms" );

            connection.closeConn();
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex);
        }

    }


    private static ArrayList<String> getOCAList(ArrayList<ArrayList<String>> tsList,
                                    ArrayList<ArrayList<String>> tnList,
                                    String ocaAttr, String tableName, String[] primaryAttr){
        String oca = "";
        ArrayList<String> ocaList = new ArrayList<>();
        int key1 =  Integer.parseInt(tsList.get(0).get(0));  //double check this index
        String key2 = tsList.get(0).get(2);
        oca = oca + tsList.get(0).get(1) + "|" + key1 +
                key2+ ocaAttr + tableName + tsList.get(0).get(4);
        if(tnList.size()==2){
            oca =oca + tnList.get(0).get(1) + "|" + tnList.get(0).get(0) +
                    tnList.get(0).get(2) + tnList.get(0).get(4);
        }
        ocaList.add(oca);

        for(int i = 1; i< tsList.size(); i++){
            oca="";
            ArrayList<String> ocaField = tsList.get(i);
            ArrayList<String> predecessorFields = tsList.get(i-1);
            key1 =  Integer.parseInt(ocaField.get(0));  //double check this index
            key2 = ocaField.get(2);
            oca = oca + ocaField.get(1) + "|" + key1 +
                    key2+ ocaAttr + tableName + ocaField.get(4) +
                    predecessorFields.get(1) + "|" + predecessorFields.get(0) +
                    predecessorFields.get(2) + predecessorFields.get(4);

            ocaList.add(oca);
        }

        return ocaList;
    }



}
