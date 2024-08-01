package persistence;

import org.junit.jupiter.api.Test;
import model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//All tests are referenced off of JsonSerializationDemo for CPSC210
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/possiblityofasuccessfullife.json");
        try {
            Year yr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyYear() {
        JsonReader reader = new JsonReader("./data/testEmptyYear.json");
        double[] temp = new double[12];
        try {
            Year yr = reader.read();
            assertEquals(2016, yr.getYear());
            assertEquals(0, yr.getmonthlyBudget(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralYear() {
        JsonReader reader = new JsonReader("./data/testGeneralYear.json");
        try {
            Year yr = reader.read();
            assertEquals(2022, yr.getYear());
            assertEquals(100, yr.getmonthlyBudget(1));
            Day janfirst = yr.getDays().get(0).get(0);
            Transaction debit = new Transaction(100,"debit","average biweekly paycheque");
            Transaction credit = new Transaction(100, "credit", "4 good tonkotsu ramen");
            checkDay(janfirst,1,credit,debit);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}