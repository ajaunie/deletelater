package edu.ncsu.csc216.wolf_regrade.model.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CompletedRegradesStackTest {
    @Test
    public void testStack() {
        CompletedRegradesStack<String> stack = new CompletedRegradesStack<>();
        assertTrue(stack.isEmpty());
    }
}