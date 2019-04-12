import java.io.*;
import java.util.Scanner;

/************************************************
 * This is the class to unmix a previously mixed message
 *
 * Created by Tim Bomers and Matt Hendrick
 *
 * CIS 163
 *
 ************************************************/
public class UnMix {
	private DoubleLinkedList<Character> message;

	public UnMix() {
		message = new DoubleLinkedList<Character>();
	}

	public static void main(String[] args) {
	    UnMix v = new UnMix();
		v.unMixture(args[0], args[1]);
	}

	public String processCommand(String command) {
		String token = "";
		int index = -1;
		Scanner scan = new Scanner(command);
		char charInput;

		try {
			command = scan.next();
			switch (command.charAt(0)) {

				case 'b':
					token = scan.next();
					index = scan.nextInt();
					this.remove(token, index);
				case 'r':
					index = scan.nextInt();
					token = scan.next();
					this.insertbefore(index, token);
			}
		} catch (Exception e) {
			System.out.println("Error in command!  Problem!!!! in undo commands");
			System.exit(0);
		}
		finally {
			scan.close();
		}

		return message.toString();
	}

	private void insertbefore(int index, String token) {
		for (int i = token.length() - 1; i >= 0; i--) {
			message.add(index, token.charAt(i));
		}
	}

	private void remove(String token, int index) {
		for (int i = index; i <= index + token.length(); i++) {
			message.remove(index);
		}
	}

	private void unMixture(String filename, String userMessage) {
		String original = UnMixUsingFile (filename, userMessage);
		System.out.println ("The Original message was: " + original);
	}


	public String UnMixUsingFile(String filename, String userMessage) {
		for( int i = 0; i < userMessage.length(); i++) {
			message.add(userMessage.charAt(i));
		}

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			String command = scanner.nextLine();
			userMessage = processCommand(command);
		} 
		return userMessage;
	}
}
