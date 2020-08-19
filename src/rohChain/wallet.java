package rohChain;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class wallet {
    // pubKey is the address for the wallet
    public PublicKey publicKey;
    public PrivateKey privateKey;

    public wallet(){
        generateKeyPair();
    }

    //generate key pair using ECDSA
    public void generateKeyPair(){
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // initiate keyGen and generate keyPair
            KeyPair keyPair = keyGen.generateKeyPair();
            // set pub/priv keys
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
