/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSDSAssignment1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Queue;


/**
 *
 * @author Ian Gortan
 * Simple client to test subscribing from CAServer over RMI
 */
 class SubThread  extends Thread{


    private String topic;
    private int num;
    private String host;
    SubThread(String host, String topic, int num) {
        this.host = host;
        this.topic = topic;
        this.num = num;
    }


    public void run(){

        try {
            System.out.println("Subscriber Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
            System.out.println("Connected to registry");
            BSDSSubscribeInterface CAServerStub = (BSDSSubscribeInterface) registry.lookup("CAServerSubscriber");
            System.out.println("Stub initialized");

/*
args[0] local host, args[1] Topic, arg[2] number of message want, args[3] topic...keeps going like that
 */



                CAServerStub.registerSubscriber(topic);
                Integer countOfMessageRecieved = 0;

                long requestNew = 100;

                Long currentTimeStamp = System.currentTimeMillis();
                //60 seconds
            int hashtopic = topic.hashCode();

                for (int i = 0; i < num; i++) {
                    String messagereturn = CAServerStub.getLatestContent(hashtopic);

                    while ( messagereturn.equals("No message for this topic ") && (requestNew < 60000)) {
                        System.out.println("Sleep Time：" + requestNew);
                        requestNew *= 2;
                        Thread.sleep(requestNew);
                        messagereturn = CAServerStub.getLatestContent(hashtopic);
                    }
                    if (requestNew >= 60000)
                        break;
                    //System.out.println("Message return #:" + countOfMessageRecieved);
                    countOfMessageRecieved++;
                    requestNew = 100;
                }


                if (countOfMessageRecieved != num)
                    System.out.println("Failed to request all content, part of missing");

                    Long walltime = System.currentTimeMillis() - currentTimeStamp;
                    System.out.println("Recieving message on topic : " +  topic);
                    System.out.println("Total wall time to receive all message " + walltime.toString() + "ms.");
                    System.out.println("Total message received for this topic: " + countOfMessageRecieved);
                    System.out.println(" ... Looks like Subscribe Client is working too");




        }catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }}
public class CASubClient {
    private CASubClient (){}
    public static void main(String[] args){
        if(args.length<3){
                System.out.println("Not enough args"); return;
        }
        Integer len = (args.length - 1)/2;
        Queue<Thread> threadQueue = new LinkedList<>();

        for (int i = 0; i < len; i++) {
                Thread t = new SubThread(args[0], args[i*2+1], Integer.parseInt(args[i*2+2]));
                threadQueue.add(t);
                t.start();

        }



        for (int i = 0; i < len; i++) {
            Thread t = threadQueue.poll();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(len.toString() + " Publish Sub(s) have done their work");
    }
}



