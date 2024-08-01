package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
//All tests are referenced off of JsonSerializationDemo for CPSC210
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.
    Transaction debit = new Transaction(100,"debit","average biweekly paycheque");
    Transaction credit = new Transaction(100, "credit", "4 good tonkotsu ramen");

    @Test
    void testWriterInvalidFile() {
        try {
            Year yr = new Year(2022);
            JsonWriter writer = new JsonWriter("./data/!%@successfullife@#$#://.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyYear() {
        try {
            Year yr = new Year(2022);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyYear.json");
            writer.open();
            writer.write(yr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyYear.json");
            yr = reader.read();
            assertEquals(2022,yr.getYear());
            assertEquals(0, yr.getmonthlyBudget(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralyear() {
        try {
            Year yr = new Year(2022);
            yr.setTransaction(1,1,debit);
            yr.setTransaction(1,1,credit);
            yr.setMonthlyBudget(100,1);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralYear.json");
            writer.open();
            writer.write(yr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralYear.json");
            yr = reader.read();
            assertEquals(2022, yr.getYear());
            double[] temp = yr.getmonthlyBudgetArray();
            Day janfirsttest = yr.getDays().get(0).get(0);
            assertEquals(100, temp[0]);
            checkDay(janfirsttest,1,credit,debit);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}