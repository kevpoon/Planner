package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayTest {
    private Day testDay;
    ArrayList<Transaction> credit = new ArrayList<>();
    ArrayList<Transaction> debit = new ArrayList<>();

    Day emptyConstant = new Day(new ArrayList<Transaction>(), new ArrayList<Transaction>(),1);
    Day empty1 = new Day(new ArrayList<Transaction>(), new ArrayList<Transaction>(),1);
    ArrayList<Transaction> creditList = new ArrayList<>();
    ArrayList<Transaction> debitList = new ArrayList<>();
    Day prefill1 = new Day(creditList,debitList,1);
    Transaction credit1;
    Transaction debit1;

    @BeforeEach
    public void runBefore() {

    }


    @Test
    public void testgetIdIndexCredit() {
        //since Junit was fixed, BeforeEach now works correctly, and this test is now usable
        credit1 = new Transaction(50, "credit","Ganyu");
        System.out.println(credit1.getId());
        creditList.add(credit1);
        Day fullday = new Day(creditList,debitList,1);
        assertEquals(0,fullday.getIdIndexCredit(4)); // id1 was added to creditList
        assertEquals(-1,fullday.getIdIndexCredit(2));// id2 does not exist in credits, should return -1
    }
    @Test
    public void testgetIdIndexDebit() {//same for this test.
        debit1 = new Transaction(50, "debit", "Sayu");
        debitList.add(debit1);
        Day fullday = new Day(creditList,debitList,1);
        assertEquals(-1,fullday.getIdIndexDebit(1)); // id1 was not added to debitList, so doesnt exist here
        assertEquals(0,fullday.getIdIndexDebit(2));// id2 does was added to debitList, so should return 0
    }

    @Test
    public void testConstructor() {
        creditList.add(credit1);
//        empty1.addCredit(credit1);
//        assertEquals(prefill1.getCredits(),empty1.getCredits());
        debitList.add(debit1);
//        empty1.addDebit(debit1);
//        assertEquals(prefill1.getDebits(),empty1.getDebits());
    }


    @Test
    public void testRemoveCreditDebit() {
        prefill1.removeDebit(0);
        assertEquals(0,prefill1.getDebits().size());
        prefill1.removeCredit(0);
        assertEquals(0,prefill1.getCredits().size());
        creditList.add(credit1);
        debitList.add(debit1);
        Day fullday = new Day(creditList,creditList,1);
        prefill1.removeDebit(0);
        assertEquals(prefill1.getDebits(), emptyConstant.getDebits());
        prefill1.removeCredit(0);
        assertEquals(prefill1.getCredits(), emptyConstant.getCredits());
    }




    @Test
    public void testgetDay(){
        Day fullday = new Day(creditList,debitList, 1);
        assertEquals(fullday.getDay(), 1);
    }

    @Test
    public void testgetNumberCredits() {
        creditList.add(credit1);
        Day fullday = new Day(creditList,debitList,1);
        assertEquals(1, fullday.getNumberCredits());
    }

    @Test
    public void testgetNumberDebits() {
        debitList.add(debit1);
        Day fullday = new Day(creditList,debitList,1);
        assertEquals(1, fullday.getNumberDebits());
        debitList.remove(0);
        assertEquals(0,fullday.getNumberDebits());
    }

    @Test
    public void testgetTotalCredits() {
        credit1 = new Transaction(50, "credit","Ganyu");
        creditList.add(credit1);
        Day fullday = new Day(creditList,debitList,1);
        assertEquals(50, fullday.getTotalCredits());
        creditList.remove(0);
        assertEquals(0,fullday.getNumberCredits());
    }

    @Test
    public void testgetTotalDebits() {
        debit1 = new Transaction(50, "debit", "Sayu");
        debitList.add(debit1);
        Day fullday = new Day(debitList,debitList,1);
        assertEquals(50, fullday.getTotalDebits());
    }
}
