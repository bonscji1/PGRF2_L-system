package Lsystem;

import java.util.HashMap;

/**
 * L system main class
 * call constructor, input all parameters, call get result for result
 *
 * @author Jiří Bönsch
 */

public class LSystemMain {
    private StringBuilder start;
    private HashMap rules;
    private StringBuilder result;
    private int iteratorN;

    public LSystemMain(StringBuilder start, HashMap rules, int iteratorN) {//basic deterministic
        this.start = start;
        this.rules = rules;
        this.result = new StringBuilder();
        this.iteratorN = iteratorN;

        doYourThing();
    }

    private void doYourThing() {
        StringBuilder initial = start;
        StringBuilder current = new StringBuilder();
        int length;
        char rule;
        for (int i = 0; i < iteratorN; i++) {
            length = initial.length();
            for (int j = 0; j < length; j++) {
                rule = initial.charAt(j);
                if (rules.get(rule) != null)//prevents null from sneaking in
                    try {
                        current.append(rules.get(rule));
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        System.err.println("No memory left for L-system");
                        System.exit(-2);
                    }
            }
            initial = current;
            current = new StringBuilder();
        }
        result = initial;

    }


    public StringBuilder getStart() {
        return start;
    }

    public HashMap getRules() {
        return rules;
    }

    public StringBuilder getResult() {
        return result;
    }


    public void setStart(StringBuilder start) {
        this.start = start;
    }

    public void setRules(HashMap rules) {
        this.rules = rules;
    }
}
