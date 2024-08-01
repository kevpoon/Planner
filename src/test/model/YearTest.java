package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class YearTest {
    Year testYear1;
    Year testYear2;
    Year testYear3;
    Year testYear4;
    Transaction testTransaction;
    Transaction testTransaction2;
    ArrayList<ArrayList<Day>> days;
    ArrayList<Day> January;
    double[] monthlyBudget = new double[12];
    Year testYear5 = new Year(2016,days,monthlyBudget);
    Day emptyDay;
    ArrayList<Transaction> empty;


    @BeforeEach
    void runBefore() {
        testYear1 = new Year(2022);
        testYear2 = new Year(2000);
        testYear3 = new Year(2300);
        testYear4 = new Year(40);
        testYear1.setMonthlyBudget(1000,1);
        testTransaction = new Transaction(50,"credit","amos bow");
        testTransaction2 = new Transaction(50,"debit","amos bow");
        ArrayList<ArrayList<Day>> days = testYear1.getDays();
        ArrayList<Day> January = days.get(0);
        empty = new ArrayList<>();
        emptyDay = new Day(empty,empty,1);
    }



    @Test
    void testBigConstructor() {
        assertEquals(2016, testYear5.getYear());
        assertEquals(days, testYear5.getDays());
        assertEquals(monthlyBudget, testYear5.getmonthlyBudgetArray());
    }

    @Test
    void testConstructor() {
        assertEquals(testYear1.getDaysInMonth(1),31); // jan
        assertEquals(testYear1.getDaysInMonth(2),28); // feb
        assertEquals(testYear2.getDaysInMonth(2),29);//leap
    }

    @Test
    public void testdailyBudgetFromMonth() {
        testYear1.setMonthlyBudget(31,1);
        assertEquals(1,testYear1.dailyBudgetFromMonth(1));
    }

    @Test
    public void testgetYear() {
        assertEquals(2022,testYear1.getYear());
    }

    @Test
    public void testsetremoveTransaction() {
        Transaction transaction1 = new Transaction(100,"credit","hu tao",1,0);
        Transaction transaction2 = new Transaction(100,"debit","ganyu",2,0);
        Transaction transaction3 = new Transaction(100,"credit","keqing",3,0);
        Transaction transaction4 = new Transaction(100,"debit","zhongli",4,0);
        assertEquals(0, testYear1.getDays().get(0).get(0).getDebits().size());
        assertEquals(0, testYear1.getDays().get(0).get(0).getCredits().size());
        assertFalse(testYear1.removeTransaction(1,1));
        assertFalse(testYear1.removeTransaction(1,1));
        testYear1.setTransaction(1,1,transaction1);
        testYear1.setTransaction(1,1,transaction2);
        assertEquals(1, testYear1.getDays().get(0).get(0).getDebits().size());
        assertEquals(1, testYear1.getDays().get(0).get(0).getCredits().size());
        assertFalse(testYear1.removeTransaction(1,500));
        assertTrue(testYear1.removeTransaction(1,1));
        assertEquals(1, testYear1.getDays().get(0).get(0).getDebits().size());
        assertEquals(0, testYear1.getDays().get(0).get(0).getCredits().size());
        assertFalse(testYear1.removeTransaction(1,500));
        assertTrue(testYear1.removeTransaction(1,2));
        assertEquals(0, testYear1.getDays().get(0).get(0).getDebits().size());
        assertEquals(0, testYear1.getDays().get(0).get(0).getCredits().size());
    }

    @Test
    public void testtotalMonthlyCreditNumber() {
        testYear2.setTransaction(1,1,testTransaction);
        assertEquals(1,testYear2.totalMonthlyCreditNumber(1));
    }

    @Test
    public void testtotalMonthlyDebitNumber() {
        testYear2.setTransaction(1,1,testTransaction2);
        assertEquals(1,testYear2.totalMonthlyDebitNumber(1));
    }

    @Test
    public void testtotalMonthlyDebitValue() {
        testYear2.setTransaction(1,1,testTransaction2);
        assertEquals(50,testYear2.totalMonthlyDebitValue(1));
    }

    @Test
    public void testtotalMonthlyCreditValue() {
        testYear2.setTransaction(1,1,testTransaction);
        assertEquals(50,testYear2.totalMonthlyCreditValue(1));
    }

    @Test

    public void testmonthlyBudgets() {
        monthlyBudget[0] = 1000;
        assertEquals(0,testYear4.getmonthlyBudget(1));
        testYear4.replaceMonthlyBudgetArray(monthlyBudget);
        assertEquals(1000,testYear4.getmonthlyBudget(1));
        testYear4.addToMonthlyBudget(100,1);
        assertEquals(1100,testYear4.getmonthlyBudget(1));
        testYear4.removeFromMonthlyBudget(200,1);
        assertEquals(900,testYear4.getmonthlyBudget(1));
        assertEquals(900,testYear4.getRemainingMonthlyBudget(1));
        assertEquals(0,testYear4.getRemainingMonthlyBudget(2));
    }

//    @Test
//    public void testremoveTransaction(){
//        ArrayList<ArrayList<Day>> test = new ArrayList<ArrayList<Day>>();
//        ArrayList<Day> month = new ArrayList<Day>();
//        Day newDay = new Day(empty,empty,1);
//        newDay.addCredit(testTransaction);
//        month.set(0,newDay);
//        test.set(0,month);
//        testYear2.setDays(test);
//        testYear2.removeTransaction(0,1);//first month, with id 0
//        assertEquals(0,testYear2.getDays().get(0).get(0).getNumberCredits());
//
//
//    }




}
