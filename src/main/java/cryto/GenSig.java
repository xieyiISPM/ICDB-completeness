package cryto;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class GenSig {

    public GenSig(){

    }

    public ArrayList genSignature(ArrayList<ArrayList<String>> ocaFieldList,
                                  ArrayList<ArrayList<String>> orderTupleList,
                                  String attrName, String tableName, String keyFile) throws NoSuchAlgorithmException {
        ArrayList sigList = new ArrayList<byte[]>();
        ArrayList<String> ocaList = ocaConcatenate(ocaFieldList, orderTupleList, attrName, tableName);
        RSA rsa = new RSA();
        for (String oca: ocaList){
            sigList.add(rsa.signature(oca,rsa.getPrivateKey(keyFile), rsa.getModulus(keyFile)));
        }

        return sigList;

    }

    public ArrayList ocaConcatenate(ArrayList<ArrayList<String>> ocaFieldList,
                                     ArrayList<ArrayList<String>> orderTupleList,
                                     String attrName, String tableName){

        ArrayList<String> ocaList = new ArrayList<>();
        for(int i = 0; i< ocaFieldList.size(); i++){
            String oca = "";
            ArrayList<String> ocaField = ocaFieldList.get(i);
            int key1 =  Integer.parseInt(ocaField.get(1));
            String key2 = ocaField.get(2);
            oca = oca + ocaField.get(0) + "|" + key1 +
                    key2+ attrName + tableName + ocaField.get(3);
            int predecessorIndex = getTupleIndex(orderTupleList, key1, key2);
            if(predecessorIndex !=-1){
                ArrayList predecessorFields = getPredecessor(predecessorIndex,orderTupleList);
                if(predecessorFields !=null){
                    oca = oca + predecessorFields.get(0) + "|" + predecessorFields.get(1) +
                            predecessorFields.get(2) + predecessorFields.get(3);
                }

            }
            else{
                oca = oca + "";
            }

            ocaList.add(oca);
        }

        return ocaList;
    }

    public int getTupleIndex(ArrayList<ArrayList<String>> orderedTupleList, int key1, String key2 ){
        for (int i = 0; i < orderedTupleList.size(); i++){
            ArrayList<String> tuple = orderedTupleList.get(i);

            if(key1 == Integer.parseInt(tuple.get(1)) && key2.equals(tuple.get(2))){
                return i;
            }
        }

        return -1;
    }

    public ArrayList getPredecessor(int index, ArrayList<ArrayList<String>> orderedTupleList){
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
