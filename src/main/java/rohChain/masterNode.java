package rohChain;

//import org.json.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.serverSocket;
import java.util.ArrayList;
import java.io.*;

public class masterNode {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //ServerSocket ss = new ServerSocket(4999);
        //System.out.println("clinet connected");
        //rohChain.main();
        Socket client = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

            ServerSocket masterNode = new ServerSocket(8888); //listening on port 8888 (local ip 127.0.0.1)
            client = masterNode.accept(); // blocking I/O call, won't be called unless a conneciton is established
            System.out.println("node connected: ");

            out = new ObjectOutputStream(client.getOutputStream()); // get output stream of client
            in = new ObjectInputStream(client.getInputStream()); //get input stream of client

            //get node name
            String nodeName = (String) in.readObject();
            System.out.println("node name: " + nodeName);

            //send full rchain to client
            out.writeObject(rohChain.rChain);
            //System.out.println()



        //block incomingBlock = (block) in.readObject();
        //System.out.println("incoming block hash: " + incomingBlock.hash + "number of txns: " + incomingBlock.txns.size());

        out.close();
        in.close();
        client.close();
        masterNode.close();


        //simple two way test
        /*
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("client: " + str);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("hello");
        pr.flush();

         */
    }

}
