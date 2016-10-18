package BSDSAssignment1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




/**
 *
 * @author Ian Gortan
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Queue;
import java.util.LinkedList;
// Simple client to test publishing to CAServer over RMI


class PubThread extends Thread {

            private String topic;
            private int pos;
            private String host;

            PubThread(String arguments, String host, int pos1){
                topic = arguments;
                pos = pos1;
                this.host = host;
            }
            public void run(){
        try {
            System.out.println ("Publisher Client Starter");
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            System.out.println ("Connected to registry");
            BSDSPublishInterface CAServerStub = (BSDSPublishInterface) registry.lookup("CAServerPublisher");
            System.out.println ("Stub initialized");

/*
topic[0] local host, topic[1] number of threads, topic[2] Topic, arg[3] number of post, topic[4[ topic...keeps going like that
 */

            Integer countOfMessageSend = 0;
            int topic1 = CAServerStub.registerPublisher(topic);
            System.out.println("Pub id = " + topic);



            Long currentTimeStamp = System.currentTimeMillis();

                for (int nums = 0; nums < pos; nums++) {
                    String message = Integer.toString(nums);
                    CAServerStub.publishContent(topic1, message, 30000);
                    countOfMessageSend++;
                }

            if(countOfMessageSend == pos) {
                Long walltime = System.currentTimeMillis() - currentTimeStamp;
                System.out.println("Total wall time to sent all message " + walltime.toString() + "ms.");
                System.out.println("Totall messages send: " + countOfMessageSend);
                System.out.println("Publish Client is working");
            }
            else System.out.println("Something is wrong");
        }


        catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }}


        public class CAPubClient {

            private CAPubClient() {
            }

            public static void main(String[] args) {
                if (args.length < 4) {
                    System.out.println("Not enough arguments, exit");
                    return;}
                final Integer numOfThreads = Integer.parseInt(args[1]);

               int len = (args.length - 2) / 2;



                    Queue<Thread> threadQueue = new LinkedList<>();
                    for (int i = 0; i < len; i++) {
                        for (int x = 0; x < numOfThreads; x++) {
                            Thread t = new PubThread(args[i*2+2],args[0], Integer.parseInt(args[i*2+3]));
                            threadQueue.add(t);
                            t.start();
                        }
                    }



                    for (int i = 0; i < numOfThreads*len; i++) {
                        Thread t = threadQueue.poll();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                System.out.println(numOfThreads.toString() + " Publish Client(s) have done their work");
            }
        }