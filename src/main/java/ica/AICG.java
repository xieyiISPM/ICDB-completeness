package ica;

import dbOp.DBOperation;
import cryto.GenSig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

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




 }
