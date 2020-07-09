import java.util.Date;

public class block {
    public String hash;
    public String prevHash;
    private String data;  // literally a small message, have yet to implement coin
    private long timeStamp;
    private int nonce = 0;

    //to be added with proof of work
    //private int nonce


    //block constructor
    public block(String data, String prevHash){
        this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedHash = StringUtil.applySha256(prevHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
        return calculatedHash;
    }

    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
            //System.out.println("mining");
        }
        System.out.println("block mined, hash: " + hash);
    }

}
