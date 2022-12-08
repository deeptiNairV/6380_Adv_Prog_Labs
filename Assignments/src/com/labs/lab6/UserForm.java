package com.labs.lab6;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolTip;

class UserForm extends JFrame {

    // UI Components needed for UI
    private Container container;
    private JLabel programTitle;
    private JLabel limitLabel;
    private JTextField limitUserInput;
    private JLabel theadLabel;
    private JComboBox threadUserInput;
    private JButton generatePrime;
    private JButton terminateProgram;
    private JTextArea resultViewer;
    private ExecutorImpl executorImpl = new ExecutorImpl();

    // Defined Executor, future and other variable needed for prime number & execution time computation
    private Integer thread[] = { 1, 2, 3, 4, 5 };


    public UserForm() {
        // UI Component - Below code for title and content pane
        setTitle(" (-: Prime Number Generator :-)");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(245, 240, 215));

        // UI Component - Label defined for prime number generator heading
        programTitle = new JLabel("Prime Number Generator");
        programTitle.setFont(new Font("Arial", Font.BOLD, 35));
        programTitle.setForeground(Color.BLUE);
        programTitle.setSize(450, 30);
        programTitle.setLocation(230, 30);
        container.add(programTitle);

        // UI Component - Label displayed for prime number input text expected from user
        limitLabel = new JLabel("Generate Prime Number Until : ");
        limitLabel.setFont(new Font("Arial", Font.BOLD, 18));
        limitLabel.setSize(290, 20);
        limitLabel.setLocation(265, 100);
        container.add(limitLabel);

        // UI Component - TextBox control for getting prime number input numeric value from user
        limitUserInput = new JTextField() {
            @Override
            public JToolTip createToolTip() {
                return (new CustomJToolTip(this));
            }
        };
        limitUserInput.setFont(new Font("Arial", Font.PLAIN, 18));
        limitUserInput.setSize(190, 22);
        limitUserInput.setLocation(530, 100);
        limitUserInput.addKeyListener(new KeyAdapter() {
            // UI Component Action - On entering any value in the prime number limit text box control validation
            // is done to check if the entered value is a valid numeric input if yes then combo box for thread
            // selection is enabled if invalid then thread control selection is disabled
            @Override
            public void keyReleased(KeyEvent ke) {
                try {
                    long value = Long.parseLong(limitUserInput.getText());
                    if (value > 0) {
                        threadUserInput.setEnabled(true);
                        threadUserInput.setToolTipText("Select Number Of Thread To Be Used For Execution.");
                    } else {
                        threadUserInput.setEnabled(false);
                    }
                } catch (Exception ex) {
                    threadUserInput.setEnabled(false);
                }
            }
        });

        limitUserInput.addMouseListener(new MouseAdapter() {
            // UI Component Action - When user hovers mouse over the limit input tex box a tool tip will be shown
            // with below message to advise user on the purpose of the field
            @Override
            public void mouseEntered(MouseEvent e) {
                limitUserInput.setToolTipText("Enter Valid Numeric Digit For Prime Generator");
            }
        });

        limitUserInput.setInputVerifier(new InputVerifier() {
            // UI Component Action - On click action by user the form input value in limit text box is validated
            // and if invalid then thread control selection will remain disabled with a popup error dialog box
            // indicating user input value entered is not valid for starting prime number generation execution
            @Override
            public boolean verify(JComponent input) {
                try {
                    long value = Long.parseLong(limitUserInput.getText());
                    if (value <= 0) {
                        threadUserInput.setEnabled(false);
                        showErrorMessage();
                    }
                } catch (Exception e) {
                    threadUserInput.setEnabled(false);
                    showErrorMessage();
                }
                return false;
            }
        });
        container.add(limitUserInput);

        // UI Component - Label displaying number of thread to be used for execution
        // and can be changed or selected by user for each run
        theadLabel = new JLabel("Number Of Thread To Be Used For Execution : ");
        theadLabel.setFont(new Font("Arial", Font.BOLD, 18));
        theadLabel.setSize(405, 20);
        theadLabel.setLocation(130, 150);
        container.add(theadLabel);

        // UI Component - TextBox control for getting number of thread to be used for execution from user
        // only gets enabled if the prime number input entered in previous control is a valid numeric value
        threadUserInput = new JComboBox(thread) {
            @Override
            public JToolTip createToolTip() {
                return (new CustomJToolTip(this));
            }
        };
        threadUserInput.setFont(new Font("Arial", Font.PLAIN, 18));
        threadUserInput.setSize(100, 22);
        threadUserInput.setLocation(530, 150);
        threadUserInput.setEnabled(false);
        threadUserInput.addMouseListener(new MouseAdapter() {
            // UI Component Action - When user hovers mouse over the combo box a tool tip will be shown
            // with below message to advise user on the purpose of the field
            @Override
            public void mouseEntered(MouseEvent e) {
                threadUserInput.setToolTipText("Select Number Of Thread To Be Used For Execution.");
            }
        });
        container.add(threadUserInput);

        // UI Component - Button control provided to user to start prime number generation execution
        // after input validation is successful
        generatePrime = new JButton("Generate") {
            @Override
            public JToolTip createToolTip() {
                return (new CustomJToolTip(this));
            }
        };
        generatePrime.setFont(new Font("Arial", Font.PLAIN, 15));
        generatePrime.setSize(100, 25);
        generatePrime.setForeground(Color.blue);
        generatePrime.setOpaque(true);
        generatePrime.setBorder(BorderFactory.createRaisedBevelBorder());
        generatePrime.setBackground(Color.orange);
        generatePrime.setToolTipText("Let's Go :-)");
        generatePrime.setLocation(300, 200);
        generatePrime.addMouseListener(new MouseAdapter() {
            // UI Component Action - When user mouse enters the Generate button the color of the
            // control is changed for visual indication of cursor presence on the button
            @Override
            public void mouseEntered(MouseEvent e) {
                generatePrime.setBackground(Color.GREEN);
                generatePrime.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                generatePrime.setBackground(Color.ORANGE);
                generatePrime.setForeground(Color.BLUE);
            }
        });
        generatePrime.addActionListener(e ->  {
            // Disabling Generate button so no more user action on the Generate button performed and avoiding
            // any accidental clicking action on button again by user
            resultViewer.setText("");
            generatePrime.setText("Running");
            generatePrime.setBackground(Color.GRAY);
            generatePrime.setEnabled(false);
            generatePrime.setContentAreaFilled(false);

            long initialValue = Long.parseLong(limitUserInput.getText());
            int threadCount = threadUserInput.getSelectedIndex() + 1;

            executorImpl.invokeGenerate(initialValue, threadCount, generatePrime, resultViewer, false);
        });
        container.add(generatePrime);

        // UI Component - Button control provided to user to cancel prime number generation execution
        terminateProgram = new JButton("Cancel") {
            @Override
            public JToolTip createToolTip() {
                return (new CustomJToolTip(this));
            }
        };
        terminateProgram.setFont(new Font("Arial", Font.PLAIN, 15));
        terminateProgram.setSize(100, 25);
        terminateProgram.setForeground(Color.blue);
        terminateProgram.setOpaque(true);
        terminateProgram.setBorder(BorderFactory.createRaisedBevelBorder());
        terminateProgram.setBackground(Color.orange);
        terminateProgram.setToolTipText("Really :-(");
        terminateProgram.setLocation(450, 200);
        terminateProgram.setVerifyInputWhenFocusTarget( false );
        terminateProgram.addMouseListener(new MouseAdapter() {
            // UI Component Action - When user mouse enters the Cancel button the color of the
            // control is changed for visual indication of cursor presence on the button
            @Override
            public void mouseEntered(MouseEvent e) {
                terminateProgram.setBackground(Color.red);
                terminateProgram.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                terminateProgram.setBackground(Color.ORANGE);
                terminateProgram.setForeground(Color.blue);
            }
        });
        terminateProgram.addActionListener(e -> {
            executorImpl.invokeCancel(resultViewer, false);
        });
        container.add(terminateProgram);

        // UI Component - TextArea control used for showing user prime number generated result and execution time taken by program
        resultViewer = new JTextArea(700, 250) {
            @Override
            public JToolTip createToolTip() {
                return (new CustomJToolTip(this));
            }
        };
        JScrollPane scroll = new JScrollPane(resultViewer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultViewer.setFont(new Font("Arial", Font.PLAIN, 9));
        resultViewer.setToolTipText("Prime Generator Execution Result Display Window.");
        resultViewer.setBorder(BorderFactory.createLoweredBevelBorder());
        resultViewer.setLineWrap(true);
        resultViewer.setEditable(false);
        scroll.setBounds(40, 250, 810, 290);
        container.add(scroll);
        setVisible(true);
    }

    // UI Component - Error Message popup window to show user invalid entered input value in limit text box control
    private void showErrorMessage() {
        JOptionPane.showConfirmDialog(this,
                "Enter Valid Numeric Digit For Prime Generator.", "Come On :-(",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        limitUserInput.setText("");
    }
}
