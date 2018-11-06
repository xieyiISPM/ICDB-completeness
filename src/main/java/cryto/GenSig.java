package cryto;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class GenSig {

    public GenSig(){

    }

    public ArrayList<ArrayList<String>> updateOrderedList(ArrayList<ArrayList<String>> orderTupleList,
                                                          String attrName, String tableName,
                                                          String keyFile) throws NoSuchAlgorithmException {
        RSA rsa = new RSA();
        for(int i = 0; i< orderTupleList.size(); i++){

            String oca = "";
            String attrValue = orderTupleList.get(i).get(1);
            String key1 = orderTupleList.get(i).get(0);
            String key2 = orderTupleList.get(i).get(2);

            String serialNum = orderTupleList.get(i).get(4);

            oca = oca + attrValue + "|" + key1 + key2 + attrName + tableName + serialNum ;

            if(i != 0){
                String preAttrValue = orderTupleList.get(i - 1).get(1);
                String preKey1 = orderTupleList.get(i).get(5);
                String preKey2 = orderTupleList.get(i).get(6);
                String preSerialNum = orderTupleList.get(i-1).get(4);

                oca = oca + preAttrValue + "|" + preKey1 + preKey2 + preSerialNum;
            }

            byte[] signature = rsa.signature(oca, rsa.getPrivateKey(keyFile),rsa.getModulus(keyFile));
            orderTupleList.get(i).add(Base64.getEncoder().encodeToString(signature));

        }
        return orderTupleList;
    }

    public String genSig(ArrayList<String> preTuple, ArrayList<String> currentTuple, String attrName, String tableName, String keyFile) throws NoSuchAlgorithmException {
        String oca = "";
        String attrValue = currentTuple.get(1);
        String key1 = currentTuple.get(0);
        String key2 = currentTuple.get(2);

        String serialNum = currentTuple.get(4);

        oca = oca + attrValue + "|" + key1 + key2 + attrName + tableName + serialNum ;

        if(Integer.parseInt(currentTuple.get(5)) != 0){
            String preAttrValue = preTuple.get(1);
            String preKey1 = preTuple.get(0);
            String preKey2 = preTuple.get(2);
            String preSerialNum = preTuple.get(4);

            oca = oca + preAttrValue + "|" + preKey1 + preKey2 + preSerialNum;
        }
        RSA rsa = new RSA();

        byte[] signature = rsa.signature(oca, rsa.getPrivateKey(keyFile),rsa.getModulus(keyFile));
       return Base64.getEncoder().encodeToString(signature);
    }

    public String genSigInsertion(ArrayList<String> preTuple, ArrayList<String> nextTuple, String attrName, String tableName, String keyFile) throws NoSuchAlgorithmException {
        String oca = "";
        String attrValue = nextTuple.get(1);
        String key1 = nextTuple.get(0);
        String key2 = nextTuple.get(2);

        String serialNum = nextTuple.get(4);

        oca = oca + attrValue + "|" + key1 + key2 + attrName + tableName + serialNum ;

        if(Integer.parseInt(preTuple.get(0)) != 0){
            String preAttrValue = preTuple.get(1);
            String preKey1 = preTuple.get(0);
            String preKey2 = preTuple.get(2);
            String preSerialNum = preTuple.get(4);

            oca = oca + preAttrValue + "|" + preKey1 + preKey2 + preSerialNum;
        }
        RSA rsa = new RSA();

        byte[] signature = rsa.signature(oca, rsa.getPrivateKey(keyFile),rsa.getModulus(keyFile));
        return Base64.getEncoder().encodeToString(signature);
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

            if(key1 == Integer.parseInt(tuple.get(0)) && key2.equals(tuple.get(2))){
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

    public ArrayList getSuccessor(int index, ArrayList<ArrayList<String>> orderedTupleList){
        if(index != orderedTupleList.size() -1 ){
            return orderedTupleList.get(index + 1);
        }
        else{
            return null;
        }

    }

}
