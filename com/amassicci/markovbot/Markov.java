package com.amassicci.markovbot;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******************************************************
 * Markov: Implements the Markov Chain used in this program
 * Author: Anthony Massicci
 *
 * fields:
 *  -static final int DEFAULT_ORDER
 *  -String currentState
 *  -List<String> memory
 *  -List<String> stateStates
 *  -Map<String List<String>> model
 *  -int order
 *
 * methods:
 *  +Markov(Iterable<String>)
 *  +Markov(Iterable<String>, int)
 *  +void updateModel(Iterable<String>)
 *  -void next()
 *  +String generate()
 *  ************************************************/

public class Markov {
    private static final int DEFAULT_ORDER = 1;

    // state
    private String currentState;
    private List<String> memory;
    private List<String> startStates;
    private Map<String, List<String>> model;
    private int order;

    public Markov(Iterable<String> strings) {
        this(strings, DEFAULT_ORDER);
    }

    public Markov(Iterable<String> strings, int order_) {
        // init state
        memory = new ArrayList<>();
        startStates = new ArrayList<>();
        model = new HashMap<>();
        currentState = null;
        order = order_;

        // BUILD THE MODEL!!!!!!!!!!!!!!!!!
        updateModel(strings);
    }

    //--------------------------------------------------------------------
    // updateModel: Takes an iterable object of strings and updates model
    // with appropiate keys and probabilities. This is basically where the
    // magic happens.
    // ------------------------------------------------------------------
    public synchronized void updateModel(Iterable<String> strings) {
        // Regex shit
        String regexStr = "((?:\\S+\\s){%d}\\S+)(?:\\s(\\S+)){0,1}";
        Pattern regex = Pattern.compile(String.format(regexStr, order-1));
        Matcher matcher;
        int pos;

        // local variables to store matches
        String key, value;

        for (String string  : strings) {
            matcher = regex.matcher(string);
            pos = 0;

            while (matcher.find(pos)) {
                // Get our key and value from the regex matches
                key = matcher.group(1);
                value = matcher.group(2);

                // If pos is 0, this is the first item
                // Add it to the starting states
                if (pos == 0) startStates.add(key);

                // if the key is not in the model already, initialize a new ArrayList
                if (!model.containsKey(key))
                    model.put(key, new ArrayList<>());

                // Add value to the list under the current key
                model.get(key).add(value);

                // Advance position of string by finding next space
                pos = string.indexOf(" ", pos) + 1;
            }
        }
    }

    //----------------------------------------------------------------
    // next: Used to perform a random walk through our model, for the
    // funnys.
    // ---------------------------------------------------------------
    private String next() {
        Random r = new Random();
        int choice;
        String currentMem;

        if (currentState == null) {
            choice = r.nextInt(startStates.size());
            currentState = startStates.get(choice);
            memory.addAll(Arrays.asList(currentState.split(" ")));
            return currentState;
        }

        currentMem = String.join(" ", memory.subList(memory.size() - order, memory.size()));
        choice = r.nextInt(model.get(currentMem).size());
        currentState = model.get(currentMem).get(choice);
        memory.add(currentState);

        return currentState;
    }

    //--------------------------------------------------------
    // generate: Used by external programs to generate text,
    //--------------------------------------------------------
    public synchronized String generate() {
        String word;
        List<String> words = new ArrayList<>();

        while ((word = next()) != null) {
            words.add(word);
        }

        return String.join(" ", words);
    }

    // Early testing stuff
    public static void unittest() throws IOException {
        Scanner scan = new Scanner(new File("trumptweets.txt"));
        List<String> lines = new ArrayList<>();
        Markov test;

        while (scan.hasNext())
            lines.add(scan.nextLine());

        test = new Markov(lines, 2);
        for (int i = 0; i < 10; i++)
            System.out.println(test.generate());
    }
}
