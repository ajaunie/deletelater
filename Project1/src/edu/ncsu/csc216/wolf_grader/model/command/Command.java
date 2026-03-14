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
            throw new IllegalArgumentException("Invalid information.");
        }

        // Combined logic for basic information check
        if ((command == CommandValue.ASSIGN || command == CommandValue.RESOLVE
                || command == CommandValue.SUBMIT || command == CommandValue.ANALYZE)
                && (commandInformation == null || commandInformation.isEmpty())) {
            throw new IllegalArgumentException("Invalid information.");
        }

        // Combined logic for SUBMIT grade validation
        if (command == CommandValue.SUBMIT
                && (!"A".equals(commandInformation) && !"B".equals(commandInformation)
                && !"C".equals(commandInformation) && !"F".equals(commandInformation)
                && !"IN".equals(commandInformation) && !"F-AIV".equals(commandInformation))) {
            throw new IllegalArgumentException("Invalid information.");
        }

        // Combined logic for RESOLVE grade validation
        if (command == CommandValue.RESOLVE
                && (!"A".equals(commandInformation) && !"B".equals(commandInformation)
                && !"C".equals(commandInformation) && !"F".equals(commandInformation)
                && !"F-AIV".equals(commandInformation))) {
            throw new IllegalArgumentException("Invalid information.");
        }

        // Combined logic for ANALYZE grade validation
        if (command == CommandValue.ANALYZE
                && (!"A".equals(commandInformation) && !"B".equals(commandInformation)
                && !"C".equals(commandInformation) && !"F".equals(commandInformation))) {
            throw new IllegalArgumentException("Invalid information.");
        }

        this.commandValue = command;
        this.commandInformation = commandInformation;
    }

    /**
     * Returns the command value that triggers a state change.
     * @return the command value. 
     */
    public CommandValue getCommand() { return commandValue; }

    /**
     * Returns the additional information associated with the command.
     * @return the information string. 
     */
    public String getCommandInformation() { return commandInformation; }
}