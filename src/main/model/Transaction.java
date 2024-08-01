package model;

//The way nextID is dealt with was inspired by TellerApp.

import org.json.JSONObject;
import persistence.Writable;

//this class represents a transaction to be recorded in day
public class Transaction implements Writable {
    private static int nextId = 1;
    private int id;


    private double amount; //amount in dollars
    private String description; //either credit or debit
    private String label; //user inspired name
    private int creditordebit;


    @Override //referenced from JSON demo
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: creates a JSONObject from Transaction object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nextId", nextId);
        json.put("id", id);
        json.put("amount", amount);
        json.put("description", description);
        json.put("label", label);
        json.put("creditordebit", creditordebit);
        return json;
    }

    //REQUIRES: a valid double amount, description is a String that is either: "credit" or "debit",
    //          and label is a valid string
    //EFFECTS: creates a new Transaction with unique transaction id
    public Transaction(double amount, String description, String label) {
        this.amount = amount;
        this.description = description;
        this.label = label;
        this.id = nextId++;
    }

    //EFFECTS: constructs transaction and overrides the nextId mechanism for JSONREADER
    public Transaction(double amount, String description, String label, int id, int nextId) {
        this.amount = amount;
        this.description = description;
        this.label = label;
        this.id = id;
        this.nextId = nextId;
    }

    //EFFECTS: simple getter returning id of Transaction
    public int getId() {
        return id;
    }

    //EFFECTS: simple getter returning amount of Transaction
    public double getAmount() {
        return amount;
    }

    //EFFECTS: simple getter returning description of Transaction
    public String getDescription() {
        return description;
    }

    //EFFECTS: simple getter returns label of Transaction
    public String getLabel() {
        return label;
    }




}