package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ShowTimeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Used for parsing user input for list command
 */
public class ShowTimeCommandParser {

    /**
     * Parse function
     */
    public Command parse(String userInput) throws ParseException {
        String[] arguments = userInput.trim().split("\\s+");
        if (arguments.length == 1) {
            if (arguments[0].equals("") || arguments[0].equals("st") || isValidDateFormat(arguments[0])) {
                return new ShowTimeCommand(arguments);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowTimeCommand.MESSAGE_USAGE));
            }
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowTimeCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Check if user input is of the correct format
     */
    public boolean isValidDateFormat(String str) {
        String[] strSplit = str.split("-");
        if (strSplit.length == 2 || strSplit.length == 3) {
            return true;
        } else {
            return false;
        }
    }

}
