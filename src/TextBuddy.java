/**
 * This class is used to allow user to manipulate text in a file using
 * Command Line Interface. If given file parameter does not exist, program 
 * will create that file as an empty file. User is only allowed to delete
 * one line of the text file using the delete command, and deletion can
 * only be done by providing its corresponding line number, failing which,
 * the program will return an error message. When giving clear or display
 * commands, there must be no more parameters together with that command,
 * or the program will return an error message. It is also assumed that the
 * user will not meddle/edit/delete file when this program is running.
 * The command format is given by the example interaction below:
 * 
 * Welcome to TextBuddy. mytextfile.txt is ready for use
 * command: add little brown fox
 * added to mytextfile.txt: “little brown fox”
 * command: display
 * 1. little brown fox
 * command: add jumped over the moon
 * added to mytextfile.txt: “jumped over the moon”
 * command: display
 * 1. little brown fox
 * 2. jumped over the moon
 * command: delete 2
 * deleted from mytextfile.txt: “jumped over the moon”
 * command: display
 * 1. little brown fox
 * command: clear
 * all content deleted from mytextfile.txt
 * command: display
 * mytextfile.txt is empty
 * command: exit
 * 
 * @author Lam Zhen Zong, Nicholas
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class TextBuddy {
	// These are the list of messages to display to user.
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADD_LINE = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETE_LINE = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_SORT = "%1$s is sorted";
	private static final String MESSAGE_SEARCH_FAIL = "\"%1$s\" is not found";
	private static final String MESSAGE_DISPLAY_EMPTY = "%1$s is empty";
	private static final String MESSAGE_QUERY = "command: ";
	private static final String MESSAGE_INVALID_FORMAT = "Invalid command encountered for: %1$s";
	private static final String MESSAGE_ERROR_CREATE_FILE = "An error is encountered when creating file.";
	private static final String MESSAGE_ERROR_READ_FILE = "An error is encountered when reading file.";
	private static final String MESSAGE_ERROR_WRITE_FILE = "An error is encountered when writing file.";
	private static final String MESSAGE_ERROR_TYPE_COMMAND = "Unrecognized command type";
	private static final String MESSAGE_ERROR_NULL_COMMAND = "command type string cannot be null!";

	// These are the list of user commands
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_DELETE_LINE = "delete";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_EXIT = "exit";

	// Special Strings
	private static final String DOT_AND_SPACE = ". ";
	private static final String NEWLINE = "\n";
	private static final String EMPTY_STRING = "";
	private static final String SPACES = "\\s+";
	
	// This argument is given to signify normal termination of code.
	private static final int STATUS_CODE_EXIT = 0;

	// This is the correct number of parameter for the command
	private static final int PARAM_SIZE_FOR_DELETE_LINE = 1;
	private static final int PARAM_SIZE_FOR_SEARCH = 1;

	// This is the location at which the parameter will appear in the argument
	// when running the program.
	private static final int PARAM_POSITION_ARGS = 0;

	// This is the location of the word of a string.
	private static final int PARAM_POSITION_FIRST_WORD = 0;
	
	// This is the location of the first letter of a string.
	private static final int PARAM_POSITION_FIRST_LETTER = 0;

	// This is the location at which various parameter will appear in a command
	private static final int PARAM_POSITION_NUMBER = 0;

	// This is used to offset the counting which starts from 0.
	private static final int OFFSET = 1;

	// This is used to scan inputs and show outputs.
	private UserInterface userInterface;

	// The text file to be written to, will be stored here.
	private FileStorage textFile;

	// These are the possible command types.
	private enum CommandType {
		ADD_LINE, DISPLAY, DELETE_LINE, CLEAR, INVALID, EXIT, ADD, SORT, SEARCH
	};

	public TextBuddy(String fileName) {
		userInterface = new UserInterface();
		textFile = new FileStorage(fileName);
		
		userInterface.showWelcomeMessage(fileName);
	}

	public static void main(String[] args) {
		String textFileName = args[PARAM_POSITION_ARGS];

		runTextBuddy(textFileName);
	}

	/**
	 * Opens and reads from the specified file name, thereafter,
	 * allows the user to manipulate the text inside by command
	 * line interface. 
	 * The text file will be rewritten when user exits the program.
	 * 
	 * @param textFileName    name of file.
	 */
	private static void runTextBuddy(String textFileName) {
		TextBuddy textBuddy = new TextBuddy(textFileName);

		textBuddy.runLoopUserCommands();
	}

	/**
	 * Repeatedly query and run commands entered by the user,
	 * until when user exits the program.
	 */
	private void runLoopUserCommands() {
		while (true) {
			userInterface.displayCommandQuery(MESSAGE_QUERY);
			String userCommand = userInterface.readUserCommand();
			String feedback = processUserCommand(userCommand);
			userInterface.displayToUser(feedback);
		}
	}

	/**
	 * Returns a feedback and performs the necessary actions based on 
	 * the specified user command.
	 * If command is empty or invalid, error message will be returned.
	 * If command is exit, program will exit and save the edits into the file.
	 * 
	 * @param userCommand    command line given by user.
	 * @return               feedback to user.
	 */
	public String processUserCommand(String userCommand) {
		Parser userCommandInfo = new Parser(userCommand);
		
		CommandType commandType = userCommandInfo.getCommandType();
		
		String message = userCommandInfo.getMessage();

		switch (commandType) {
			case ADD_LINE :
				return addLine(message);
			case DISPLAY :
				return display();
			case DELETE_LINE :
				return deleteLine(message, userCommand);
			case CLEAR :
				return clear();
			case SORT :
				return sort();
			case SEARCH :
				return search(message);
			case INVALID :
				return String.format(MESSAGE_INVALID_FORMAT, userCommand);
			case EXIT :
				writeFileAndExit();
			default :
				// throw an error if the command is not recognized
				throw new Error(MESSAGE_ERROR_TYPE_COMMAND);
		}
	}

	private String sort() {
		return textFile.sort();
	}

	public void writeFileAndExit() {
		textFile.writeIntoFile();
		System.exit(STATUS_CODE_EXIT);
	}
	
	public String deleteLine(String message, String userCommand) {
		try {
			return textFile.deleteLine(message);
		} catch (IndexOutOfBoundsException exception) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
	}
	
	public String search(String searchWord) {
		return textFile.search(searchWord);
	}

	public String addLine(String textLineToAdd) {
		return textFile.addLine(textLineToAdd);
	}

	public String display() {
		return textFile.display();
	}

	public String clear() {
		textFile.clearAllContents();
		
		return String.format(MESSAGE_CLEAR, textFile.getFileName());
	}
	
	/**
	 * This class is used to scan in user inputs / commands, and output / displays
	 * feedback to user.
	 */
	private class UserInterface {
		private Scanner scanner;
		
		public UserInterface() {
			scanner = new Scanner(System.in);
		}
		
		public void displayError(String errorMessage) {
			System.out.println(errorMessage);
		}
		
		public String readUserCommand() {
			return scanner.nextLine();
		}
		
		public void showWelcomeMessage(String textFileName) {
			displayToUser(String.format(MESSAGE_WELCOME, textFileName));
		}
		
		private void displayToUser(String textToDisplay) {
			System.out.println(textToDisplay);
		}
	
		public void displayCommandQuery(String queryToDisplay) {
			System.out.print(queryToDisplay);
		}
	}
	
	/** 
	 * This class is used to parse user inputs so that the main class can
	 * retrieve the command type and the message if the command is valid.
	 * If the command or command-message combination is invalid, the getCommandType()
	 * method will return CommandType.INVALID. 
	 */
	private class Parser {
		private CommandType commandType;
		
		private String message;
		
		public Parser(String commandTypeString) {
			parseCommand(commandTypeString);
		}
		
		public CommandType getCommandType() {
			return commandType;
		}
		
		public String getMessage() {
			return message;
		}
		
		private void parseCommand(String userCommand) {
			if (isEmptyCommand(userCommand)) {
				commandType = CommandType.INVALID;
				message = userCommand;
			} else {
				commandType = determineCommandType(userCommand);
				message = removeFirstWord(userCommand);
				
				if (!isValidCommand(commandType, message)) {
					commandType = CommandType.INVALID;
					message = userCommand;
				}
			}
		}	
		
		private boolean isValidCommand(CommandType commandType, String givenMessage) {
			switch (commandType) {
				case ADD_LINE :
					if (isEmptyString(givenMessage)) {
						return false;
					} else {
						return true;
					}
				case DISPLAY :
				case CLEAR :
				case SORT :
				case EXIT :
					if (isEmptyString(givenMessage)) {
						return true;
					} else {
						return false;
					}
				case SEARCH :
					return isValidSearch(givenMessage);
				case DELETE_LINE :
					return isValidDelete(givenMessage);
				default :
					return false;
			}
		}
		
		private boolean isValidDelete(String givenMessage) {
			String[] parameters = splitParameters(givenMessage);
			
			if (parameters.length != PARAM_SIZE_FOR_DELETE_LINE) {
				return false;
			}
			
			String paramForLineNumber = parameters[PARAM_POSITION_NUMBER];

			if (isANumber(paramForLineNumber)) {
				return true;
			} else {
				return false;
			}
		}
		
		private boolean isValidSearch(String givenMessage) {
			String[] parameters = splitParameters(givenMessage);
			
			if (parameters.length != PARAM_SIZE_FOR_SEARCH) {
				return false;
			} else if (isEmptyString(parameters[PARAM_POSITION_FIRST_WORD])) {
				return false;
			} else {
				return true;
			}
		}
		
		private boolean isANumber(String parameter) {
			try {
				Integer.parseInt(parameter);

				return true;
			} catch (NumberFormatException notNumberException) {
				return false;
			}
		}
		
		private CommandType determineCommandType(String userCommand) {
			String commandTypeString = getFirstWord(userCommand);
			
			if (commandTypeString == null) {
				throw new Error(MESSAGE_ERROR_NULL_COMMAND);
			}

			if (commandTypeString.equalsIgnoreCase(COMMAND_ADD)) {
				return CommandType.ADD_LINE;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_DISPLAY)) {
				return CommandType.DISPLAY;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_DELETE_LINE)) {
				return CommandType.DELETE_LINE;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_CLEAR)) {
				return CommandType.CLEAR;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_SORT)) {
				return CommandType.SORT;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_SEARCH)) {
				return CommandType.SEARCH;
			} else if (commandTypeString.equalsIgnoreCase(COMMAND_EXIT)) {
				return CommandType.EXIT;
			} else {
				return CommandType.INVALID;
			}
		}
		
		private String removeFirstWord(String userCommand) {
			return userCommand.replace(getFirstWord(userCommand), EMPTY_STRING).trim();
		}

		private String[] splitParameters(String commandParametersString) {
			String[] parameters = commandParametersString.trim().split(SPACES);
			return parameters;
		}

		private String getFirstWord(String userCommand) {
			String commandTypeString = userCommand.trim().split(SPACES)[PARAM_POSITION_FIRST_WORD];
			return commandTypeString;
		}

		private boolean isEmptyCommand(String userCommand) {
			return isEmptyString(userCommand);
		}

		private boolean isEmptyString(String givenString) {
			if (givenString.trim().equals(EMPTY_STRING)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/** 
	 * This class is used to write and read files, and to manipulate any data regarding
	 * the file.
	 */
	private class FileStorage {
		// The name of the text file to be written to, will be stored here.
		private String fileName;

		// This ArrayList is used to store the lines of texts to be written to the
		// text file.
		private ArrayList <String> textFileLines;
		
		// Used only to show error messages with regards to file processing.
		private UserInterface errorMessageDisplayer;
		
		public FileStorage(String fileName) {
			this.fileName = fileName;
			textFileLines = new ArrayList<String>();
			errorMessageDisplayer = new UserInterface();
			
			readFromFile();
		}
		
		public String getFileName() {
			return fileName;
		}
		
		/**
		 * This operation reads the contents from specified file name
		 * and save them into the text buddy program.
		 * If specified file cannot be found, program will create that file.
		 */
		private void readFromFile() {
			try {
				FileReader fileToBeRead = new FileReader(fileName);
				extractContents(fileToBeRead);
				fileToBeRead.close();
			} catch (FileNotFoundException fileNotFoundException) {
				createNewFile();
			} catch (IOException iOException) {
				errorMessageDisplayer.displayError(MESSAGE_ERROR_READ_FILE);
				System.exit(STATUS_CODE_EXIT);
			}
		}
		
		public String sort() {
			if (textFileLines.isEmpty()) {
				return String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			} else {
				Collections.sort(textFileLines);
				return String.format(MESSAGE_SORT, fileName);
			}
		}
		
		public String search(String searchWord) {
			return findAllLinesWithWord(searchWord);	
		}
		
		private String findAllLinesWithWord(String searchWord) {
			String allLinesWithWord = EMPTY_STRING;
			
			for (int i = 0; i < textFileLines.size(); i++) {
				String lineToCheck = textFileLines.get(i);
				
				if (lineToCheck.contains(searchWord)) {
					int lineNumber = i + OFFSET;
					allLinesWithWord = allLinesWithWord + lineNumber + DOT_AND_SPACE + lineToCheck + NEWLINE;
				}
			}
			
			if (isEmptyString(allLinesWithWord)) {
				return String.format(MESSAGE_SEARCH_FAIL, searchWord);
			} else {
				return removeLastNewline(allLinesWithWord);
			}
		}
		
		private String removeLastNewline(String givenString) {
			return givenString.substring(PARAM_POSITION_FIRST_LETTER, givenString.length() - OFFSET);
		}
		
		private boolean isEmptyString(String givenString) {
			if (givenString.trim().equals(EMPTY_STRING)) {
				return true;
			} else {
				return false;
			}
		}
		
		private void extractContents(FileReader fileToBeRead) throws IOException {
			BufferedReader fileReader = new BufferedReader(fileToBeRead);
			readFileLineByLine(fileReader);
			fileReader.close();
		}

		private void readFileLineByLine(BufferedReader fileReader)
				throws IOException {
			while (true) {
				String lineRead = fileReader.readLine();
				if (lineRead == null) {
					break;
				} else {
					textFileLines.add(lineRead);
				}
			}
		}
		
		private void createNewFile() {
			File fileToBeCreated = new File(fileName);
			try {
				fileToBeCreated.createNewFile();
			} catch (IOException iOException) {
				errorMessageDisplayer.displayError(MESSAGE_ERROR_CREATE_FILE);
				System.exit(STATUS_CODE_EXIT);
			}
		}
		
		public void writeIntoFile() {
			try {
				writeFile();
			} catch (FileNotFoundException notFoundException) {
				errorMessageDisplayer.displayError(MESSAGE_ERROR_WRITE_FILE);
				System.exit(STATUS_CODE_EXIT);
			}
		}
		
		/**
		 * This operation writes the final contents after manipulation of text file by user,
		 * into the text file.
		 * 
		 * @throws FileNotFoundException    if text file to be written to does not exist.
		 */
		private void writeFile() throws FileNotFoundException {
			PrintWriter writer = new PrintWriter(fileName);
			for (int i = 0; i < textFileLines.size(); i++) {
				writer.println(textFileLines.get(i));
			}
			writer.close();
		}
		
		public String deleteLine(String message) throws IndexOutOfBoundsException {
			String lineDeleted = deleteLineFromTextFile(message);
			
			return String.format(MESSAGE_DELETE_LINE, fileName, lineDeleted);
		}

		private String deleteLineFromTextFile(String lineNumberString) throws IndexOutOfBoundsException {
			int lineNumber = Integer.parseInt(lineNumberString);

			String removedLine = textFileLines.remove(lineNumber - OFFSET);

			return removedLine;
		}
		
		public String addLine(String textLineToAdd) {
			textFileLines.add(textLineToAdd);

			return String.format(MESSAGE_ADD_LINE, fileName, textLineToAdd);
		}
		
		/**
		 * Returns the text file contents.
		 * Returns text file is empty if text file is empty.
		 * 
		 * @return               contents in the text file.
		 */
		public String display() {
			if (textFileLines.isEmpty()) {
				return String.format(MESSAGE_DISPLAY_EMPTY, fileName);
			} else {
				return processTextFileContent();
			}
		}

		public void clearAllContents() {
			textFileLines = new ArrayList<String>();
		}

		/**
		 * This operation processes the current text file content and returns a
		 * String which can properly display the content of the current text file
		 * with their line numbers.
		 * 
		 * @return    text file contents with line numbering.
		 */
		private String processTextFileContent() {
			String toDisplay = EMPTY_STRING;

			toDisplay = addLineByLineToDisplay(toDisplay);

			return toDisplay;
		}

		private String addLineByLineToDisplay(String toDisplay) {
			for (int i = 0; i < textFileLines.size(); i++) {
				String lineToAdd = processLineToAdd(i);
				toDisplay = toDisplay + lineToAdd;
			}
			return toDisplay;
		}

		/**
		 * Returns the text content of specified line number, with the line number prefixed.
		 * 
		 * @param lineNumber    line number in text file.
		 * @return              text content at specified line number with numbering.
		 */
		private String processLineToAdd(int lineNumber) {
			int displayedLineNumber = lineNumber + OFFSET;

			String formattedLine = displayedLineNumber + DOT_AND_SPACE
					+ textFileLines.get(lineNumber);

			if (isLastLine(lineNumber)) {
				return formattedLine;
			} else {
				return formattedLine + NEWLINE;
			}
		}

		private boolean isLastLine(int lineNumber) {
			if (lineNumber == textFileLines.size() - OFFSET) {
				return true;
			} else {
				return false;
			}
		}
	}
} 
