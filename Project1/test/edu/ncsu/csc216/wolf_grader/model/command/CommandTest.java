package edu.ncsu.csc216.wolf_grader.model.command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.wolf_grader.model.command.Command.CommandValue;

/**
 * Provides comprehensive tests for the Command class, covering valid and invalid
 * constructor arguments for state transitions.
 * @author Ajaunie White
 */
public class CommandTest {

    /**
     * Checks valid command creation for ASSIGN which requires non-null, non-empty info.
     */
    @Test
    public void testValidAssign() {
        Command c = new Command(CommandValue.ASSIGN, "grader123");
        assertEquals(CommandValue.ASSIGN, c.getCommand());
        assertEquals("grader123", c.getCommandInformation());
    }

    /**
     * Checks valid command creation for SUBMIT with each allowed grade value.
     */
    @Test
    public void testValidSubmitGrades() {
        Command cA = new Command(CommandValue.SUBMIT, "A");
        assertEquals(CommandValue.SUBMIT, cA.getCommand());
        assertEquals("A", cA.getCommandInformation());

        Command cB = new Command(CommandValue.SUBMIT, "B");
        assertEquals("B", cB.getCommandInformation());

        Command cC = new Command(CommandValue.SUBMIT, "C");
        assertEquals("C", cC.getCommandInformation());

        Command cF = new Command(CommandValue.SUBMIT, "F");
        assertEquals("F", cF.getCommandInformation());

        Command cIN = new Command(CommandValue.SUBMIT, "IN");
        assertEquals("IN", cIN.getCommandInformation());

        Command cAIV = new Command(CommandValue.SUBMIT, "F-AIV");
        assertEquals("F-AIV", cAIV.getCommandInformation());
    }

    /**
     * Checks valid command creation for ANALYZE with each allowed letter grade.
     */
    @Test
    public void testValidAnalyzeGrades() {
        Command cA = new Command(CommandValue.ANALYZE, "A");
        assertEquals(CommandValue.ANALYZE, cA.getCommand());

        Command cB = new Command(CommandValue.ANALYZE, "B");
        assertEquals("B", cB.getCommandInformation());

        Command cC = new Command(CommandValue.ANALYZE, "C");
        assertEquals("C", cC.getCommandInformation());

        Command cF = new Command(CommandValue.ANALYZE, "F");
        assertEquals("F", cF.getCommandInformation());
    }

    /**
     * Checks valid command creation for RESOLVE with each allowed grade (no IN allowed).
     */
    @Test
    public void testValidResolveGrades() {
        Command cA = new Command(CommandValue.RESOLVE, "A");
        assertEquals(CommandValue.RESOLVE, cA.getCommand());

        Command cB = new Command(CommandValue.RESOLVE, "B");
        assertEquals("B", cB.getCommandInformation());

        Command cC = new Command(CommandValue.RESOLVE, "C");
        assertEquals("C", cC.getCommandInformation());

        Command cF = new Command(CommandValue.RESOLVE, "F");
        assertEquals("F", cF.getCommandInformation());

        Command cAIV = new Command(CommandValue.RESOLVE, "F-AIV");
        assertEquals("F-AIV", cAIV.getCommandInformation());
    }

    /**
     * Checks valid commands that require no commandInformation.
     */
    @Test
    public void testCommandsWithNoInfo() {
        Command move = new Command(CommandValue.MOVE, null);
        assertEquals(CommandValue.MOVE, move.getCommand());
        assertNull(move.getCommandInformation());

        Command provide = new Command(CommandValue.PROVIDE, null);
        assertEquals(CommandValue.PROVIDE, provide.getCommand());

        Command reupload = new Command(CommandValue.REUPLOAD, null);
        assertEquals(CommandValue.REUPLOAD, reupload.getCommand());

        Command process = new Command(CommandValue.PROCESS, null);
        assertEquals(CommandValue.PROCESS, process.getCommand());

        Command request = new Command(CommandValue.REQUEST, null);
        assertEquals(CommandValue.REQUEST, request.getCommand());

        Command publish = new Command(CommandValue.PUBLISH, null);
        assertEquals(CommandValue.PUBLISH, publish.getCommand());

        Command pass = new Command(CommandValue.PASS, null);
        assertEquals(CommandValue.PASS, pass.getCommand());

        Command fail = new Command(CommandValue.FAIL, null);
        assertEquals(CommandValue.FAIL, fail.getCommand());
    }

    /**
     * Checks that a null CommandValue throws IllegalArgumentException.
     */
    @Test
    public void testNullCommandValue() {
        assertThrows(IllegalArgumentException.class, () -> new Command(null, "info"));
    }

    /**
     * Checks that ASSIGN with null or empty info throws IllegalArgumentException.
     */
    @Test
    public void testAssignNullInfo() {
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ASSIGN, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ASSIGN, ""));
    }

    /**
     * Checks that SUBMIT with null, empty, or invalid grade throws IllegalArgumentException.
     */
    @Test
    public void testSubmitInvalidInfo() {
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.SUBMIT, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.SUBMIT, ""));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.SUBMIT, "X"));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.SUBMIT, "invalid"));
    }

    /**
     * Checks that RESOLVE with null, empty, IN, or invalid grade throws IllegalArgumentException.
     */
    @Test
    public void testResolveInvalidInfo() {
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.RESOLVE, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.RESOLVE, ""));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.RESOLVE, "IN"));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.RESOLVE, "Z"));
    }

    /**
     * Checks that ANALYZE with null, empty, IN, F-AIV, or invalid grade throws IllegalArgumentException.
     */
    @Test
    public void testAnalyzeInvalidInfo() {
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ANALYZE, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ANALYZE, ""));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ANALYZE, "IN"));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ANALYZE, "F-AIV"));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ANALYZE, "bad"));
    }
}