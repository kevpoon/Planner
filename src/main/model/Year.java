package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

//this class represents a full year
public class Year implements Writable {
    // 3 fields inside year
    // one 2d arraylist, one int year, one primitive double array monthlyBudget
    private ArrayList<ArrayList<Day>> days;
    private int year;

    public double[] getmonthlyBudgetArray() {
        return monthlyBudget;
    }

    private double[] monthlyBudget = new double[12];


    //REQUIRES: 4 digit valid year integer
    //EFFECTS: constructs a Year object generated as a 2D ArrayList<Day> of Day object and assigns/stores fields
    public Year(int year) {
        this.year = year;
        days = new ArrayList<ArrayList<Day>>();
        for (int i = 1; i <= 12; i++) {
            ArrayList<Day> monthDays = new ArrayList<>();
            days.add(monthDays);
            for (int j = 1; j <= getDaysInMonth(i); j++) {
                monthDays.add(new Day(new ArrayList<>(), new ArrayList<>(),j));
            }

        }
    }

    //MODIFIES: this
    //EFFECTS: constructs a year with a set monthlyBudget array
    public Year(int year, ArrayList<ArrayList<Day>> days, double[] monthlyBudget) {
        this.year = year;
        this.days = days;
        this.monthlyBudget = monthlyBudget;
    }

    //EFFECTS: replaces monthlybudget array with input one
    // for use in JSONReader
    public void replaceMonthlyBudgetArray(double[] monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    @Override//referenced from JSON demo
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //EFFECTS: constructs a JSONObject from Year object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("days", daysToJson());
        json.put("monthlyBudget", monthlyBudgetToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private JSONArray monthlyBudgetToJson() {
        JSONArray jsonArray = new JSONArray();

        for (double d : monthlyBudget) {
            jsonArray.put(d);
        }
        return jsonArray;
    }
    //constructs a jsonObject from day
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    private JSONArray daysToJson() {
        JSONArray choncArray = new JSONArray();

        for (ArrayList<Day> dl : days) {
            JSONArray smolArray = new JSONArray();
            for (Day d: dl) {
                smolArray.put(d.toJson());
            }
            choncArray.put(smolArray);
        }
        return choncArray;
    }

    //REQUIRES: months start from 1-12
    //EFFECTS: returns the budget stored in the monthlyBudget primitive Array
    public double getmonthlyBudget(int month) {
        return monthlyBudget[month - 1];
    }

    //REQUIRES: valid month between 1-12, valid amount
    //MOIDIFES: this object's monthlyBudget index
    //EFFECTS: the monthlybudget for this specific month is set to the amount
    public void setMonthlyBudget(double amount, int month) {
        this.monthlyBudget[month - 1] = amount;
        EventLog.getInstance().logEvent(new Event("Monthly budget for month "
                + month + " was set to " + amount));
    }

    //REQUIRES: valid month between 1-12, valid amount
    //MOIDIFES: this object's monthlyBudget index
    //EFFECTS: the monthlybudget for this specific month has amount added to it
    public void addToMonthlyBudget(double amount, int month) {
        this.monthlyBudget[month - 1] += amount;
        EventLog.getInstance().logEvent(new Event("Monthly budget for month "
                + month + " was increased by " + amount));
    }

    //REQUIRES: valid month between 1-12, valid amount
    //MOIDIFES: this object's monthlyBudget index
    //EFFECTS: the monthlybudget for this specific month has amount subtracted from it
    public void removeFromMonthlyBudget(double amount, int month) {
        this.monthlyBudget[month - 1] -= amount;
        EventLog.getInstance().logEvent(new Event("Monthly budget for month "
                + month + " was decreased by " + amount));
    }

    //REQUIRES: valid month between 1-12
    //EFFECTS: returns the set budget for the month - the total expenses
    public double getRemainingMonthlyBudget(int month) {
        return getmonthlyBudget(month) - totalMonthlyCreditValue(month);
    }

    //REQUIRES: a valid month between 1-12
    //EFFECTS: adds the total value of debits of each day in the month, and returns it as a double
    public double totalMonthlyCreditValue(int month) {
        double temp = 0;
        for (int i = 0; i < days.size(); i++) {
            temp += days.get(month - 1).get(i).getTotalCredits();
        }
        return temp;
    }

    //REQUIRES: a valid month between 1-12
    //EFFECTS: adds the total value of debits of each day in the month, and returns it as a double
    public double totalMonthlyDebitValue(int month) {
        double temp = 0;
        for (int i = 0; i < days.size(); i++) {
            temp += days.get(month - 1).get(i).getTotalDebits();
        }
        return temp;
    }

    //REQUIRES: a valid month between 1-12
    //EFFECTS: adds the total number (occurrences) of debits of each day in the month, and returns it as a double
    public double totalMonthlyDebitNumber(int month) {
        double temp = 0;
        for (int i = 0; i < days.size(); i++) {
            temp += days.get(month - 1).get(i).getNumberDebits();
        }
        return temp;
    }

    //REQUIRES: Valid month between 1-12
    //EFFECTS: adds the total number (occurrences) of credits of each day in the month, and returns it as a double
    public double totalMonthlyCreditNumber(int month) {
        double temp = 0;
        for (int i = 0; i < days.size(); i++) {
            temp += days.get(month - 1).get(i).getNumberCredits();
        }
        return temp;
    }


    //EFFECTS: returns the calculated daily budget
    public double dailyBudgetFromMonth(int m) {
        return monthlyBudget[m - 1] / getDaysInMonth(m);
    }

    //REQUIRES: valid month between 1-12, valid day between 1- max days in month, and valid transaction
    //MODIFIES: 2D ArrayList Days - specifically the credit or debit transaction ArrayList inside specified day
    //EFFECTS: adds a transaction to either the credit or debit
    public void setTransaction(int month, int day,Transaction ta) {
        Day tempDay;
        ArrayList<Day> tempMonth;
        tempDay = days.get(month - 1).get(day - 1);
        tempMonth = days.get(month - 1);

        if (ta.getDescription() == "debit") {
            tempDay.addDebit(ta);
        }
        if (ta.getDescription() == "credit") {
            tempDay.addCredit(ta);
        }

        tempMonth.set(day - 1, tempDay);
        days.set(month - 1,tempMonth);
    }

    //REQUIRES: valid month between 1-12, valid day between 1- max days in month, and valid transaction
    //MODIFIES: 2D ArrayList Days - specifically the credit or debit transaction ArrayList inside specified day
    //EFFECTS: removes a transaction to either the credit or debit
    public boolean removeTransaction(int month, int id) {
        ArrayList<Day> tempMonth;
        tempMonth = days.get(month - 1);

        int spot = 0;

        for (int i = 0; i < tempMonth.size(); i++) {
            if (tempMonth.get(i).getIdIndexCredit(id) != -1) {
                spot = tempMonth.get(i).getIdIndexCredit(id);
                days.get(month - 1).get(i).removeCredit(spot);
                return true;
            }

            if (tempMonth.get(i).getIdIndexDebit(id) != -1) {
                spot = tempMonth.get(i).getIdIndexDebit(id);
                days.get(month - 1).get(i).removeDebit(spot);
                return true;
            }

        }
        return false;
    }


    //REQUIRES: valid int month that is between 1-12
    //EFFECTS: returns the correct number of days in the month
    public int getDaysInMonth(int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return leapYearDays(year);
        }
    }

    //REQUIRES: 4 digit year eg. 2022, used only when month = 2 (Feb)
    //EFFECTS: returns number of days in Feb of this year. 29 if divisible by 4, or ends with 00 and divisible by 400
    //         28 days otherwise
    public int leapYearDays(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            }
            return 29;
        } else {
            return 28;
        }
    }

    //EFFECTS: simple getter method getting the 2D Day ArrayList
    public ArrayList<ArrayList<Day>> getDays() {
        return days;
    }

    //EFFECTS: gets the year number stored inside the Year object
    public int getYear() {
        return year;
    }
}
