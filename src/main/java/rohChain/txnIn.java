package rohChain;

public class txnIn {
    public String txnOutId; //txnOut --> txnId
    public txnOut UTXO;

    public txnIn(String txnOutId){
        this.txnOutId = txnOutId;
    }
 // add UTXO databse

}
