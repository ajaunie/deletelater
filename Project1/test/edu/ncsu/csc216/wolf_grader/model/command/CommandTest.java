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
     * Checks valid command creation for actions that require supplemental text info.
     */
    @Test
    public void testCommandWithInfo() {
        Command c1 = new Command(CommandValue.ASSIGN, "grader123");
        assertEquals(CommandValue.ASSIGN, c1.getCommand());
        assertEquals("grader123", c1.getCommandInformation());

        Command c2 = new Command(CommandValue.SUBMIT, "A");
        assertEquals(CommandValue.SUBMIT, c2.getCommand());
        assertEquals("A", c2.getCommandInformation());
    }

    /**
     * Checks valid command creation for actions that do not require extra info.
     */
    @Test
    public void testCommandWithoutInfo() {
        Command moveCmd = new Command(CommandValue.MOVE, null);
        assertEquals(CommandValue.MOVE, moveCmd.getCommand());
        assertNull(moveCmd.getCommandInformation());
    }

    /**
     * Checks various invalid constructor arguments to ensure exceptions are thrown correctly.
     */
    @Test
    public void testInvalidCommandConstruction() {
        // Missing the action type
        assertThrows(IllegalArgumentException.class, () -> new Command(null, "info"));

        // Missing required info for ASSIGN
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ASSIGN, ""));
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.ASSIGN, null));

        // Missing required info for SUBMIT
        assertThrows(IllegalArgumentException.class, () -> new Command(CommandValue.SUBMIT, null));
    }
}