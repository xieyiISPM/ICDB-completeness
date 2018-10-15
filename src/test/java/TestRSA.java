import rsa.RSA;

import java.math.BigInteger;
import java.util.LinkedList;

public class TestRSA {
    public static void main(String[] args){

        LinkedList sigList = new LinkedList<byte[]>();
        LinkedList messageList = new LinkedList<String>();
        int num = 10;
        for(int i= 0; i < num; i++){
            messageList.add(new String("message" + i));
        }

        String keyFile ="secret/keyFile.txt";

        RSA rsa = new RSA();
        //rsa.genkeyFile(keyFile);
        BigInteger privateKey = rsa.getPrivateKey(keyFile);
        BigInteger publicKey = rsa.getPublicKey(keyFile);
        BigInteger modulus = rsa.getModulus(keyFile);
        System.out.println("Public key: " + publicKey);
        System.out.println("Private key: " + privateKey);
        System.out.println("modulus: " + modulus);

        for(int i = 0; i< num; i++){
            sigList.add(rsa.signature((String)messageList.get(i),privateKey,modulus));
        }


        byte[] condSig = rsa.condensedRSASignature(sigList, keyFile);
        byte[] prodMessage = rsa.messageMultiplication(messageList,keyFile);

        System.out.println("Verification result: "  + rsa.verify(condSig, prodMessage, publicKey, modulus));


    }

}
