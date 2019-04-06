/*********************************************************
 * This is the class to mix a given message in various ways.
 *
 * Created by Tim Bomers and Matt Hendrick
 * CIS 163
 *
 *********************************************************/

import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;

public class Mix {

	private DoubleLinkedList<Character> message;
	private String undoCommands;
	//TODO: Switch to LL
	private Hashtable<Integer, DoubleLinkedList<Character>> clipBoards;

	private String userMessage;
	private Scanner scan;

	public Mix() {
		scan = new Scanner(System.in);
		message = new DoubleLinkedList<Character>();
		//TODO: Switch to LL
		clipBoards = new Hashtable<Integer, DoubleLinkedList<Character>>();

		undoCommands = "";
	}

	public static void main(String[] args) {
		Mix mix = new Mix();
		mix.userMessage = args[0];
		System.out.println (mix.userMessage);
		mix.mixture();
	}


	private void mixture() {
		do {
			for (int i = 0; i < userMessage.length(); i++) {
				message.add(userMessage.charAt(i));
			}
			DisplayMessage();
			System.out.print("Command: ");

			// save state
			DoubleLinkedList<Character> currMessage =  new DoubleLinkedList<>();
			String currUndoCommands = undoCommands;

			try {
				String command = scan.next("[Qbrpcxh]");

				switch (command) {
				case "Q":
					save(scan.next());
					System.out.println ("Final mixed up message: \"" + message+"\"");
					System.exit(0);
				case "b":
					insertbefore(scan.next(), scan.nextInt());
					break;
				case "r":
					remove(scan.nextInt(), scan.nextInt());
					break;
				case "c":
					copy(scan.nextInt(), scan.nextInt(), scan.nextInt());
					break;
				case "x":
					cut(scan.nextInt(), scan.nextInt(), scan.nextInt());
					break;
				case "p":
					paste(scan.nextInt(), scan.nextInt());
					break;
				case "h":
					helpPage();
					break;

					// all the rest of the commands have not been done
                    // No "real" error checking has been done.
				}
				scan.nextLine();   // should flush the buffer
				System.out.println("For demonstration purposes only:\n" + undoCommands);
			}
			catch (Exception e ) {
				System.out.println ("Error on input, previous state restored.");
				scan = new Scanner(System.in);  // should completely flush the buffer

				// restore state;
				undoCommands = currUndoCommands;
				message = currMessage ;
			}

		} while (true);
	}

	private void remove(int start, int stop) {
        //TODO: check that the start and end is valid

        undoCommands = undoCommands + "r " + start + " " +stop + " ";

        //TODO: Remove the specified elements AND add them to "undoCommands"

        undoCommands = undoCommands + "\n";


	}

	private void cut(int start, int stop, int clipNum) {

	}

	private void copy(int start, int stop, int clipNum) {

	}

	private void paste( int index, int clipNum) {

	}
         
	private void insertbefore(String token, int index) throws IllegalArgumentException{
	    if(index >= message.size()) {
	        throw new IllegalArgumentException();
        }

	    undoCommands = undoCommands + "b " + token + " " + index + "\n";
	    for (int i = token.length() - 1; i >= 0; i--) {
	      message.add(token.charAt(i));
        }
	}

	private void DisplayMessage() {
		System.out.print ("Message:\n");
		userMessage = message.toString();

		for (int i = 0; i < userMessage.length(); i++) 
			System.out.format ("%3d", i);
		System.out.format ("\n");
		for (char c : userMessage.toCharArray()) 
			System.out.format("%3c",c);
		System.out.format ("\n");
	}

	public void save(String filename) {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(undoCommands);
		out.close();
	}

	private void helpPage() {
		System.out.println("Commands:");
		System.out.println("\t'q' will quit the program and print the final mixed-up message");
		System.out.println("\tto the screen.  It will also save the set of undo commands into");
		System.out.println("\ta textfile named 'filename'");
		System.out.println();
		System.out.println("\t~ is used for a space character" );
		System.out.println();
		System.out.println("\t'b s #' will insert String 's' at position #.  This is an insert");
		System.out.println("\tbefore command.  Example: b abc 3 would insert 'abc' starting at");
		System.out.println("\tposition 3.");
		System.out.println();
		System.out.println("\t'r # *' remove all the characters within the message, range # to *");
		System.out.println("\tExample: 'r 3 5 would start at position 3 and remove 3, 4, 5");
		System.out.println();
		System.out.println("\t'h'\tmeans to show this help page");
		System.out.println();
		System.out.println("\t'd #' deletes all of the '#' in the message. '#' is one character");
		System.out.println();
		System.out.println("\t'r # *' replaces all the # with * in the message. # and * are each one character");
		System.out.println();
		System.out.println("\t'z' randomizes the message and mixes it a multiple number of times");
	}
}
