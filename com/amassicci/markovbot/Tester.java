package com.amassicci.markovbot;

import com.amassicci.markovbot.utils.*;
import twitter4j.*;
import twitter4j.auth.*;
import java.util.*;
import java.io.*;
import jsjf.*;

/********************************************************
 * Markov 2016
 * Author: Anthony Massicci
 * Description: A Twitter bot that generates a markov chain
 * and posts political sounding tweets.
 *
 *
 * *****************************************************/


public class Tester {
    public static final String THREAD_NAME = "Main";

    public static void main(String[] args)  {
        final String cKey = "KEY";
        final String cSecret = "SECRET";
        final String tKey = "KEY";
        final String tSecret = "SECRET";

	    final String cKey2 = "KEY";
	    final String cSecret2 = "SECRET";
	    final String tKey2 = "KEY";
	    final String tSecret2 = "SECRET";

        long[] userIds = {15087855};

        UnorderedListADT<String> lines = new ArrayUnorderedList<>();        
        Scanner scan = null;
        Markov model;

        // Build the initial state of the markov chain from file
        
        try {
            scan = new Scanner(new File("tweets.txt"));
        } catch (FileNotFoundException ex) {
            Message.display(Message.FATAL_MSG, THREAD_NAME, "Could not find initial training data, exiting!");
            System.exit(1);
        }

        while (scan.hasNext())
            lines.addToRear(scan.nextLine());
        model = new Markov(lines, 2);


        // Set up online training thread
        OnlineTrainer trainer = new OnlineTrainer(model);
        
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(trainer);
        
        twitterStream.setOAuthConsumer(cKey, cSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(tKey, tSecret));

        twitterStream.filter(new FilterQuery(userIds));

        // Set up posting thread
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(cKey2, cSecret2);
        twitter.setOAuthAccessToken(new AccessToken(tKey2, tSecret2));
       
        Poster poster = new Poster(twitter, model); 
        Thread posterThread = new Thread(poster);
        posterThread.start();

        //shutdown hook
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // shutdown threads
                Message.display(Message.INFO_MSG, THREAD_NAME, "Threads Cleaning up and closing.");
                poster.shutdown();
                twitterStream.cleanUp();
            }
        });

        while (posterThread.isAlive()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Message.display(Message.INFO_MSG, THREAD_NAME, "Main thread interrupted.");
            }
        }
    }
}

