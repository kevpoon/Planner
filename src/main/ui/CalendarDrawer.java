package ui;

import model.*;
import model.Event;
import persistence.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Calendar;
import java.util.Timer;

import static java.awt.Component.CENTER_ALIGNMENT;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

//The while loop and menu designs were inspired by TellerApp
// This class runs the calendarDrawer
// the GUI portion is based off of phase 3 example
public class CalendarDrawer implements ActionListener {
    private Year year;
    private static final String JSON_STORE = "./data/year.json";
    private Scanner input;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // different frames
    JFrame mainFrame;
    JFrame startMenu;
    JFrame daySelector;
    JFrame yearSelector;
    JFrame budgetFrameSet;
    JFrame budgetFrameAdd;
    JFrame budgetFrameSubtract;
    JFrame addTransactionFrame;
    JFrame removeTransactionFrame;



    JPanel budgetPanel;
    JPanel jpanel;
    JPanel mainFrameEastPanel;
    JPanel transactionPanel;
    JTable monthVisuals;
    JPanel transactionModifyPanel;

    // must use .setText to update
    JLabel budgetSummary;
    JLabel budgetInformationLabelOne;
    JLabel budgetInformationLabelTwo;
    JLabel daylabel;
    JLabel total;


    private boolean right = true;
    private boolean remove = true;
    JLabel visualBoop;
    ImageIcon boopLeft;
    ImageIcon boopRight;

    ArrayList<JLabel> creditList;
    ArrayList<JLabel> debitList;
    JTextField month;
    JTextField day;
    JTextField userInput;
    JTextField transactionName;
    JTextField transactionId;
    JTextField transactionAmount;
    JTextField year1;

    JComboBox<String> debitorcredit;

    private String selectedMonth;
    private String selectedDay;
    private String selectedYear;

    private Day currentDay;

    //REQUIRES: A valid Year object
    //EFFECTS: Creates a CalendarDrawer object basing all generation on the Year object provided
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public CalendarDrawer() throws FileNotFoundException {
        input = new Scanner(System.in);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        runVisualGUI();
//        runCalendarDrawer();

    }

    // MODIFIES: this
    // EFFECTS: sets the default day and month to 1 upon initializing the GUI. This is the default day
    public void setDefaultDay() {
        selectedDay = "1";
        selectedMonth = "1";
        int monthIndex = Integer.parseInt(selectedMonth) - 1;
        int dayIndex = Integer.parseInt(selectedDay) - 1;
        currentDay = year.getDays().get(monthIndex).get(dayIndex);
    }

    // MODIFIES: this
    // EFFECTS: initializes the boop images as ImageIcons
    public void initializeBoops() {
        boopRight = new ImageIcon("./data/boop.png");
        boopLeft = new ImageIcon("./data/boop2.png");
        visualBoop = new JLabel(boopRight);
        visualBoop.setIcon(boopRight);

    }

    //MODIFIES: this
    //EFFECTS: runs the moving boop image. switches the images boop.png and boop2.png
    public void runBoop() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (right) {
                    visualBoop.setIcon(boopLeft);
                    right = false;
                } else {
                    visualBoop.setIcon(boopRight);
                    right = true;
                }
            }
        }, 2000, 5000);
    }

    // MODIFIES: this
    // EFFECTS: constructs the visual components for GUI
    public void runVisualGUI() {

        startMenu = new JFrame();
        startMenu.setLayout(new FlowLayout());
        startMenu.setBackground(Color.cyan);


        JButton newPlanner = new JButton("Start a new planner");
        newPlanner.addActionListener(this);
        newPlanner.setActionCommand("startNewPlanner");
        JButton loadPlanner = new JButton("Load save");
        loadPlanner.addActionListener(this);
        loadPlanner.setActionCommand("loadSavePlanner");

        ImageIcon boop = new ImageIcon("./data/boop.png");
        JLabel boopButton = new JLabel(boop);

        JButton quit = new JButton("Quit");
        quit.addActionListener(this);
        quit.setActionCommand("quit");

        startMenu.add(newPlanner);
        startMenu.add(loadPlanner);
        startMenu.add(boopButton);
        startMenu.add(quit);

        startMenu.pack();
        startMenu.setLocationRelativeTo(null);
        startMenu.setVisible(true);
        startMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }


    //MODIFIES: this
    //EFFECTS: creates a transactionModifyPanel containing the buttons to modify the transactionpanel
    private JPanel transactionModifyPanel() {
        transactionModifyPanel = new JPanel();
        transactionModifyPanel.setLayout(new BoxLayout(transactionModifyPanel,Y_AXIS));

        JButton addTransaction = new JButton("Add a transaction");
        addTransaction.addActionListener(this);
        addTransaction.setActionCommand("runAddTransactionButton");

        JButton removeTransaction = new JButton("Remove a transaction");
        removeTransaction.addActionListener(this);
        removeTransaction.setActionCommand("runRemoveTransactionButton");

        JButton selectDay = new JButton("Select different day to view");
        selectDay.addActionListener(this);
        selectDay.setActionCommand("selectDayButton");


        transactionModifyPanel.add(selectDay);
        transactionModifyPanel.add(addTransaction);
        transactionModifyPanel.add(removeTransaction);
        initializeBoops();
        transactionModifyPanel.add(visualBoop);
        runBoop();
        transactionModifyPanel.setBorder(new LineBorder(Color.BLACK));




        return transactionModifyPanel;
    }

    //REQUIRES: userinput must input a String for transactionName, a double/integer for transactionAmount
    //MODIFIES: this
    //EFFECTS: performs the user interaction through a new popup JFrame
    private void runAddTransaction() {
        addTransactionFrame = new JFrame();
        addTransactionFrame.setLocationRelativeTo(null);
        addTransactionFrame.setLayout(new FlowLayout());

        transactionName = new JTextField("Enter transaction name here");
        transactionAmount = new JTextField("Enter transaction amount here");
        String[] temp = new String[] {"Purchase","Deposit"};
        debitorcredit = new JComboBox<>(temp);

        JButton confirm = new JButton("Confirm");
        confirm.setActionCommand("dotheAddTransaction");
        confirm.addActionListener(this);

        addTransactionFrame.add(transactionName);
        addTransactionFrame.add(transactionAmount);
        addTransactionFrame.add(debitorcredit);
        addTransactionFrame.add(confirm);

        addTransactionFrame.pack();
        addTransactionFrame.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: performs the actual addition of a transaction to the year, and updates the appropriate displays
    private void dotheAddTransaction() {
        String description = "";
        if (debitorcredit.getSelectedItem().equals("Purchase")) {
            description = "credit";
        } else if (debitorcredit.getSelectedItem().equals("Deposit")) {
            description = "debit";
        }

        Transaction tempTransaction = new Transaction(Double.parseDouble(transactionAmount.getText()),
                description,transactionName.getText());
        year.setTransaction(Integer.parseInt(selectedMonth),Integer.parseInt(selectedDay),tempTransaction);
        addTransactionFrame.dispose();
        if (!currentDay.getDebits().isEmpty()) {
//            System.out.println(currentDay.getTotalDebits());
        } else {
//            System.out.println("Debits empty");
        }

        if (!currentDay.getCredits().isEmpty()) {
//            System.out.println(currentDay.getTotalCredits());
        } else {
//            System.out.println("Credits empty");
        }
        updateBudgetInformation();
    }

    //REQUIRES: userinput must be an integer corresponding to a transaction
    //MODIFIES: this
    //EFFECTS: deals with the user interaction for removing a transaction. user inputs id only
    private void runRemoveTransaction() {
        removeTransactionFrame = new JFrame();
        removeTransactionFrame.setLocationRelativeTo(null);
        removeTransactionFrame.setLayout(new FlowLayout());

        transactionId = new JTextField("Enter transaction id here");

        JButton confirm = new JButton("Confirm");
        confirm.setActionCommand("dotheRemoveTransaction");
        confirm.addActionListener(this);

        removeTransactionFrame.add(transactionId);
        removeTransactionFrame.add(confirm);

        removeTransactionFrame.pack();
        removeTransactionFrame.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: performs the actual removing of a transaction
    private void dotheRemoveTransaction() {
        year.removeTransaction(Integer.parseInt(selectedMonth),Integer.parseInt(transactionId.getText()));
        removeTransactionFrame.dispose();
        updateBudgetInformation();
        updateTransactionPanel();
    }

    //MODIFIES: this
    //EFFECTS: creates an overall budgetPanel containing all the information about the budget (display)
    private JPanel budgetPanel() {

        budgetPanel = new JPanel();
        int m = Integer.parseInt(selectedMonth);
        String resultstart = ("<html>Your monthly budget is set to : " + year.getmonthlyBudget(m)
                + "<br/>You have made "
                + (int)year.totalMonthlyCreditNumber(m) + " purchases this month = "
                + year.totalMonthlyCreditValue(m) + "</html>");

        budgetSummary = new JLabel(resultstart);
        budgetPanel.setLayout(new BorderLayout());
        budgetPanel.add(budgetSummary, BorderLayout.NORTH);
        budgetPanel.add(budgetInformationPanel(m),BorderLayout.WEST);
        budgetPanel.add(modifyBudgetPanel(), BorderLayout.EAST);
        budgetPanel.setBorder(new LineBorder(Color.black));

        return budgetPanel;
    }

    //MODIFIES: this
    //EFFECTS: creates a budgetInformationPanel that displays information
    public JPanel budgetInformationPanel(int m) {
        String displayResult = "";
        String displayResultCalculated = "";
        if (year.totalMonthlyCreditValue(m) > year.getmonthlyBudget(m)) {
            displayResult += ("<html><br/>You should consider spending less! "
                    + "<br/>You have exceeded your budget this month!</html>");
        } else {
            displayResult += ("<html><br/>You have been keeping your budget so far. "
                    + "<br/>You have " + year.getRemainingMonthlyBudget(m)
                    + " remaining on your budget. " + "<br/>Keep up the good work!</html>");
        }
        displayResultCalculated += ("<html><br/>Your daily budget has been calculated to be "
                + (int)year.dailyBudgetFromMonth(m) + " a day. <br/>Try to keep your daily purchases under this amount!"
                + ("<br/> <br/> Keep in mind you have a total income of " + year.totalMonthlyDebitValue(m)
                + " saved up. <br/>Try not to exceed your available savings.</html>"));
        if (budgetInformationLabelOne == null) {
            budgetInformationLabelOne = new JLabel(displayResult);
            budgetInformationLabelTwo = new JLabel((displayResultCalculated));
        }

        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, Y_AXIS));
        temp.add(budgetInformationLabelOne);
        temp.add(budgetInformationLabelTwo);
        return temp;
    }

    //MODIFIES: this
    //EFFECTS: updates the budget information so the user will see the changes they made
    public void updateBudgetInformation() {
        int m = Integer.parseInt(selectedMonth);
        String displayResult = "";
        String displayResultCalculated = "";
        if (year.totalMonthlyCreditValue(m) > year.getmonthlyBudget(m)) {
            displayResult += ("<html><br/>You should consider spending less! "
                    + "<br/>You have exceeded your budget this month!</html>");
        } else {
            displayResult += ("<html><br/>You have been keeping your budget so far. "
                    + "<br/>You have " + year.getRemainingMonthlyBudget(m)
                    + " remaining on your budget. " + "<br/>Keep up the good work!</html>");
        }
        displayResultCalculated += ("<html><br/>Your daily budget has been calculated to be "
                + (int) year.dailyBudgetFromMonth(m)
                + " a day. <br/>Try to keep your daily purchases under this amount!"
                + ("<br/>Keep in mind you have a total income of " + year.totalMonthlyDebitValue(m)
                + " saved up. <br/>Try not to exceed your available savings.</html>"));

        String resultstart = ("<html>Your monthly budget is set to : " + year.getmonthlyBudget(m)
                + "<br/>You have made "
                + (int)year.totalMonthlyCreditNumber(m) + " purchases this month = "
                + year.totalMonthlyCreditValue(m) + "</html>");

        budgetSummary.setText(resultstart);

        budgetInformationLabelOne.setText(displayResult);
        budgetInformationLabelTwo.setText(displayResultCalculated);
    }


    //MODIFIES: this
    //EFFECTS: calls the appropriate response depending on user interaction
    @Override
    public void actionPerformed(ActionEvent e) {
        actionPeformedTransactions(e);
        actionPerformedMonthlyBudget(e);
        if (e.getActionCommand().equals("startNewPlanner")) {
            runYearSelector();
        } else if (e.getActionCommand().equals("loadSavePlanner")) {
            tryLoadYear();
            runMainFrame();
            updateTransactionPanel();
        } else if (e.getActionCommand().equals("selectDayButton")) {
            runDaySelector();
        } else if (e.getActionCommand().equals("save")) {
            saveYear();
        } else if (e.getActionCommand().equals("selectDay")) {
            doSelectDay();

        } else if (e.getActionCommand().equals("runYearSelect")) {
            dotheYearSelector();
        } else if (e.getActionCommand().equals("quit")) {
//            mainFrame.dispose();
            for (Event o:EventLog.getInstance()) {
                System.out.println(o.toString());
            }
            startMenu.dispose();
        }
    }

    //MODIFIES: this
    //EFFECTS: calls the appropriate response depending on user interaction
    public void actionPeformedTransactions(ActionEvent e) {
        if (e.getActionCommand().equals("runAddTransactionButton")) {
            runAddTransaction();
        } else if (e.getActionCommand().equals("dotheAddTransaction")) {
            dotheAddTransaction();
            updateTransactionPanel();
        } else if (e.getActionCommand().equals("runRemoveTransactionButton")) {
            runRemoveTransaction();
        } else if (e.getActionCommand().equals("dotheRemoveTransaction")) {
            dotheRemoveTransaction();
        }
    }

    //MODIFIES: this
    //EFFECTS: calls the appropriate response depending on user interaction
    public void actionPerformedMonthlyBudget(ActionEvent e) {
        if (e.getActionCommand().equals("runSetMonthlyBudgetButton")) {
            runSetBudget();
        } else if (e.getActionCommand().equals("runAddMonthlyBudgetButton")) {
            runAddBudget();
        } else if (e.getActionCommand().equals("runSubtractMonthlyBudgetButton")) {
            runSubtractBudget();
        } else if (e.getActionCommand().equals("setMonthlyBudget")) {
            year.setMonthlyBudget(Double.parseDouble(userInput.getText()), Integer.parseInt(selectedMonth));
            budgetFrameSet.dispose();
            updateBudgetInformation();
            budgetPanel.repaint();
//            System.out.println(year.getmonthlyBudget(Integer.parseInt(selectedMonth)));
        } else if (e.getActionCommand().equals("addMonthlyBudget")) {
            year.addToMonthlyBudget(Double.parseDouble(userInput.getText()), Integer.parseInt(selectedMonth));
            budgetFrameAdd.dispose();
            updateBudgetInformation();
            budgetPanel.repaint();
        } else if (e.getActionCommand().equals("subtractMonthlyBudget")) {
            year.removeFromMonthlyBudget(Double.parseDouble(userInput.getText()), Integer.parseInt(selectedMonth));
            budgetFrameSubtract.dispose();
            updateBudgetInformation();
            budgetPanel.repaint();
        }
    }



    // MODIFIES: this
    // EFFECTS: creates a modifyBudgetPanel containing the buttons for budget
    public JPanel modifyBudgetPanel() {
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new BoxLayout(modifyPanel, Y_AXIS));

        JButton setBudget = new JButton("Set new budget");
        setBudget.addActionListener(this);
        setBudget.setActionCommand("runSetMonthlyBudgetButton");

        JButton addBudget = new JButton("Add to budget");
        addBudget.addActionListener(this);
        addBudget.setActionCommand("runAddMonthlyBudgetButton");

        JButton subtractBudget = new JButton("Remove from budget");
        subtractBudget.addActionListener(this);
        subtractBudget.setActionCommand("runSubtractMonthlyBudgetButton");

        ImageIcon boop = new ImageIcon("./data/boop2.png");
        JLabel boopButton = new JLabel(boop);



        modifyPanel.add(setBudget);
        modifyPanel.add(addBudget);
        modifyPanel.add(subtractBudget);

        modifyPanel.add(boopButton);



        return modifyPanel;
    }

    //MODIFIES: this
    //EFFECTS: deals with the setBudget
    public void runSetBudget() {
        budgetFrameSet = new JFrame();
        budgetFrameSet.setLocationRelativeTo(null);
        budgetFrameSet.setLayout(new FlowLayout());
        budgetFrameSet.setVisible(true);

        userInput = new JTextField("Please enter new monthly budget");
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setActionCommand("setMonthlyBudget");
        budgetFrameSet.add(userInput);
        budgetFrameSet.add(confirm);

        budgetFrameSet.pack();
    }

    //REQUIRES: userInput must be a double or integer
    //MODIFIES: this
    //EFFECTS: deals with the user interaction for adding a budget
    public void runAddBudget() {
        budgetFrameAdd = new JFrame();
        budgetFrameAdd.setLocationRelativeTo(null);
        budgetFrameAdd.setLayout(new FlowLayout());
        budgetFrameAdd.setVisible(true);

        userInput = new JTextField("Please enter value to add to budget");
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setActionCommand("addMonthlyBudget");
        budgetFrameAdd.add(userInput);
        budgetFrameAdd.add(confirm);

        budgetFrameAdd.pack();
    }

    //REQUIRES: userInput must be a double or integer.
    //MODIFIES: this
    //EFFECTS: deals with the user interaction for removing a budget
    public void runSubtractBudget() {
        budgetFrameSubtract = new JFrame();
        budgetFrameSubtract.setLocationRelativeTo(null);
        budgetFrameSubtract.setLayout(new FlowLayout());
        budgetFrameSubtract.setVisible(true);

        userInput = new JTextField("Please enter value to subtract from budget");
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setActionCommand("subtractMonthlyBudget");
        budgetFrameSubtract.add(userInput);
        budgetFrameSubtract.add(confirm);

        budgetFrameSubtract.pack();
    }

    //MODIFIES: this
    //EFFECTS: generates a monthVisual for the month in the form of a JTable
    public JTable generateMonthVisual() {
        int currentMonth = Integer.parseInt(selectedMonth);

        String[] strings = new String[5];
        strings = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Day[][] dayList = generateMonthPrintVisual(year,currentMonth);
        String[][] stringList = convertDayToStringArray(dayList);


        JTable newjtable = new JTable(stringList,strings);
        monthVisuals = newjtable;
        monthVisuals.setSize(300,300);
        monthVisuals.setGridColor(Color.lightGray);
        newjtable.setBorder(new LineBorder(Color.black));
//        newjtable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        for (int i = 0; i < dayList[0].length; i++) {
            newjtable.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
        newjtable.setFocusable(false);
        newjtable.setRowSelectionAllowed(false);

        return newjtable;

    }

    //MODIFIES: this
    //EFFECTS: updates the month visual panel
    public void updateMonthVisual() {
        mainFrame.remove(monthVisuals);
        monthVisuals.setVisible(false);
        monthVisuals = generateMonthVisual();
        jpanel.add(monthVisuals,BorderLayout.CENTER);
        jpanel.repaint();
    }

    //REQUIRES: valid Day[][]
    //EFFECTS: converts the Day[][] to a String[][] for graphical display usages
    public String[][] convertDayToStringArray(Day[][] day) {
        String[][] result = new String[day.length + 1][day[0].length];
        String[] title = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < day[0].length; i++) {
            result[0][i] = title[i];
        }


        for (int i = 0; i < day.length; i++) {
            for (int j = 0; j < day[i].length; j++) {
                result[i + 1][j] = Integer.toString(day[i][j].getDay());
                if (result[i + 1][j].equals("0")) {
                    result[i + 1][j] = "";
                }
            }
        }
        return result;
    }

    //MODIFIES: this
    //EFFECTS: creates a displayPanel that displays the current month and day
    public JPanel displayPanel() {
        JPanel panel = new JPanel();
//        System.out.println(currentDay);

        daylabel = new JLabel(Integer.toString(currentDay.getDay()));
        updateLabel();
        panel.add(daylabel);
        return panel;
    }

    //MODIFIES: this
    //EFFECTS: creates a transactionPanel displaying transaction information for curre
    private JPanel transactionPanel() {
        transactionPanel = new JPanel();
        transactionPanel.setLayout(new BoxLayout(transactionPanel, Y_AXIS));

        JLabel title = new JLabel("Paycheques/purchases today:");
        transactionPanel.add(title);
        String empty = "<html><br>Nothing for now!<html>";

        String creditResult = "";
        String debitResult = "";
        creditResult = processCredit(creditResult);
        debitResult = processDebit(debitResult);

        String totalResult = creditResult + debitResult;
        if (totalResult.equals("")) {
            totalResult += empty;
        }
        total = new JLabel(totalResult);
        transactionPanel.add(total);
        return transactionPanel;
    }

    //MODIFIES: this
    //EFFECTS: updates the transactionpanel graphically for display
    public void updateTransactionPanel() {
        String empty = "<html><br>Nothing for now!<html>";
        String creditResult = "";
        String debitResult = "";
        creditResult = processCredit(creditResult);
        debitResult = processDebit(debitResult);

        String totalResult = creditResult + debitResult;
        if (totalResult.equals("")) {
            totalResult += empty;
        }
        total.setText(totalResult);
        transactionModifyPanel.repaint();
        transactionPanel.repaint();
        mainFrame.repaint();
    }

    //MODIFIES: this
    //EFFECTS: creates a string to display regarding the credits stored within the day
    public String processCredit(String initial) {
        String initialprocess = initial;
        if (!currentDay.getCredits().isEmpty()) {
            for (int i = 0; i < currentDay.getCredits().size(); i++) {
                initialprocess += addCreditString(i);
            }
        }
        return initialprocess;
    }

    //MODIFIES: this
    //EFFECTS: creates a string to display regarding the debits stored within the day
    public String processDebit(String initial) {
        String initialprocess = initial;
        if (!currentDay.getDebits().isEmpty()) {
            for (int i = 0; i < currentDay.getDebits().size(); i++) {
                initialprocess += addDebitString(i);
            }
        }
        return initialprocess;
    }

    //MODIFIES: this
    //EFFECTS: gets the string for the specific index in the arraylist for the day
    public String addCreditString(int i) {
        String result = "";
        Transaction currentTransaction = currentDay.getCredits().get(i);

        result += ("<html><br>Id:" + currentTransaction.getId() + " ["
                + currentTransaction.getLabel() + " costed you " + currentTransaction.getAmount() + "]<html>");
        return result;
    }

    //MODIFIES: this
    //EFFECTS: gets the string for the specific index in the arraylist for the day
    public String addDebitString(int i) {
        String result = "";
        Transaction currentTransaction = currentDay.getDebits().get(i);

        result += ("<html><br>Id:" + currentTransaction.getId() + " ["
                + currentTransaction.getLabel() + " deposited " + currentTransaction.getAmount()
                + " into your savings]<html>");
        return result;
    }

    //MODIFIES: this
    //EFFECTS: sets the currentDay field according to the monthIndex and dayIndex fields
    public void setCurrentDay() {
        int monthIndex = Integer.parseInt(selectedMonth) - 1;
        int dayIndex = Integer.parseInt(selectedDay) - 1;
        currentDay = year.getDays().get(monthIndex).get(dayIndex);
        updateLabel();
    }

    //MODIFIES: this
    //EFFECTS: updates the label displaying month and day
    public void updateLabel() {
        String finalLabel = "";
        int fmonth = Integer.parseInt(selectedMonth);
        int fday = Integer.parseInt(selectedDay);

        String monthstring = printMonth(fmonth);
        year.getDays().get(fmonth - 1).get(fday - 1);

        finalLabel += monthstring;
        finalLabel = finalLabel + " " + fday + ", " + year.getYear();

        daylabel.setText(finalLabel);
    }

    //MODIFIES: this
    //EFFECTS: runs the mainframe
    public void runMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setBackground(Color.cyan);

        setDefaultDay();
        JScrollPane dailyTransactions = new JScrollPane();
        addMenuBar();

        //JPanel containing displayPanel and TransactionPanel
        jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        jpanel.add(generateMonthVisual(), BorderLayout.CENTER);
        jpanel.add(displayPanel(), BorderLayout.NORTH);
        jpanel.add(transactionPanel(), BorderLayout.WEST);
        jpanel.add(transactionModifyPanel(), BorderLayout.SOUTH);

        mainFrameEastPanel = new JPanel();
        mainFrameEastPanel.setLayout(new BorderLayout());
        mainFrameEastPanel.add(budgetPanel());


        //mainFrame containing selectDay, and jpanel (and menuBar from earlier)
        mainFrame.add(budgetPanel, BorderLayout.EAST);
        mainFrame.add(jpanel,BorderLayout.CENTER);




        mainFrame.pack();
        mainFrame.setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: updates the year based on the user selected year (creates a new year object)
    public void updateYear() {
        year = new Year(Integer.parseInt(selectedYear));
    }

    //MODIFIES: this
    //EFFECTS: adds a menubar to the mainframe with functional buttons
    public void addMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu options = new JMenu("Options");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("save");
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(this);
        load.setActionCommand("load");

        file.add(save);
//        file.add(load);
        menubar.add(file);
//        menubar.add(options);

        mainFrame.add(menubar,BorderLayout.NORTH);
    }

    //REQUIRES: user must input an int
    //MODIFIES: this
    //EFFECTS: runs the year selector for the user to interact.
    public void runYearSelector() {
        yearSelector = new JFrame();
        yearSelector.setLayout(new FlowLayout());
        yearSelector.setLocationRelativeTo(null);

        year1 = new JTextField("Enter year here");

        JButton confirm = new JButton("Confirm");

        confirm.addActionListener(this);
        confirm.setActionCommand("runYearSelect");

        yearSelector.add(year1);
        yearSelector.add(confirm);
        yearSelector.pack();
        yearSelector.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: actually assigns the year and runs the mainframe after the initial selecting
    public void dotheYearSelector() {
        selectedYear = year1.getText();
        updateYear();
        yearSelector.dispose();
        runMainFrame();
    }

    //REQUIRES: user must enter valid ints for day and month
    //MODIFIES: this
    //EFFECTS: deals with the user interaction to select a day
    public void runDaySelector() {
        daySelector = new JFrame();
        daySelector.setLayout(new FlowLayout());
        daySelector.setLocationRelativeTo(null);

        day = new JTextField("Enter day here");
        month = new JTextField("Enter month here");

        JButton confirm = new JButton("Confirm date");

        confirm.addActionListener(this);
        confirm.setActionCommand("selectDay");

        daySelector.add(month);
        daySelector.add(day);
        daySelector.add(confirm);

        daySelector.pack();
        daySelector.setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: selects the day and updates all visual components of mainFrame to reflect the change
    private void doSelectDay() {
        selectedDay = day.getText();
        selectedMonth = month.getText();
        setCurrentDay();
        updateLabel();
        updateMonthVisual();
        updateTransactionPanel();
        updateBudgetInformation();
        daySelector.dispose();
    }


    //EFFECTS: Runs the runCalendarDraw method, which contains the UI loop.
    //NOTE: This style of console based UI was inspired by TellerApp, the sample project provided as a sample project
//    @SuppressWarnings("methodlength")
    //reason for supress: this UI is console based only until the graphical interface is introduced. it would be very
    //difficult to condense at this point since most of it is dealing with user interaction. scanners/printing


//    public void runCalendarDrawer() throws FileNotFoundException {
//        boolean keepGoing = true;
//        int userCommand = 0;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Would you like to start a new planner?\n1. Start new planner\n2. Load save");
//        userCommand = scanner.nextInt();
//        if (userCommand == 1) {
//            System.out.println("Which year would you like to plan for?");
//            int userInputYear = scanner.nextInt();
//            Year yearObject = new Year(userInputYear);
//            year = yearObject;
//        } else {
//            loadYear();
//        }
//        System.out.println("Which month would you like to view? Please enter month number");
//        int userInputMonth = scanner.nextInt();
//
//        Day[][] monthVisual = generateMonthPrintVisual(year,userInputMonth);
//        visualizeMonth(monthVisual,userInputMonth);
//
//        while (keepGoing) {
//            displayMonthMenu();
//            userCommand = scanner.nextInt();
//            userInputMonth = processCommandMonth(userCommand,userInputMonth,monthVisual);
//
//            if (userCommand == 9) {
//                keepGoing = false;
//                dealWithQuit();
//            }
//        }
//        System.out.println("Goodbye! Good luck on budgeting!");
//    }



    //EFFECTS: deals with the Quit scenario
    public void dealWithQuit() {
        Scanner sc = new Scanner(System.in);
        int userInput;
        System.out.println("Would you like to save?\n1. Yes\n2. No");
        userInput = sc.nextInt();
        if (userInput == 1) {
            saveYear();
        }
    }
//     EFFECTS: displays options for user command processCommandMonth to deal with

    public void displayMonthMenu() {
        System.out.println("Select from by typing the number:");
        System.out.println("1. View day");
        System.out.println("2. Review monthly spending summary");
        System.out.println("3. Set monthly budget");
        System.out.println("4. Switch to another month");
        System.out.println("5. Add a paycheque or purchase");
        System.out.println("6. Remove a paycheque or purchase with id");
        System.out.println("7. Save year");
        System.out.println("8. Load year");
        System.out.println("9. Quit");
    }

    //REQUIRES: int command, valid month 1-12, 2d primitive array, and year
    //EFFECTS: deals with user, according to userInput runs the appropriate choice
    private int processCommandMonth(int command, int m,Day[][] monthVisual) {
        if (command == 1) {
            selectDay(m, year);
        } else if (command == 2) {
            getMonthlyBudget(m);
        } else if (command == 3) {
            setMonthlyBudget(m);
        } else if (command == 4) {
            return switchToAnotherMonth(command,m, monthVisual);
        } else if (command == 5) {
            addTransaction(m,monthVisual);
        } else if (command == 6) {
            removeTransaction(m);
        } else if (command == 7) {
            saveYear();
        } else if (command == 8) {
            tryLoadYear();
        } else {
            if (command != 9) {
                System.out.println("You have not selected a valid choice, please try again");
            }
        }
        return m;
    }

    //MODIFIES: this
    //Robust version of LoadYear, which catches the FileNotFoundException, and won't break the program if
    //the user tries to load a save that is not there.
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void tryLoadYear() {
        try {
            loadYear();
        } catch (FileNotFoundException e) {
            System.out.println("Save file not found. Remember to save next time!");
        }
    }

    // MODIFIES: JSON file, specifically stored at JSON_STORE location
    // EFFECTS: saves the year to file
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void saveYear() {
        try {
            jsonWriter.open();
            jsonWriter.write(year);
            jsonWriter.close();
            System.out.println("Saved year " + year.getYear() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

//     MODIFIES: this
//     EFFECTS: loads year from file
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void loadYear() throws FileNotFoundException {
        try {
            year = jsonReader.read();
            System.out.println("Loaded year " + year.getYear() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            throw new FileNotFoundException();
        }
    }

    //REQUIRES: valid month, and later userInput must be a valid id
    //EFFECTS: removes transaction based on transaction id for given month
    private void removeTransaction(int m) {
        Scanner sc = new Scanner(System.in);
        int temp;
        System.out.println("Which paycheque or purchase would you like to remove?"
                + "\nPlease enter id found on the left of your deposit/purchase in View Day to remove order");
        temp = sc.nextInt();
        if (year.removeTransaction(m, temp) == true) {
            System.out.println("Order with id: " + temp + " has been deleted");
        } else {
            System.out.println("Order id " + temp + " not found. Order not deleted");
        }

    }

    //REQUIRES: valid month 1-12
    //EFFECTS: sets the monthly budget for month
    private void setMonthlyBudget(int m) {
        Scanner sc = new Scanner(System.in);
        double temp;
        System.out.println("What do you want to set your monthly budget to?");
        double userInput = sc.nextDouble();
        year.setMonthlyBudget(userInput, m);
    }

    //REQUIRES: valid month 1-12
    //EFFECTS: gets monthly budget stored in Year object
    private void getMonthlyBudget(int m) {
        Scanner sc = new Scanner(System.in);
        double temp;
        System.out.println("Your monthly budget is set to : " + year.getmonthlyBudget(m) + "\nYou have made "
                + (int)year.totalMonthlyCreditNumber(m) + " purchases this month = " + year.totalMonthlyCreditValue(m));

        if (year.totalMonthlyCreditValue(m) > year.getmonthlyBudget(m)) {
            System.out.println("You should consider spending less! You have exceeded your budget this month!");
        } else {
            System.out.println("You have been keeping your budget so far. You have " + year.getRemainingMonthlyBudget(m)
                    + " remaining on your budget. Keep up the good work!");
        }
        System.out.println("Your daily budget has been calculated to be "
                + (int) year.dailyBudgetFromMonth(m)
                + " a day, try to keep your daily purchases under this amount!");


        changeMonthlyBudget(m);

    }

    //REQUIRES: Valid month m 1-2, and the user only types according to instructions
    //MODIFIES: Monthly budget stored in Year object
    //EFFECTS: changes the monthly budget
    private void changeMonthlyBudget(int m) {
        System.out.println("Do you want to change your budget?\n1. Set a new budget"
                + "\n2. Add to your budget\n3. Remove from your budget\n4. No");
        Scanner sc = new Scanner(System.in);
        int userInput = sc.nextInt();
        if (userInput != 4) {
            System.out.println("Please enter the amount you would like to set/change.");
            System.out.println("Keep in mind you have a total income of " + year.totalMonthlyDebitValue(m)
                    + " saved up. Try not to exceed your available savings.");
            if (userInput == 1) {
                userInput = sc.nextInt();
                year.setMonthlyBudget(userInput, m);
            } else if (userInput == 2) {
                userInput = sc.nextInt();
                year.addToMonthlyBudget(userInput, m);
            } else if (userInput == 3) {
                userInput = sc.nextInt();
                year.removeFromMonthlyBudget(userInput, m);
            }
        }
    }

    //REQURIES: valid month m 1-12
    //EFFECTS: helper method getting remaining monthly budget
    private void getRemainingMonthlyBudget(int m) {
        year.getRemainingMonthlyBudget(m);
    }



    //REQUIRES: Valid month 1-12, monthVisual 2d primitive array
    //MODIFIES: a day object's credits and debit ArrayLists
    //EFFECTS: adds a transaction to the ArrayList of credits/debits in the day
    private void addTransaction(int m, Day[][] monthVisual) {
        Scanner sc = new Scanner(System.in);
        double temp3;
        String description = "";
        System.out.println("Which day would you like to edit?");
        int temp = sc.nextInt();
        System.out.println("Would you like to add a cheque deposit or a purchase?\n1. Cheque deposit \n2. Purchase");
        int temp2 = sc.nextInt();
        dealWithUserInTransaction(m,temp,temp2);


    }

    //REQUIRES: valid m temp and temp2, however these parameters ar generated in addTransaction
    //MODIFIES: a day's objects
    //EFFECTS: helper function to for addTransaction, takes data from addTransaction and fills in the fields to add
    //         the transaction to credit or debit for the Day object selected
    private void dealWithUserInTransaction(int m,int temp, int temp2) {
        String description = "";
        double temp3;
        Scanner sc = new Scanner(System.in);
        if (temp2 == 1) {
            System.out.println("How much would you like to deposit?");
            temp3 = sc.nextDouble();
        } else {
            System.out.println("How much did this purchase cost?");
            temp3 = sc.nextDouble();
        }
        System.out.println("What would you like to name this entry?");
        String temp4 = sc.nextLine();
        temp4 = sc.nextLine();
        if (temp2 == 1) {
            description = "debit";
        }
        if (temp2 == 2) {
            description = "credit";
        }
        Transaction ts = new Transaction(temp3,description,temp4);
        year.setTransaction(m,temp,ts);
        System.out.println("Entry made on " + printMonth(m) + " " + temp + " named "
                + temp4 + " adding " + temp3 + " to your " + ts.getDescription() + " balance ");
    }

//==================================DOES NOT DEAL WITH REAL ARRAYLIST SIMPLY VISUAL STUFF==============================



    //REQUIRES: An already initialized monthVisual 2d primitive array
    //MODIFIES: monthVisual 2d Array
    //EFFECTS: generates a new monthVisual and replaces the old input one to "switch" the printed image
    private int switchToAnotherMonth(int command, int m, Day[][] monthVisual) {
        Scanner userInput = new Scanner(System.in);
        int temp;
        System.out.println("Which month would you like to view?");
        temp = userInput.nextInt();
        monthVisual = generateMonthPrintVisual(year,temp);
        visualizeMonth(monthVisual,temp);
        return temp;
    }

    //EFFECTS: "Selects" the day by displaying the DayVisual corresponding to the day in UI
    private int selectDay(int m, Year y) {
        Scanner sc = new Scanner(System.in);
        int temp = 0;
        System.out.println("Select Day of Month the view");
        temp = sc.nextInt();
        displayDayVisual(temp,m);
        return m;
    }

    //REQUIRES: a valid month between 1-12, and a valid day of the month between 1-max days in the month
    public void displayDayVisual(int dayOfTheMonth, int m) {
        Day temp = year.getDays().get(m - 1).get(dayOfTheMonth - 1);
        System.out.println(printMonth(m) + " " + dayOfTheMonth);
        System.out.println("Your current paycheques/purchases today:");
        boolean keepgoing = true;
        if (temp.getCredits().isEmpty() && temp.getDebits().isEmpty()) {
            System.out.println("Nothing for now!");
            keepgoing = false;
        }
        if (keepgoing == true) {
            printArray(temp.getCredits());
            printArray(temp.getDebits());
        }
//        if ((printArray(temp.getCredits())) && (printArray(temp.getDebits()))) {
//            System.out.println("Nothing for now!");
//        }
    }

    //REQUIRES: ArrayList is an ArrayList
    //EFFECTS: returns false if ArrayList is not empty, true if empty, and prints
    public void printArray(ArrayList<Transaction> t) { //tested. works as intended

        for (Transaction ts: t) {
//            String s = "";
//            if (ts.getCreditordebit() == 1) {
//                s = " was bought for ";
//            }
//
//            if (ts.getCreditordebit() == 2) {
//                s = " was deposited amounting to ";
//            }
            System.out.println("Id:" + ts.getId() + " [" + ts.getLabel() + " " + ts.getDescription()
                    + "ted your account " + ts.getAmount() + "]");
        }

    }

    //REQUIRES: a valid month between 1-12
    //EFFECTS: creates a visual primitive Array for the purpose of printing in the console ui
    @SuppressWarnings("methodlength")
    //Reason: this is only for phase 1 (console based ui), and is a visual representation of the month
    //This code cannot be compressed
    public Day[][] generateMonthPrintVisual(Year y, int m) {
        Day[][] monthVisualize = new Day[6][7]; //:c
        ArrayList<ArrayList<Day>> master = y.getDays(); //pull Days ArrayList from Year object
        ArrayList<Day> monthDays = master.get(m - 1);//creates an ArrayList for selected month
        int adjustedFirst = getFirstDayOfWeekMonth(m - 1) - 1; //get first day of the month (1, 2, 3 etc for mon

        boolean start = false;
        boolean finished = false;
        int counter = 0; //counter to keep track of progress on ArrayList<Day> monthDays

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if (j >= adjustedFirst) {
                    start = true;
                }

                if (((j <= adjustedFirst) && (!start)) || (finished == true)) {
                    monthVisualize[i][j] = new Day(new ArrayList<Transaction>(), new ArrayList<Transaction>(),0);
                }

                if ((start == true) && (finished == false)) { //if started
                    if (counter < y.getDaysInMonth(m)) {
                        monthVisualize[i][j] = monthDays.get(counter);
                    } //copies from monthDays to monthVisualize

                    if (counter + 1 >= y.getDaysInMonth(m)) {
                        finished = true;
                    } else {
                        counter++; //adds 1 to counter
                    } //deals with counter
                }
            }
        }
        return monthVisualize;
    }


    //REQUIRES: 2d primitive arraylist for the month, and month int that is between 1-12
    //EFFECT: visualizes the generated visual for that month
    public void visualizeMonth(Day[][] month, int m) {
        System.out.println("             " + printMonth(m));
        System.out.println("  Sun  Mon  Tue  Wed  Thu  Fri  Sat");
        for (int i = 0; i < month.length; i++) {
            for (int j = 0; j < month[i].length; j++) {
                int temp = month[i][j].getDay();
                if (temp != 0) {
                    if (temp < 10) {
                        System.out.print("  " + temp + "  ");
                    } else {
                        System.out.print("  " + temp + " ");
                    }
                } else {
                    System.out.print("  " + " " + "  ");
                }
            }
            System.out.println("");
        }
    }


    //REQUIRES: month must be between 1-12
    //EFFECTS: returns month String/common names based on the given month code
    public String printMonth(int m) {
        ArrayList<String> months = new ArrayList<>(Arrays.asList("January","February","March","April","May","June",
                "July","August","September","October","November","December"));
        return months.get(m - 1);
    }

    //REQUIRES: valid initalized Calendar object, valid year, and month m 1-12
    //MODIFIES: the Calendar object's parameters.
    public Calendar modifyCalendarYear(Calendar cal2, int year,int m) {
        //user deciding which year to create calendar for
        cal2.set(Calendar.YEAR, year);
        cal2.set(Calendar.MONTH, m);
        cal2.set(Calendar.DAY_OF_MONTH, 1);
        return cal2;
    }

    //REQUIRES: valid month id between 1-12
    //EFFECTS: returns the 1st day's day of the week
    public int getFirstDayOfWeekMonth(int m) {
        Calendar cal = Calendar.getInstance();
        modifyCalendarYear(cal,year.getYear(),m);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


}
