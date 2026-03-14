package edu.ncsu.csc216.wolf_grader.model.submission;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.command.Command.CommandValue;

/**
 * Provides comprehensive tests for the Submission class, focusing on the correctness
 * of the state machine logic and error handling for all transitions.
 * @author Ajaunie White
 */
public class SubmissionTest {

    @BeforeEach
    public void setUp() {
        Submission.setCounter(0);
    }

    @Test
    public void testFullStateCycle() {
        Submission s = new Submission("Alice Smith", "asmith");
        
        s.update(new Command(CommandValue.MOVE, null));
        assertEquals("Submitted", s.getState());
        
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals("Grade", s.getState());
        
        // Correct path: ANALYZE goes to Check
        s.update(new Command(CommandValue.ANALYZE, null));
        assertEquals("Check", s.getState()); 
        
        s.update(new Command(CommandValue.PASS, null));
        assertEquals("Return", s.getState());

        s.update(new Command(CommandValue.REQUEST, null));
        assertEquals("Regrade", s.getState());
        
        s.update(new Command(CommandValue.PROCESS, null));
        assertEquals("Grade", s.getState());
        
        // Correct path: SUBMIT goes to Return
        s.update(new Command(CommandValue.SUBMIT, "A"));
        assertEquals("Return", s.getState());
    }

    @Test
    public void testToString() {
        Submission s = new Submission("Eva", "e1");
        // End format should have two commas for empty grader/grade
        String expected = "* 0,Upload,Eva,e1,false,Not Done,false,,";
        assertEquals(expected, s.toString());
    }
}