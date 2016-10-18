/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSDSAssignment1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Ian Gortan
 * Simple client to test subscribing from CAServer over RMI
 */
public class CASubClient {

    private CASubClient() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println("Subscriber Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
            System.out.println("Connected to registry");
            BSDSSubscribeInterface CAServerStub = (BSDSSubscribeInterface) registry.lookup("CAServerSubscriber");
            System.out.println("Stub initialized");

/*
args[0] local host, args[1] Topic, arg[2] number of message want, args[3] topic...keeps going like that
 */
            int len = args.length;
            int pos = 1;
            while (pos < len-1) {
                Thread.sleep(1000);


                String topic = args[pos] ;
                int id = CAServerStub.registerSubscriber(args[pos++]);
                int nums = Integer.parseInt(args[pos]);
                if(pos <= len-2 ) pos++;
                Integer countOfMessageRecieved = 0;

                long requestNew = 100;

                Long currentTimeStamp = System.currentTimeMillis();
                //60 seconds


                for (int i = 0; i < nums; i++) {
                    String messagereturn = CAServerStub.getLatestContent(id);

                    while (messagereturn.equals("No message for this topic ") && (requestNew < 6000)) {
                        System.out.println("Sleep Time：" + requestNew);
                        requestNew *= 2;
                        Thread.sleep(requestNew);
                        messagereturn = CAServerStub.getLatestContent(id);
                    }
                    if (requestNew >= 6000)
                        break;
                    //System.out.println("Message return #:" + countOfMessageRecieved);
                    countOfMessageRecieved++;
                    requestNew = 100;
                }


                if (countOfMessageRecieved != nums)
                    System.out.println("Failed to request all content, part of missing");

                    Long walltime = System.currentTimeMillis() - currentTimeStamp;
                    System.out.println("Recieving message on topic : " +  topic);
                    System.out.println("Total wall time to receive all message " + walltime.toString() + "ms.");
                    System.out.println("Total message received for this topic: " + countOfMessageRecieved);
                    System.out.println(" ... Looks like Subscribe Client is working too");

            }


        }catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
} 
