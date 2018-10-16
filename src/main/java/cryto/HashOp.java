package cryto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashOp {

    public HashOp(){

    }

    public byte[] getHashByte(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        byte[] digestData = md.digest();
        BigInteger digestDec = new BigInteger(bytesToHex(digestData));
        return digestDec.toByteArray();
    }

    private byte[] bytesToHex(byte[] hexData){
        StringBuffer result = new StringBuffer();
        for(byte byt : hexData) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString().getBytes();
    }
}
