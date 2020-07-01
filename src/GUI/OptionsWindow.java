package GUI;

import Handle.Handler;
import Lsystem.AlphabetForDraw;
import Lsystem.OperationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class OptionsWindow extends JFrame {
    private JTextField textLS, iteratorTF, startString, textX, textY, textZ, textADD, textDelete;
    private JLabel labelX, labelY, labelZ, labelOperation, labelDelete, labelADD;
    private JComboBox typeList;
    private JButton buttonDelete, buttonADD;

    private JTextArea textArea;
    private Handler handler;
    private int optionType;
    private String type;


    public OptionsWindow(Handler handler) {
        this.handler = handler;
        initFrame();
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        JPanel bottomPanel = new JPanel(new GridBagLayout());


        //top ------------------------------------------------------------------------
        JRadioButton draw = new JRadioButton("Draw rules");
        JRadioButton lSys = new JRadioButton("L system rules");
        JRadioButton controls = new JRadioButton("Controls");
        ButtonGroup bg = new ButtonGroup();
        bg.add(draw);
        bg.add(lSys);
        bg.add(controls);

        JButton save = new JButton("Save all");
        JButton load = new JButton("Load all");
        JLabel labelLS = new JLabel("File Name:");
        textLS = new JTextField("drawRules", 10);

        JLabel labelN = new JLabel("Input L-system N:");
        iteratorTF = new JTextField("1", 5);
        JButton set = new JButton("Set N");

        JLabel labelStart = new JLabel("Input start string:");
        startString = new JTextField("U", 20);
        JButton set2 = new JButton("Set start");

        GridBagConstraints topGBC = new GridBagConstraints();

        topGBC.gridx = 0;
        topGBC.gridy = 0;
        topPanel.add(labelLS, topGBC);
        topGBC.gridx = 1;
        topPanel.add(textLS, topGBC);
        topGBC.gridx = 2;
        topPanel.add(load, topGBC);
        topGBC.gridx = 3;
        topPanel.add(save, topGBC);

        topGBC.gridy = 1;
        topGBC.gridx = 0;
        topPanel.add(labelN, topGBC);
        topGBC.gridx = 1;
        topPanel.add(iteratorTF, topGBC);
        topGBC.gridx = 2;
        topPanel.add(set, topGBC);

        topGBC.gridy = 2;
        topGBC.gridx = 0;
        topPanel.add(labelStart, topGBC);
        topGBC.gridx = 1;
        topPanel.add(startString, topGBC);
        topGBC.gridx = 2;
        topPanel.add(set2, topGBC);


        topGBC.gridy = 3;
        topGBC.gridx = 0;
        topGBC.ipadx = 20;
        topPanel.add(draw, topGBC);
        topGBC.gridx = 1;
        topPanel.add(lSys, topGBC);
        topGBC.gridx = 2;
        topPanel.add(controls, topGBC);

        //center -------------------------------------------------------------------
        textArea = new JTextArea();
        textArea.setEditable(false);

        //bottom ---------------------------------------------------------------------
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //row1
        labelDelete = new JLabel("Character of the rule to delete:");
        textDelete = new JTextField("", 3);
        buttonDelete = new JButton("Delete");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        bottomPanel.add(labelDelete, gbc);
        gbc.gridx = 1;
        bottomPanel.add(textDelete, gbc);
        gbc.weightx = 1;
        gbc.gridx = 2;
        bottomPanel.add(buttonDelete, gbc);
        //row2
        String[] typeStrings = getOperations();
        typeList = new JComboBox(typeStrings);
        buttonADD = new JButton("Add");
        labelOperation = new JLabel("Operation Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(labelOperation, gbc);
        gbc.gridx = 1;
        bottomPanel.add(typeList, gbc);
        gbc.gridx = 2;
        bottomPanel.add(buttonADD, gbc);
        //row3
        labelADD = new JLabel("Character of the rule to add:");
        textADD = new JTextField("", 3);
        gbc.gridx = 0;
        gbc.gridy = 2;
        bottomPanel.add(labelADD, gbc);
        gbc.gridx = 1;
        bottomPanel.add(textADD, gbc);
        //row4
        labelX = new JLabel("labelX");
        textX = new JTextField("", 10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        bottomPanel.add(labelX, gbc);
        gbc.gridx = 1;
        bottomPanel.add(textX, gbc);
        //row5
        labelY = new JLabel("labelY");
        textY = new JTextField("", 10);
        gbc.gridx = 0;
        gbc.gridy = 4;
        bottomPanel.add(labelY, gbc);
        gbc.gridx = 1;
        bottomPanel.add(textY, gbc);
        //row6
        labelZ = new JLabel("labelZ");
        textZ = new JTextField("", 10);
        gbc.gridx = 0;
        gbc.gridy = 5;
        bottomPanel.add(labelZ, gbc);
        gbc.gridx = 1;
        bottomPanel.add(textZ, gbc);


        //main window -----------------------------------------------------
        add(bottomPanel, BorderLayout.SOUTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        //initial settings
        optionType = 0;
        updateTextArea();
        typeList.setSelectedIndex(1);
        type = "DRAW_INNER";


        //Listeners
        controls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionType = 0;
                updateTextArea();

            }
        });
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionType = 1;
                updateTextArea();
            }
        });

        lSys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionType = 2;
                updateTextArea();
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.load(textLS.getText());
                updateTextArea();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.save(textLS.getText());
            }
        });
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changed = true;
                try {
                    handler.iteratorN = Integer.parseInt(iteratorTF.getText());
                } catch (NumberFormatException ex) {
                    System.err.println("Input value to iterator is not a number");
                }

            }
        });
        set2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changed = true;
                handler.start = new StringBuilder(startString.getText());

            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changed = true;
                deleteRule(textDelete.getText());
            }
        });

        buttonADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changed = true;
                addRule();
            }
        });

        typeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.changed = true;
                JComboBox cb = (JComboBox) e.getSource();
                type = (String) cb.getSelectedItem();
                updateTextArea();
                //Visibility
                switch (OperationType.fromString(type)) {
                    case DRAW://xyz
                    case MOVE://xyz
                    case CHANGE_DRAW_LENGTH://xzy
                    case CHANGE_INNER_VECTOR://xzy
                    case CHANGE_INNER_ANGLE://xyz
                        labelX.setVisible(true);
                        labelX.setText("labelX");
                        labelY.setVisible(true);
                        labelZ.setVisible(true);
                        textX.setVisible(true);
                        textY.setVisible(true);
                        textZ.setVisible(true);
                        break;
                    case DRAW_INNER://
                        break;
                    default:
                        System.err.println("unsupported operation type");
                        break;
                }
            }
        });


    }


    private String[] getOperations() {
        String[] array = new String[OperationType.values().length];
        int counter = 0;
        for (OperationType opType : OperationType.values()) {
            array[counter] = opType.getType();
            counter++;
        }
        return array;

    }


    private void initFrame() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Handle");
        setSize(600, 800);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(false);
    }

    public void closeOptions() {
        super.dispose();
    }

    private void updateTextArea() {
        String line;
        switch (optionType) {
            case 0://options
                //Visibility
                hideAll();

                //Content
                textArea.setText("[lmb] angle camera\n" +
                        "[mouse wheel]move camera IN/OUT\n" +
                        "[WASD] move camera UP/DOWN/LEFT/RIGHT\n" +
                        "[O] open options\n" +
                        "[P] change perspective\n" +
                        "[M] animate");
                break;
            case 1:// draw
                //Visibility
                hideAll();

                buttonDelete.setVisible(true);
                buttonADD.setVisible(true);
                textADD.setVisible(true);
                textDelete.setVisible(true);
                labelADD.setVisible(true);
                labelDelete.setVisible(true);
                labelOperation.setVisible(true);
                typeList.setVisible(true);

                //content
                textArea.setText("Draw Rules\n\n");
                for (Map.Entry<Character, AlphabetForDraw> entry : handler.getDrawRules().entrySet()) {
                    Character key = entry.getKey();
                    AlphabetForDraw rule = entry.getValue();
                    if (rule.getType() == OperationType.DRAW_INNER) {
                        line = String.format("Character:    %c      Type:   %s \n", key, rule.getType());
                    } else {
                        line = String.format("Character:    %c      Type:   %s                      oArguments: %d: %d: %d \n", key, rule.getType(), rule.getIntX(), rule.getIntY(), rule.getIntZ());
                    }
                    textArea.append(line);
                }
                break;

            case 2:// L system
                //visibility
                hideAll();
                buttonDelete.setVisible(true);
                buttonADD.setVisible(true);
                textADD.setVisible(true);
                textDelete.setVisible(true);
                labelX.setVisible(true);
                textX.setVisible(true);
                labelADD.setVisible(true);
                labelDelete.setVisible(true);

                //fill with content
                labelX.setText("Rule: ");
                textArea.setText("L-system Rules\n\n");
                for (Map.Entry<Character, String> entry : handler.getRules().entrySet()) {
                    Character key = entry.getKey();
                    String rule = entry.getValue();

                    line = String.format("Character: %c     Rule: %s \n", key, rule);
                    textArea.append(line);
                }
                break;
            default:
                break;
        }
    }

    private void hideAll() {
        buttonADD.setVisible(false);
        buttonDelete.setVisible(false);

        typeList.setVisible(false);

        labelX.setVisible(false);
        labelY.setVisible(false);
        labelZ.setVisible(false);
        labelOperation.setVisible(false);
        labelDelete.setVisible(false);
        labelADD.setVisible(false);

        textX.setVisible(false);
        textY.setVisible(false);
        textZ.setVisible(false);
        textADD.setVisible(false);
        textDelete.setVisible(false);
    }

    private void deleteRule(String text) {
        StringBuilder sb = new StringBuilder(text);
        Character character = sb.charAt(0);//Here i delete just 1st input
        switch (optionType) {
            case 1:// draw
                handler.getDrawRules().remove(character);
                updateTextArea();
                break;

            case 2:// L system
                handler.getRules().remove(character);
                updateTextArea();
                break;
        }
    }

    private void addRule() {
        switch (optionType) {
            case 1:// draw
                if (type.equals("DRAW_INNER")) {
                    handler.getDrawRules().put(textADD.getText().charAt(0),
                            new AlphabetForDraw(OperationType.fromString(type), 0, 0, 0));
                } else {
                    handler.getDrawRules().put(textADD.getText().charAt(0),
                            new AlphabetForDraw(OperationType.fromString(type),
                                    Integer.parseInt(textX.getText()),
                                    Integer.parseInt(textY.getText()),
                                    Integer.parseInt(textZ.getText())));
                }
                updateTextArea();
                break;

            case 2:// L system
                handler.getRules().put(textADD.getText().charAt(0), textX.getText());
                updateTextArea();
                break;
        }
    }

}
