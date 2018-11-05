package ica;

import cryto.RSA;
import dbOp.DBOperation;
import cryto.GenSig;
import dbOp.DBQuery;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class AICG {
        private Connection conn;
        private String schema;
        private String tableName;
        private String[] primaryAttr;
        //private String[] attrName;
        private String ocaAttr;
        private ArrayList<String> tsHead = null;
        private ArrayList<String> tsTail = null;
        private DBOperation dbOperation;

        public AICG(Connection conn, String schema, String tableName, String[] primaryAttr, String ocaAttr) throws SQLException {
            this.conn = conn;
            this.schema = schema;
            this.tableName = tableName;
            this.primaryAttr = primaryAttr;
            this.ocaAttr = ocaAttr;
            this.dbOperation = new DBOperation(conn,schema);
        }

        public ArrayList<ArrayList<String>> getTsList(String sql) throws SQLException {
            ArrayList<ArrayList<String>> tsList = dbOperation.queryDB(sql);
            if(tsList !=null){
                tsHead = tsList.get(0);
                tsTail = tsList.get(tsList.size()-1);
            }
            return dbOperation.queryDB(sql);
        }

        public ArrayList<ArrayList<String>> getTnList(String[] primaryAttr, String sql) throws SQLException {
            ArrayList<ArrayList<String>> orderedTupleList = dbOperation.sortTable(sql);
            GenSig genSig = new GenSig();
            ArrayList<ArrayList<String>> tnList = new ArrayList<>();
            int headIndex = genSig.getTupleIndex(orderedTupleList, Integer.parseInt(tsHead.get(0)), tsHead.get(2));
            tnList.add(genSig.getPredecessor(headIndex,orderedTupleList));

            int tailIndex = genSig.getTupleIndex(orderedTupleList, Integer.parseInt(tsTail.get(0)), tsTail.get(2));
            tnList.add(genSig.getSuccessor(tailIndex,orderedTupleList));

            return tnList;
        }

        public ArrayList<ArrayList<String>> getTnList2() throws SQLException{
            ArrayList<ArrayList<String>> tnList = new ArrayList<>();
            String sqlPre = "SELECT * from " + schema + "." + tableName +
                            " WHERE emp_no = " + tsHead.get(5) + " AND " + "from_date = '" + tsHead.get(6) + "' ;";
            String sqlSuc = "SELECT * from " + schema + "." + tableName +
                             " WHERE emp_no = " + tsTail.get(5) + " AND " + "from_date = '" + tsTail.get(6) + "';";

            ArrayList<ArrayList<String>> tuplePre = dbOperation.queryDB(sqlPre);
            ArrayList<ArrayList<String>> tupleSuc = dbOperation.queryDB(sqlSuc);
            tnList.add(tuplePre.get(0));
            tnList.add(tupleSuc.get(0));

            return tnList;
        }

        public byte[] genCondensedRSA(ArrayList<ArrayList<String>> tsList, String keyFile) throws NoSuchAlgorithmException {

            ArrayList<byte[]> sigList = new ArrayList<>();
            int sigIndex = tsList.get(0).size() -1;
            for(ArrayList<String> tuple: tsList){
                sigList.add(Base64.getDecoder().decode(tuple.get(sigIndex)));
            }
            if(sigList!=null){
                RSA rsa = new RSA();
                byte[] condensedRSASig = rsa.condensedRSASignature(sigList, keyFile);
                return condensedRSASig;
            }
            return null;
        }




 }
