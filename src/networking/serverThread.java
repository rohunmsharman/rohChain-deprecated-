package networking;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;


public class serverThread extends Thread {
    private ServerSocket serverSocket;
    private Set<serverThreadThread> serverThreadThreads = new HashSet<serverThreadThread>();
    public serverThread(String portNumb) throws IOException{
        serverSocket = new ServerSocket(Integer.valueOf(portNumb));
    }
    public void run(){

    }
    void

    public Set<serverThreadThread> getServerThreadThreads(){return serverThreadThreads;}
}
