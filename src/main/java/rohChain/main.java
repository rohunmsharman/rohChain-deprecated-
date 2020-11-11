package rohChain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class main {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("localhost");
        ServerSocket ss = new ServerSocket(5056);
        Socket s = new Socket(ip, 8888);
        node n1 = new node("127.0.0.1", ss, s);
        n1.nodeConection();


    }


}
