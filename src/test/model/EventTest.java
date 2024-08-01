package model;

import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

//Tests for the Event class referenced directly from AlarmSystems
//https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Event e1;
    private Event e2;
    private Date d;
    private Year y;
    Date dateLogged;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        e = new Event("Fun");   // (1)
        e1 = new Event("notFun");
        e2 = new Event("Fun");
        d = Calendar.getInstance().getTime();   // (2)
        y = new Year(2022);
        dateLogged = Calendar.getInstance().getTime();

    }

    @Test
    public void testEvent() {
        assertEquals("Fun", e.getDescription());
        assertEquals(d, e.getDate());
    }

    @Test
    public void testEquals() {
        assertFalse(e.equals(null));
        assertFalse(e.equals(y));
        assertEquals(e.hashCode(),13* e.getDate().hashCode() + e.getDescription().hashCode());
        assertFalse(e.equals(e1));
        assertTrue(e.equals(e2));
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Fun", e.toString());
    }
}
