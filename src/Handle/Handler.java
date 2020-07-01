package Handle;

import GUI.MainWindow;
import GUI.OptionsWindow;
import Lsystem.AlphabetForDraw;
import Lsystem.Rules;

import java.util.HashMap;

public class Handler {
    // not synchronized, only 1 writing with current design causes no problems
    public MainWindow main;
    public OptionsWindow options;
    public boolean changed;
    public int iteratorN;
    public StringBuilder start;
    //public boolean running; //this would be used to close whole application if i could figure out how to shut threads correctly with it, system.exit(-2) is used instead for now


    private Rules mainRules = new Rules();

    public HashMap<Character, AlphabetForDraw> getDrawRules() {
        return mainRules.getDrawRules();
    }

    public HashMap<Character, String> getRules() {
        return mainRules.getRules();
    }

    public void save(String name) {
        mainRules.save(name);
    }

    public void load(String name) {
        mainRules.load(name);
    }

    public void setDrawRules(HashMap<Character, AlphabetForDraw> drawRules) {
        mainRules.setDrawRules(drawRules);
    }

    public void setRules(HashMap<Character, String> rules) {
        mainRules.setRules(rules);
    }


}

