package rohChain;
/**
 * Txn structure, methods here will also be called by nodes
 */

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;


//  transactions will follow a basic UTXO design
public class txn implements Serializable {
    private static final long serialVerisonUID = 2L;

    public String txnId; //hash of the transaction
    public PublicKey sender; //sender address/pubKey
    public PublicKey recipient; //recipient address/pubKey
    public float value;
    public byte[] signature; //sig

    public ArrayList<txnIn> inputs = new ArrayList<txnIn>(); //transaction inputs
    public ArrayList<txnOut> outputs = new ArrayList<txnOut>(); //transaction outputs

    private static int seq = 0; // rough count of how many transactions exist

    //transaction constructor
    public txn(PublicKey from, PublicKey to, float value, ArrayList<txnIn> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    //determines if new transaction should be created
    public boolean processTxn(){
        if(verifySignature() == false){
            return false;
        }
        //gather txn inputs
        for(txnIn i : inputs){
            i.UTXO = rohChain.UTXOs.get(i.txnOutId);
        }
        //check if txn is valid
        if(getInputsValue() < rohChain.minTxn){
            System.out.println("#transaction too small: " + getInputsValue());
            return false;

        }

        //generate txn outputs
        float leftOver = getInputsValue() - value; //get left over inputs
        txnId = calculateHash();
        outputs.add(new txnOut(this.sender, leftOver, txnId)); // return leftover coin to sender

        outputs.add(new txnOut(this.recipient, value, txnId)); // send value to recipient :::::THIS IS WHERE THAT STUPID SPENDING BUG WAS, CHECK TXNS ASAP


        //add output to unspent list
        for(txnOut o : outputs){
            rohChain.UTXOs.put(o.id, o);
        }

        for(txnIn i : inputs){
            if(i.UTXO == null) continue; // if txn can't be found: skip
            rohChain.UTXOs.remove(i.UTXO.id);
        }

        return true;

    }

    //returns sum of input values
    public float getInputsValue(){
        float total = 0;
        for(txnIn i : inputs){
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    //returns sum of outputs
    public float getOutputsValue(){
        float total = 0;
        for(txnOut o : outputs){
            total += o.value;
        }
        return total;
    }

    //sign txn
    public void genSig(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }




    //verify txn sig
    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    // calc. transaction hash, will be used as txnID
    private String calculateHash(){
        seq ++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        Float.toString(value) +
                        seq
        );
    }


}
