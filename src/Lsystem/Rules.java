package Lsystem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Rules {
    HashMap<Character, AlphabetForDraw> drawRules = new HashMap<Character, AlphabetForDraw>();
    HashMap<Character, String> rules = new HashMap();

    public Rules(HashMap<Character, AlphabetForDraw> drawInstructions) {
        this.drawRules = drawInstructions;
    }

    public Rules() {
        try {
            String path = new File("src/resources/drawRules").getAbsolutePath();
            loadInternal(path);

        } catch (Exception e) {
            System.err.println("Could not load resource");
        }

    }
    public void load(String name){
        try{
            String path = new File("src/resources/"+name).getAbsolutePath();
            loadInternal(path);
        } catch (Exception e) {
            System.err.println("Could not load resource");
        }
    }

    private void loadInternal(String source){

        try (BufferedReader br =
                     new BufferedReader(new FileReader(source))) {

            String line = br.readLine();

            while (line != null ){
                if (line.equals("***")){
                    line = br.readLine();
                    break;
                }
                String[] split= line.split(":");
                rules.put(split[0].charAt(0), split[1]);

                line = br.readLine();
            }
            while (line != null ){
                String[] split= line.split(":");
                drawRules.put(split[0].charAt(0),new AlphabetForDraw(OperationType.fromString(split[1]),Integer.parseInt(split[2]),Integer.parseInt(split[3]),Integer.parseInt(split[4])));

                line = br.readLine();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void save(){
        String path = new File("src/resources/drawRules").getAbsolutePath();
        saveInternal(path);

    }
    public void save(String name){
        String path = new File("src/resources/"+name).getAbsolutePath();
        saveInternal(path);
    }



    private void saveInternal(String destination){

        try (FileWriter file = new FileWriter(destination)) {

            String line;

            for(Map.Entry<Character, String> entry : rules.entrySet()) {
                Character key = entry.getKey();
                String rule = entry.getValue();

                line = String.format("%c:%s\n",key, rule);
                file.write(line);
            }
            file.write("***\n");//this will divide 2 rule sets


            for(Map.Entry<Character, AlphabetForDraw> entry : drawRules.entrySet()) {
                Character key = entry.getKey();
                AlphabetForDraw rule = entry.getValue();

                line = String.format("%c:%s:%d:%d:%d\n",key,rule.getType(),rule.getIntX(),rule.getIntY(),rule.getIntZ());
                file.write(line);
            }


        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Character, AlphabetForDraw> getDrawRules() {
        return drawRules;
    }

    public HashMap<Character, String> getRules() {
        return rules;
    }

    public void setDrawRules(HashMap<Character, AlphabetForDraw> drawRules) {
        this.drawRules = drawRules;
    }

    public void setRules(HashMap<Character, String> rules) {
        this.rules = rules;
    }
}
