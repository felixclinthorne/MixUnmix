/*********************************************************
 * This is the class to mix a given message in various ways.
 * We can remove characters in a given range as well as replace
 * each instance of a given character with another character.  We
 * can also insert a string of characters at any point in the message
 * as well.  We can also do a random mix of the above commands.
 *
 * Created by Tim Bomers and Matt Hendrick
 * CIS 163
 *
 *********************************************************/

import java.io.*;
import java.util.Scanner;
import java.util.Random;


public class Mix {

    //The message to be mixed
    private DoubleLinkedList<Character> message;
    //The commands that will be saved so we can undo them later
    private String undoCommands;
    //A series of clipboards from/to which to cut, copy, and paste
    private clipBdLinkedList clipBoards;
    //The new message
    private String userMessage;
    //The scanner to accept commands
    private Scanner scan;

    /**
     * Ths constructor for the Mix class
     */
    public Mix() {
        scan = new Scanner(System.in);
        message = new DoubleLinkedList<Character>();
        clipBoards = new clipBdLinkedList();
        undoCommands = "";
    }

    /**
     * Runs the main program and brings up the command screen
     *
     * @param args Character array
     */
    public static void main(String[] args) {
        Mix mix = new Mix();
        mix.userMessage = args[0];
        System.out.println (mix.userMessage);
        mix.mixture();
    }


    /**
     * This is the main portion of the Mix class.  This method will
     * contain all of the commands for message mixing with different
     * functions for each case.  In the case of an illegal entry, the
     * program will reset the message to the state it was in before the
     * illegal entry.
     */
    private void mixture() {
        //Loops through the message and assigns each character in the
        //userMessage to its own character slot for the purposes of
        //message manipulation
        for (int i = 0; i < userMessage.length(); i++) {
            message.add(userMessage.charAt(i));
        }
        //While in the command screen, keep allowing input of commands for
        //multiple message edits/mixes
        do {
            //Displays the current message and asks for command input
            DisplayMessage();
            System.out.print("Command: ");

            // save state
            DoubleLinkedList<Character> currMessage =  new DoubleLinkedList<>();
            //Constructs the current message and also acts as a restore point
            //in the event of an illegal action
            for(int i = 0; i < message.size(); i++) {
                currMessage.add(message.get(i));
            }
            //Sets the undo commands to the current undo commands
            String currUndoCommands = undoCommands;

            try {
                String command = scan.next("[Qbrdcxphz]");

                switch (command) {
                    //Quits the program, but saves and prints out the final mixed message
                    case "Q":
                        save(scan.next());
                        System.out.println ("Final mixed up message: \"" + message+"\"");
                        System.exit(0);
                    //Inserts a given string before the given character slot
                    case "b":
                        if(scan.hasNext()) {
                            String str = scan.next();
                            int num;
                            if (scan.hasNextInt()) {
                                num = scan.nextInt();
                                this.insertbefore(str, num);
                            }
                            else {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                        break;
                    //There are two types of 'r' commands.  In the case of 'r' followed
                    //by two integers, remove the characters in the range of those integers.
                    //If 'r' is followed by two other characters, replace any instance of
                    //the first input character with the second input character.
                    case "r":
                        //If the first entry is an integer, make sure the second entry
                        //is also an integer, else throw an error
                    	if( scan.hasNextInt()) {
                    		int num1 = scan.nextInt();
							int num2;
                    		if (scan.hasNextInt()) {
                    			num2 = scan.nextInt();
							}
                    		else {
                    			throw new IllegalArgumentException();
							}
                    		//Remove the character slots between (and including) num1 and num2
                    		remove(num1, num2);
						} else {
                    	    //Otherwise, make sure that if the entries are strings, make sure
                            //they are exactly one character long.  If so, replace str1 with
                            //str2.  If not, throw an error.
                    		String str1 = scan.next();
                    		String str2 = scan.next();
                    		if(str1.length() == 1 && str2.length() == 1) {
                    			replace(str1.charAt(0), str2.charAt(0));
							} else {
                    			throw new IllegalArgumentException();
							}
						}
                        break;
                    //Deletes any instance of a given character.  Also makes sure the
                    //string length is only one character, throws an error otherwise
					case "d":
						String str3 = scan.next();
						if(str3.length() == 1) {
							delete(str3.charAt(0));
						} else {
							throw new IllegalArgumentException();
						}
						break;

                    //Copies a given string
                    case "c":
                        if (scan.hasNextInt()) {
                            if (scan.hasNextInt()) {
                                if (scan.hasNextInt()) {
                                    this.copy(scan.nextInt(), scan.nextInt(), scan.nextInt());
                                    break;
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    //Cuts a given string
                    case "x":
                        if (scan.hasNextInt()) {
                            if (scan.hasNextInt()) {
                                if (scan.hasNextInt()) {
                                    this.cut(scan.nextInt(), scan.nextInt(), scan.nextInt());
                                    break;
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    //Pastes a given string
                    case "p":
                        if (scan.hasNextInt()) {
                            if (scan.hasNextInt()) {
                                this.paste(scan.nextInt(), scan.nextInt());
                                break;
                            }
                        }
                    //Brings up the help screen with all the commands
                    case "h":
                        this.helpPage();
                        break;
                    //Does a random assortment of the previous commands
					case "z":
					    this.randomize();
						break;
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

    /**
     * This method will go through the message and delete any instance
     * of the given character.  For example, the word "testing" should
     * look like "esing" after this command is selected.
     *
     * @param c The character to be deleted
     */
    private void delete(char c) {
        //Loops through the message for any instance of
        //the given character, then removes it.
    	for (int i = 0; i < message.size(); i++) {
			while(message.get(i) == c) {
			    if(message.get(i) == ' ') {
                    undoCommands = undoCommands + "r " + i + " " + "~" + "\n";
                } else {
                    undoCommands = undoCommands + "r " + i + " " + message.get(i) + "\n";
                }
			    message.remove(i);
            }
		}
	}

    /**
     * This message will remove characters in a given range.  For example,
     * r 3 5 will remove characters 3 through 5.
     *
     * @param start The first character to be deleted
     * @param stop The last character to be deleted
     */
    private void remove(int start, int stop) {
        //Checks for illegal entries, such as a starting point less than zero,
        //a stopping point beyond the message, and a starting point greater
        //than the stopping point
    	if (start < 0 || stop >= message.size() - 1
				|| start > stop) {
    		throw new IllegalArgumentException();
    	}
    	//Reconstructs the string, then deletes the specified portion
    	String temp = "";
    	for (int i = start; i <= stop; i++) {
    		if (message.get(start) == ' ') {
                temp = temp + "~";
            } else {
                temp = temp + message.get(start);
            }
    		message.remove(start);
    	}
    	//Places the message into the undo command.
    	undoCommands = undoCommands + "r " + start + " " + temp + "\n";
    }

    /**
     * Replaces a given character with another character
     *
     * @param find The character to be replaced
     * @param replace The character that replaces any instance of "find"
     */
    private void replace(char find, char replace) {
        //Loops through for any instance of find, removes it, then
        //replaces it with replace.
		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) == find) {
				this.remove(i, i);
				this.insertbefore(String.valueOf(replace), i);
			}
		}
	}


    /**
     * This method will cut a portion of the message and save it to
     * a clipboard to be used later.
     *
     * @param clipNum The assigned clipboard number
     * @param start The starting point of the clipped portion
     * @param stop The end point of the clipped portion
     */
    private void cut(int clipNum, int start, int stop) {
        //Copies, then removes the portion
        this.copy(clipNum, start, stop);
        this.remove(start, stop);
    }

    /**
     * This method will copy a portion of the message and save it to
     * a clipboard to be used later.
     *
     * @param start The starting point of the copied portion
     * @param stop The stopping point of the copied portion
     * @param clipNum The assigned clipboard number
     */
    private void copy(int clipNum, int start, int stop) {
        //Copies the substring and assigns it to a clipboard
        String copyString = userMessage.substring(start, stop + 1);
        clipBoards.add(clipNum, copyString);
        return;
    }

    /**
     * This method will paste a portion of the message to another message
     *
     * @param clipNum The assigned clipboard number
     * @param index The point to which to insert the message
     */
    private void paste( int clipNum, int index) {
        //Reads the appropriate clipboard and inserts it at the index point
        String pasteString = clipBoards.get(clipNum).getMyLinkedList().toString();
        insertbefore(pasteString, index);
    }

    /**
     * This method will allow a substring to be entered at a given point in
     * the message.  This is also used while pasting messages from a clipboard
     *
     * @param token The string to be inserted
     * @param index The point at which the new string is inserted
     *
     * @throws IllegalArgumentException
     */
    private void insertbefore(String token, int index) throws IllegalArgumentException{
        //Throws an error if the index is beyond the message bounds
        if(index >= message.size()) {
            throw new IllegalArgumentException();
        }

        //Adds the insertion into the undo commands
        String newToken = "";
        for(int i = 0; i < token.length(); i++) {
            if(token.charAt(i) == ' ') {
                newToken = newToken + "~";
            } else {
                newToken = newToken + token.charAt(i);
            }
        }
        undoCommands = undoCommands + "b " + newToken + " " + index + "\n";
        //Loops through the message and determines where to add the substring
        for (int i = token.length() - 1; i >= 0; i--) {
            message.add(index, token.charAt(i));
        }
        return;
    }

    private void randomize() {
        System.out.println("called");
        Random rand = new Random();
        int n = rand.nextInt(4) + 1;

        for (int i = 0; i < n; i++) {
            int choice =  rand.nextInt(4);
            int index1;
            int index2;
            String chars;
            String token;
            int position;
            switch (choice) {
                case 0:
                    index1 = rand.nextInt(this.message.size());
                    chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    token = "";
                    for(int j = 0; j < rand.nextInt(4) + 1; j++) {
                        position = rand.nextInt(chars.length());
                        token = token + chars.charAt(position);
                    }
                    this.insertbefore(token, index1);
                    break;
                case 1:
                    if (this.message.size() > 2) {
                        index1 = rand.nextInt(this.message.size() - 1);
                        index2 = index1 + rand.nextInt(this.message.size() - 1 - index1);
                        this.remove(index1, index2);
                    }
                    break;
                case 2:
                    chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    token = String.valueOf(message.get(rand.nextInt(message.size() - 2)));
                    this.delete(token.charAt(0));
                    break;
                case 3:
                    chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    this.replace(message.get(rand.nextInt(message.size() - 2)), chars.charAt(rand.nextInt(chars.length())));
                    break;
            }
        }
    }

    /**
     * This method will display the message along with its character
     * assignments.  This will help the user scramble the message more
     * effectively
     */
    private void DisplayMessage() {
        //Prints the current message
        System.out.print ("Message:\n");
        userMessage = message.toString();

        //Loops through the current message and prints out each
        //character assignment along with the characters themselves
        for (int i = 0; i < userMessage.length(); i++)
            System.out.format ("%3d", i);
        System.out.format ("\n");
        for (char c : userMessage.toCharArray())
            System.out.format("%3c",c);
        System.out.format ("\n");
    }

    /**
     * This message will save to a file from which the unmixer will read
     *
     * @param filename The file to which the message will be saved
     */
    public void save(String filename) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Prints the undo commands to be read later
        out.println(undoCommands);
        out.close();
    }

    /**
     * This method is the help page to help users learn the various commands.
     */
    private void helpPage() {
        System.out.println("Commands:");
        System.out.println("\t'Q' will quit the program and print the final mixed-up message");
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
        System.out.println("\t'd #' will delete all of the '#' in the message. '#' is one character");
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
