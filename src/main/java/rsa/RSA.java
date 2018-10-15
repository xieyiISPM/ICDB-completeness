package rsa;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;

public class RSA {
    private BigInteger publicKey = new BigInteger("65537");
    private int bitLength = 1024;
    private int certainty = 100;
    private LinkedList sigList = null;

    public RSA(){

    }

    public RSA(String pk, int bitLength, int certainty){
        this.publicKey = new BigInteger(pk);
        this.bitLength = bitLength;
        this.certainty = certainty;
    }

    /**
     * Generate key file - one time use
     * first line is public key
     * second line is modular n
     * third line is private key
     * @param keyFile
     */
    public void genkeyFile(String keyFile){
        BigInteger phi_n = null;
        BigInteger n = null;
        do {
            BigInteger p = getPrime();
            BigInteger q = getPrime();
            n = p.multiply(q);
            phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        }while (phi_n.bitLength() == bitLength);

        BigInteger privateKey = publicKey.modInverse(phi_n);

        try {
            FileWriter writer = new FileWriter(keyFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(publicKey.toString() + "\n");
            bufferedWriter.write(n.toString() + "\n");
            bufferedWriter.write(privateKey.toString() + "\n");
            bufferedWriter.close();
            System.out.println("Key file generated!");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Key file writing fails!");

        }
    }

    private BigInteger getPrime() {
        SecureRandom rnd = new SecureRandom();
        return BigInteger.probablePrime(bitLength/2, rnd );
    }

    /**
     * Get modulus from key file, which is in the second line of the file
     * @return return modulus
     */
    public BigInteger getModulus(String keyFile){
        BigInteger modulus = null;
        String line = null;
        try {
            FileReader fileReader = new FileReader(keyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        modulus = new BigInteger(line);
        return modulus;
    }

    /**
     * Get public key from key file, which is in the first line of the file
     * @return return public key from key file
     */
    public BigInteger getPublicKey(String keyFile) {
        BigInteger publicKey = null;
        String line = null;
        try {
            FileReader fileReader = new FileReader(keyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        publicKey = new BigInteger(line);
        return publicKey;
    }

    /**
     * Get private key from key file, which is in the third line of the file
     * @return return private key
     */
    public BigInteger getPrivateKey(String keyFile) {
        BigInteger privateKey = null;
        String line = null;
        try {
            FileReader fileReader = new FileReader(keyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        privateKey = new BigInteger(line);
        return privateKey;
    }

    public byte[] signature(String message, BigInteger privateKey, BigInteger modulus){
        byte[] messageByte =message.getBytes();
        BigInteger messageBGInt = new BigInteger(messageByte);
        BigInteger signature = messageBGInt.modPow(privateKey, modulus);
        return signature.toByteArray();
    }

    public boolean verify(byte[] signature, byte[] messageByte, BigInteger publicKey, BigInteger modulus){
        BigInteger signatureBGInt = new BigInteger(signature);
        BigInteger verificationCode = signatureBGInt.modPow(publicKey, modulus);
        BigInteger messageBGInt = new BigInteger(messageByte);
        if (verificationCode.compareTo(messageBGInt)== 0){
            return true;
        }
        else
            return false;
    }

    public byte[] condensedRSASignature(LinkedList<byte[]> sigList, String keyFile){
        BigInteger condSig = BigInteger.ONE;
        BigInteger modulus = getModulus(keyFile);

        for(byte[] signature: sigList){
            BigInteger sigBGInt = new BigInteger(signature);
            condSig = (condSig.multiply(sigBGInt)).mod(modulus);
        }
        return condSig.toByteArray();
    }

    public byte[] messageMultiplication(LinkedList<String> messageList, String keyFile){
        BigInteger prod = BigInteger.ONE;
        BigInteger modulus = getModulus(keyFile);

        for (String message: messageList){
            byte[] messageByte = message.getBytes();
            BigInteger messageBGInt = new BigInteger(messageByte);
            prod = (prod.multiply(messageBGInt.mod(modulus))).mod(modulus);
        }
        return prod.toByteArray();
    }



}
