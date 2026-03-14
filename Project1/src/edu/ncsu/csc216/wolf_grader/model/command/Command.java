package edu.ncsu.csc216.wolf_grader.model.command;

/**
 * Handles the data for a specific user action that changes a submission's status.
 */
public class Command {
    /** The set of actions that can trigger a state change in the FSM. */
    public enum CommandValue { ASSIGN, MOVE, PROVIDE, REUPLOAD, SUBMIT, ANALYZE, RESOLVE, PROCESS, REQUEST, PUBLISH, PASS, FAIL }
    
    /** Stores the extra details related to the respective action. */
    private String commandInformation;
    /** The specific action value that triggers a change in the state machine. */
    private CommandValue commandValue;

    /**
     * Sets up a command with an action and its details.
     * @param command the action being requested.
     * @param commandInformation extra data like a grade or name.
     */
    public Command(CommandValue command, String commandInformation) {
        if (command == null) {
            throw new IllegalArgumentException("Invalid command.");
        }

        if (command == CommandValue.ASSIGN || command == CommandValue.SUBMIT) {
            if (commandInformation == null || commandInformation.isEmpty()) {
                throw new IllegalArgumentException("Invalid information.");
            }
        }
        
        if (command == CommandValue.ANALYZE || command == CommandValue.RESOLVE) {
            if (commandInformation != null && !commandInformation.isEmpty()) {
                throw new IllegalArgumentException("Invalid information.");
            }
        }

        this.commandValue = command;
        this.commandInformation = commandInformation;
    }

    /** @return the command value. */
    public CommandValue getCommand() { return commandValue; }
    /** @return the information string. */
    public String getCommandInformation() { return commandInformation; }
}