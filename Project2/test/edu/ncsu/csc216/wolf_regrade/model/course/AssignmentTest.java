package edu.ncsu.csc216.wolf_regrade.model.course;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AssignmentTest {
    @Test
    public void testAssignmentConstructor() {
        Assignment a = new Assignment("Project 1");
        assertEquals("Project 1", a.getAssignmentName());
    }
}