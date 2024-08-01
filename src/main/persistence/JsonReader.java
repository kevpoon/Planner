package persistence;


import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

//referenced from JSON demo
//All referenced off of JsonSerializationDemo for CPSC210
// Represents a reader that reads year from JSON data stored in file
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Year from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Year read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return formYear(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

//////////////////

    //MODIFIES: Transaction object
    //EFFECTS: creates a Transaction object based on the JSONObject stored in the JSON
    private Transaction formTransaction(JSONObject jsonObject) {
        int nextId = jsonObject.getInt("nextId");
        int id = jsonObject.getInt("id");
        double amount = jsonObject.getDouble("amount");
        String description = jsonObject.getString("description").toString();
        String label = jsonObject.getString("label");
        Transaction transaction = new Transaction(amount,description,label,id,nextId);
        return transaction;
    }

    //MODIFIES: ArrayList<Transaction>
    //EFFECTS: creates a ArrayList<Transaction> based on the JSONArray stored in the JSONArray
    private ArrayList<Transaction> formTransactionList(JSONArray jsonArray) {
        ArrayList<Transaction> temps = new ArrayList<Transaction>();
        for (int i = 0; i < jsonArray.length(); i++) {
            temps.add(formTransaction((JSONObject)jsonArray.get(i)));
        }
        return temps;
    }

    //MODIFIES: Day object
    //EFFECTS: creates a Day object based on the JSONObject stored in the JSON
    private Day formDay(JSONObject jsonObject) {
        int day = jsonObject.getInt("day");
        ArrayList<Transaction> credits = formTransactionList(jsonObject.getJSONArray("credits"));
        ArrayList<Transaction> debits = formTransactionList(jsonObject.getJSONArray("debits"));
        Day temp = new Day(credits,debits,day);

        return temp;
    }

    // MODIFIES: Year object
    //EFFECTS: creates a year object based on json
    private Year formYear(JSONObject jsonObject) {

        int year = jsonObject.getInt("year");
        JSONArray dumb = jsonObject.getJSONArray("days");
        double[] monthlyBudget = formmonthlyBudget(jsonObject);
        ArrayList<ArrayList<Day>> days = new ArrayList<ArrayList<Day>>();

        for (int i = 0; i < dumb.length(); i++) {
            JSONArray dumber = dumb.getJSONArray(i);
            ArrayList<Day> temp = new ArrayList<Day>();
            for (int j = 0; j < dumber.length(); j++) {
                temp.add(formDay((JSONObject)dumber.get(j)));
            }
            days.add(temp);
        }
        Year yearfinal = new Year(year,days,monthlyBudget);
        return yearfinal;
    }

    //MODIFIES: double[]
    //EFFECTS: creates a double[] from jsonObject
    private double[] formmonthlyBudget(JSONObject jsonObject) {

        JSONArray monthlyBudgetJson = jsonObject.getJSONArray("monthlyBudget");
        double[] monthlyBudget = new double[12];

        for (int i = 0; i < 12; i++) {
            monthlyBudget[i] = monthlyBudgetJson.getDouble(i);
        }
        return monthlyBudget;
    }


}
