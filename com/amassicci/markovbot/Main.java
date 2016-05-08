package com.amassicci.markovbot;

public class Main {

   public static void main(String[] args)
   throws InterruptedException {
      
      // Shutdown hook setup
      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
           // TODO: Implement shutdown hook
         }
      });
      
   }
}