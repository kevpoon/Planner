package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

//this class represents a full day
public class Day implements Writable {
    // 3 fields contained within Day
    // two ArrayLists of Transactions, and one int day
    private ArrayList<Transaction> credits;
    private ArrayList<Transaction> debits;
    private int day;

    //REQUIRES: two different ArrayLists of the Transaction object, and a valid int between 1 and max day of month
    //MODIFIES: this
    //EFFECTS: constructs a Day with an ArrayList of credits, debits, and an int day representing the day
    //         of the month
    public Day(ArrayList<Transaction> credits, ArrayList<Transaction> debits,int day) {
        this.credits = credits;
        this.debits = debits;
        this.day = day;
    }

    @Override//referenced from JSON demo
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //EFFECTS: creates a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("day", day);
        json.put("credits", creditsToJson());
        json.put("debits", debitsToJson());
        return json;
    }

    //EFFECTS: creates a JSONArray with each transaction of credits inside
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONArray creditsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : credits) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }

    //EFFECTS: creates a JSONArray with each transaction of debits inside
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONArray debitsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : debits) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }


    //REQUIRES: valid index or program will break
    //EFFECTS: removes debit transaction from debits
    public void removeDebit(int index) {
        if (!debits.isEmpty()) {
            debits.remove(index);
        }
        EventLog.getInstance().logEvent(new Event("Transaction at index " + index + " was removed"));
    }

    //REQUIRES valid index or program will break
    //EFFECTS: removes credit transaction from credits
    public void removeCredit(int index) {
        if (!credits.isEmpty()) {
            credits.remove(index);
        }
        EventLog.getInstance().logEvent(new Event("Transaction at index " + index + " was removed"));
    }

    //REQUIRES: valid id. If the id is not valid and points to an ArrayList index that is null, the program will break
    //EFFECTS: returns the index of the transaction id
    public int getIdIndexCredit(int id) {
        for (int i = 0; i < credits.size(); i++) {
            if (credits.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    //REQUIRES: valid id. If the id is not valid and points to an ArrayList index that is null, the program will break
    //EFFECTS: returns the index of the transaction id
    public int getIdIndexDebit(int id) {
        for (int i = 0; i < debits.size(); i++) {
            if (debits.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }


    //EFFECTS: returns the total amount of credits in the credits ArrayList for this object
    public double getTotalCredits() {
        double temp = 0;
        for (Transaction t: credits) {
            temp += t.getAmount();
        }
        return temp;
    }

    //EFFECTS: returns the total amount of debits in the debits ArrayList for this object
    public double getTotalDebits() {
        double temp = 0;
        for (Transaction t: debits) {
            temp += t.getAmount();
        }
        return temp;
    }

    //REQUIRES: Valid transaction, transaction.description must be "credit"
    //MODIFIES: the credit Arraylist
    //EFFECTS: adds a new entry to the end of the ArrayList
    public void addCredit(Transaction transaction) {
        this.credits.add(transaction);
        EventLog.getInstance().logEvent(new Event("Id:" + transaction.getId() + " " + transaction.getLabel()
                + " was processed, adding " + transaction.getAmount() + " to your credit balance."));
    }

    //REQUIRES: Valid transaction, transaction.description must be "debit"
    //MODIFIES: the credit Arraylist
    //EFFECTS: adds a new entry to the end of the ArrayList
    public void addDebit(Transaction transaction) {
        this.debits.add(transaction);
        EventLog.getInstance().logEvent(new Event("Id:" + transaction.getId() + " " + transaction.getLabel()
                + " was processed, adding " + transaction.getAmount() + " to your debit balance."));

    }

    //EFFECTS: returns the size of the credit ArrayList
    public int getNumberCredits() {
        return this.credits.size();
    }

    //EFFECTS: returns the size of the debit ArrayList
    public int getNumberDebits() {
        return this.debits.size();
    }





    //Getters Setters

    //EFFECTS: returns the day number for this Day object
    public int getDay() {
        return day;
    }

    //EFFECTS: returns the credit ArrayList for this Day object
    public ArrayList<Transaction> getCredits() {
        return this.credits;
    }

    //EFFECTS: returns the debit ArrayList for this Day object
    public ArrayList<Transaction> getDebits() {
        return this.debits;
    }





}
