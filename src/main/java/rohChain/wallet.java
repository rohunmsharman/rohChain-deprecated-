package rohChain;
/**
 * Local wallet class, will be edited to be instantiated with each node.
 */

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class wallet {
    // pubKey is the address for the wallet
    public PublicKey publicKey;
    public PrivateKey privateKey;

    public HashMap<String, txnOut> UTXOs = new HashMap<String, txnOut>(); //UTXOs return by respective wallet

    public wallet(){
        generateKeyPair();
    }

    //generate key pair using ECDSA
    public void generateKeyPair(){

        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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

    //returns balance and stores the UTXOs owned by this wallet in this.UTXOs
    public float getBalance(){
        float total = 0;
        //chekcs to see if publicKey of UTXO matches wallet publicKey, if they match the UTXO is added to the wallet.
        for(Map.Entry<String, txnOut> item: rohChain.UTXOs.entrySet()){
            txnOut UTXO = item.getValue();
            System.out.println("UTXO:" + UTXO );
            if(UTXO.isMine(publicKey)){
                //System.out.println(UTXO + "utxo for ")
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;

            }
        }
        return total;
    }

    //generates/returns new txn from this wallet
    public txn sendFunds(PublicKey _recipient, float value){
        if(getBalance() < value){ //gather balance and check funds
            System.out.println("#not enough funds to send transaction, transaction discarded");
            return null;
        }

        //create an arrayList of inputs
        ArrayList<txnIn> inputs = new ArrayList<txnIn>();

        float total = 0;
        for(Map.Entry<String, txnOut> item: UTXOs.entrySet()){
            txnOut UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new txnIn(UTXO.id));
            if(total > value) break;
        }
        txn newTxn = new txn(publicKey, _recipient, value, inputs);
        newTxn.genSig(privateKey);

        for(txnIn input: inputs){
            UTXOs.remove(input.txnOutId);
        }
        return newTxn;

    }


}
