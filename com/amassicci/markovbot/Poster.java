package com.amassicci.markovbot;

import twitter4j.*;
import com.amassicci.markovbot.utils.*;

public class Poster implements Runnable {
    //thread name
    public static final String THREAD_NAME = "Poster";

    // fields
    private Twitter twitter;
    private Markov model;
    private boolean isRunning;
    //constructor
    public Poster(Twitter twitter_, Markov model_) {
        twitter = twitter_;
        model = model_;
        isRunning = true;
        Message.display(Message.INFO_MSG, THREAD_NAME, "Hello from Poster!");
    }


    // implemented fields
    public void run() {
        while (isRunning) {

            // generate text and post
            String msg = model.generate();
            try {
                twitter.updateStatus(msg);
            } catch (TwitterException ex) {
                Message.display(Message.FATAL_MSG, THREAD_NAME, "There was an error posting to twitter.");
                isRunning = false;
                break;
            }
    
            Message.display(Message.INFO_MSG, THREAD_NAME, "Text generated and successfully posted to Twitter.");
            // take a nap for a bit to avoid rate limits
            Message.display(Message.INFO_MSG, THREAD_NAME, "Sleeping for 1m.");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Message.display(Message.WARN_MSG, THREAD_NAME, "Thread interrupted.");
                shutdown();
            }
        }
    }

    public void shutdown() {
        isRunning = false;
        Message.display(Message.INFO_MSG, THREAD_NAME, "Shutting down");
    }
}

