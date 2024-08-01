package persistence;

import model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
//All tests are referenced off of JsonSerializationDemo for CPSC210
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkDay(Day days, int day, Transaction credit, Transaction debit) {
        assertEquals(day, days.getDay());
        assertEquals(credit.getLabel(), days.getCredits().get(0).getLabel());
        assertEquals(debit.getLabel(), days.getDebits().get(0).getLabel());
        assertEquals(credit.getAmount(), days.getCredits().get(0).getAmount());
        assertEquals(debit.getAmount(), days.getDebits().get(0).getAmount());
        assertEquals(credit.getDescription(), days.getCredits().get(0).getDescription());
        assertEquals(debit.getDescription(), days.getDebits().get(0).getDescription());
    }
}
