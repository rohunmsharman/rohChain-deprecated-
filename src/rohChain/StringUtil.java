package rohChain;

import java.security.MessageDigest;

//basically a carbon copy of http://www.baeldung.com/sha-256-hashing-java, just to has using SHA256

public class StringUtil {

    public static String applySha256(String input){
         try {
             MessageDigest digest = MessageDigest.getInstance("SHA-256");
             //apply sha256
             byte[] hash = digest.digest(input.getBytes("UTF-8"));
             StringBuffer hexString = new StringBuffer(); //hexdec form of the hash
             for (int i = 0; i < hash.length; i++){
                 String hex = Integer.toHexString(0xff & hash[i]);
                 if(hex.length() == 1) hexString.append('0');
                 hexString.append(hex);
             }

             return hexString.toString();

         }

         catch(Exception e){
             throw new RuntimeException(e);
         }
    }

}
