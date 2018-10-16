import cryto.RSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;

public class TestRSA {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

        ArrayList sigList = new ArrayList<byte[]>();
        ArrayList messageList = new ArrayList<String>();
        int num = 300000;
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

        //watch out where to put stopwatch
        Stopwatch stopwatch = Stopwatch.createStarted();
        //verify single signature
        byte[] sig = rsa.signature((String)messageList.get(1), privateKey, modulus);
        byte[] messageByte = ((String) messageList.get(1)).getBytes();
        System.out.println("Verification one signature: " + rsa.verify(sig, messageByte,publicKey,modulus));
        System.out.println("Run Time for single signature verification : " + stopwatch.elapsed(TIME_UNIT) + " ms");
        stopwatch.reset();

        //verify condensed signature
        stopwatch.start();
        byte[] condSig = rsa.condensedRSASignature(sigList, keyFile);
        byte[] prodMessage = rsa.messageMultiplication(messageList,keyFile);
        System.out.println("Verification result: "  + rsa.verifyCondensedRSA(condSig, prodMessage, publicKey, modulus));
        System.out.println("Run Time for condensed signature verification : " + stopwatch.elapsed(TIME_UNIT) + " ms");

    }

}
