package rohChain;

import java.util.ArrayList;
import java.io.*;
import java.net.Socket;


public class client implements Serializable {
    //JSONObject json = new
    public static void main(String[] args)throws IOException{
        //pulls rchain for client usage
        //ArrayList<block> rc = new ArrayList<block>();
        //rc = rohChain.rChain;

        //Socket s = new Socket("localhost", 4999);
        System.out.println("connected to server");

        //block testBlock = new block("0");
        Socket client = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try{
            client = new Socket("localhost", 8888);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            ArrayList fullChain = (ArrayList) in.readObject();
            System.out.println("rchain length: " + fullChain.size());
            //out.writeObject(testBlock);
            out.close();
            in.close();
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }


        //simple two way test
        /*
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("hey");
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server: " + str);

         */

    }

}
