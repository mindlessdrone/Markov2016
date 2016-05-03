package com.amassicci.markovbot.utils;

import java.util.Calendar;

public class Message {
    // constants
    public static final int INFO_MSG = 0;
    public static final int WARN_MSG = 1;
    public static final int ERROR_MSG = 2;
    public static final int FATAL_MSG = 3;

    public static void display(int type, String sender, String msg) {
        String date = Calendar.getInstance().getTime().toString();
        String[] types = {"INFO", "WARN", "ERR", "FATAL"};

        System.out.println(String.format("[%s] %s - %s: %s",
                    types[type], date, sender, msg));
    }
}
