package rohChain;

import java.util.Scanner;
import java.util.ArrayList;


public class addMessage {
    //creates returns rohChain.rohChain.block w/ new messages and mines the rohChain.rohChain.block
    public static void addMessage() {
        String cont;
        String newMsg;
        Scanner input = new Scanner(System.in);

        //temporary, will change to try/catch error implementation for user
        System.out.println("would you like to add a new message to the rohChain.rohChain? Y/N");
        cont = input.nextLine();
        while (cont.equals("Y")) {
            System.out.print("message: ");
            newMsg = input.nextLine();
            rohChain.rChain.add(new block(newMsg, rohChain.rChain.get(rohChain.rChain.size() - 1).hash));
            rohChain.rChain.get(rohChain.rChain.size() - 1).mineBlock(rohChain.difficulty);
            System.out.println("would you like to add another message: (Y/N)");
            cont = input.nextLine();
        }
    }
}