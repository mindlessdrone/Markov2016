package com.amassicci.markovbot;

import twitter4j.*;
import com.amassicci.markovbot.utils.*;

public class OnlineTrainer implements StatusListener {
    // Thread name
    public static final String THREAD_NAME = "Trainer";

    // fields
    private Markov model;

    // constructor
    public OnlineTrainer(Markov model_) {
        Message.display(Message.INFO_MSG, THREAD_NAME, "Thread started.");
        model = model_;
    }

    // implemented methods
    @Override
    public void onStatus(Status status) {
        String userName = status.getUser().getName();
        String text= status.getText();

        String message = String.format("Recieved from @%s: %s", userName, text);
        Message.display(Message.INFO_MSG, THREAD_NAME, message);
    }

    // implemented stubs
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
    public void onTrackLimitationNotice(int numOfLimitedStatuses) {}
    public void onStallWarning(StallWarning warning){}
    public void onScrubGeo(long userId, long upToStatusId) {}
    //Exception handling
    public void onException(Exception ex) {
        Message.display(Message.ERROR_MSG, THREAD_NAME, "An exception has occurred!, Stack trace incoming.");
        ex.printStackTrace();
    }




}


