package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {
    Transaction testTransaction;


    @BeforeEach
    void runBefore() {
        testTransaction = new Transaction(500,"credit", "hu tao");
    }

    @Test
    void testConstructor() {
//        assertEquals(1, testTransaction.getId());
        assertEquals(500, testTransaction.getAmount());
        assertEquals("credit",testTransaction.getDescription());
        assertEquals("hu tao",testTransaction.getLabel());
    }
}
