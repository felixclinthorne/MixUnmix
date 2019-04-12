import java.io.*;
import java.util.Scanner;

/************************************************
 * This is the class to unmix a previously mixed message and
 * restore it to its original state
 *
 * Created by Tim Bomers and Matt Hendrick
 *
 * CIS 163
 *
 ************************************************/
public class UnMix {
	private DoubleLinkedList<Character> message;

	/**
	 * This is the constructor that will establish "message" as
	 * a new doubly linked list
	 */
	public UnMix() {
		message = new DoubleLinkedList<Character>();
	}

	public static void main(String[] args) {
	    UnMix v = new UnMix();
		v.unMixture(args[0], args[1]);
	}

	/**
	 * This method will read the file for a 'b' or an 'r'.  It will either
	 * insert or remove the appropriate text based on the initial letter.
	 *
	 * @param command The command that is put into the scanner
	 * @return The message string
	 */
	public String processCommand(String command) {
		//Initializing the variables to be used in the scanner
		String token = "";
		int index = -1;
		Scanner scan = new Scanner(command);
		char charInput;

		try {
			command = scan.next();
			switch (command.charAt(0)) {
				//Removes the text starting at the specified location
				case 'b':
					token = scan.next();
					index = scan.nextInt();
					this.remove(token, index);
					break;
				//Inserts the text starting at the specified location
				case 'r':
					index = scan.nextInt();
					token = scan.next();
					this.insertbefore(index, token);
					break;
			}
		} catch (Exception e) {
			//Checks for problems in the undo commands such as unaccounted for spaces
			System.out.println("Error in command!  Problem!!!! in undo commands");
			System.exit(0);
		}
		finally {
			scan.close();
		}

		return message.toString();
	}

	/**
	 * Inserts the token beginning at the index point, similar to the Mix method of
	 * the same name.
	 *
	 * @param index The point at which the message is to be inserted
	 * @param token The string to be inserted
	 */
	private void insertbefore(int index, String token) {
		//Checks for a tilde character, used previously to allow proper undo commmand
		//reading.  If a tilde exists, remove it and replace it with a space so it looks
		//like the original message.
		for (int i = token.length() - 1; i >= 0; i--) {
            if (token.charAt(i) == '~') {
                message.add(index, ' ');
            } else {
                message.add(index, token.charAt(i));
            }
		}
	}

	/**
	 * This method removes the text starting at the directed index point
	 *
	 * @param token The text to be removed
	 * @param index The point at which removal starts
	 */
	private void remove(String token, int index) {
		//Loops through from the starting point to the end of the section to
		//be removed
		for (int i = index; i < index + token.length(); i++) {
			message.remove(index);
		}
	}

	/**
	 * This message calls the unmixing method and finishes by printing the
	 * original message.  The final message will not include any tilde spaces
	 * (as they were used as placeholders when queueing up the undo commands)
	 *
	 * @param filename The name of the file
	 * @param userMessage The message to be translated
	 */
	private void unMixture(String filename, String userMessage) {
		//Converts the mixed message back to its original message
		String original = UnMixUsingFile (filename, userMessage);
		System.out.println ("The Original message was: " + original);
	}

	/**
	 * This method will translate the mixed message back to its original state.
	 *
	 * @param filename The file to which the commands are stored
	 * @param userMessage The message to be translated
	 * @return
	 */
	public String UnMixUsingFile(String filename, String userMessage) {
		String[] stringList = new String[0];
		String[] tempStringList = new String[0];
		for( int i = 0; i < userMessage.length(); i++) {
			message.add(userMessage.charAt(i));
		}

		Scanner scanner = null;
		//Checks for a valid filename.  If one doesn't exist, throw an error
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//This will read the file for the command list.  While there are commands to
		//be read, this will continue and add to the temporary string list.
		while (scanner.hasNext()) {
			String command = scanner.nextLine();
			tempStringList = new String[stringList.length + 1];
			for(int i = 0; i < stringList.length; i++) {
				tempStringList[i] = stringList[i];
			}
			tempStringList[stringList.length] = command;
			stringList = tempStringList;
		}
		//Loops through the stringList and runs the commands in reverse to translate
		//the mixed message back to the original
		for(int i = stringList.length - 1; i >= 0; i--) {
			userMessage = processCommand(stringList[i]);
		}
		return userMessage;
	}
}
