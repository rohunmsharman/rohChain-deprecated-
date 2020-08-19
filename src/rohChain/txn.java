package rohChain;
import java.security.*;
import java.util.ArrayList;

//  transactions will follow a basic UTXO design
public class txn {
    public String transacitonId; //hash of the transaction
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
