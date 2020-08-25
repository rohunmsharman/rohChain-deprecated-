package rohChain;
import java.security.PublicKey;

public class txnOut {
    public String id;
    public PublicKey recipient;
    public float value;
    public String parentTxnId; //parent txn id

    //txnOut constructor
    public txnOut(PublicKey recipient, float value, String parentTxnId){
        this.recipient = recipient;
        this.value = value;
        this.parentTxnId = parentTxnId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTxnId);
    }

    //ensure txn public key matches recipient
    public boolean isMine(PublicKey publicKey){
        return(publicKey == recipient);
    }


}
