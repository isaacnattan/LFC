package main;

import conversoes.ConversaoERParaAFN;

/**
 * @author Isaac_Nattan
 */
public class Main {
    public static void main(String[] args) {
        if(args.length > 0){
            new ConversaoERParaAFN(args[0]);
        }
    }
}
