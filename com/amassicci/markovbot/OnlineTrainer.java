package com.amassicci.markovbot;

import twitter4j.*;
import com.amassicci.markovbot.utils.*;
import java.util.*;

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
    

    //-----------------------------------------------------------------
    // onStatus: Called when a Twitter sends us a new tweet to process
    // updates model with new data.
    // ----------------------------------------------------------------
    @Override
    public void onStatus(Status status) {
        String userName = status.getUser().getName();
        String text = status.getText();


        String message = String.format("Recieved from @%s: %s", userName, text);
        Message.display(Message.INFO_MSG, THREAD_NAME, message);

        // update model
        List<String> msg = new ArrayList<>();
        msg.add(text);
        model.updateModel(msg);
        Message.display(Message.INFO_MSG, THREAD_NAME, "The model has been updated.");
    }

    // implemented stubs
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
    public void onTrackLimitationNotice(int numOfLimitedStatuses) {}
    public void onStallWarning(StallWarning warning){}
    public void onScrubGeo(long userId, long upToStatusId) {}
    //Exception handling

    //--------------------------------------------------------------------
    // onException: Called when we recieve an error (Connection error,
    // Authentication issues, etc)
    // -------------------------------------------------------------------
    public void onException(Exception ex) {
        Message.display(Message.ERROR_MSG, THREAD_NAME, "An exception has occurred!, Stack trace incoming.");
        ex.printStackTrace();
    }

}


