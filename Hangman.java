/**
 * Main method to run the Hangman game. It initialises the game with a list of words, 
 * then repeatedly allows the player to guess letters for a randomly selected word until the player decides to stop.
 *
 * @param args Command-line arguments that can optionally be used to specify a custom list of words for the game.
 */


import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Hangman {
    static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {

    //Initialize the game with a list of words and set the play flag to 'y'
    
    String[] words = wordList(args);
    String playGame = "y";
    while (playGame.equals("y")) {
        int wrongGuesses = 0;
        String chosenWord = selectRandomWord(words);
        char[] chosenWordCharList = convertWordToCharList(chosenWord);
        char[] asteriskList = changeWordToAsterisk(chosenWordCharList);
        ArrayList<Character> guessedLetters = new ArrayList<>();

        //Keep asking for guesses until the word is fully guessed
        
        while (!String.valueOf(asteriskList).equals(chosenWord)) {
            char guess = userGuess(String.valueOf(asteriskList), guessedLetters);

            //Skip the loop iteration if the letter was already guessed

            if (guessedLetters.contains(guess)) {
                System.out.println("\t" + guess + " has already been guessed.");
                continue;
            }
            
            guessedLetters.add(guess);
            int feedback = updateCurrentGuess(chosenWordCharList, asteriskList, guess);
            if (feedback == 2) {
                wrongGuesses++;
            }
            printFeedback(feedback, guess);
        }
        
        //After the word is guessed, print the result and ask if the player wants to play again

        System.out.println("The word is " + chosenWord + ". You missed " + wrongGuesses + " time" + (wrongGuesses == 1 ? "" : "s"));
        System.out.print("Do you want to guess another word? Enter y or n> ");
        playGame = keyboard.next().toLowerCase().trim();
    }
}


/**
 * Prompts the user to guess a letter within the current word being guessed, ensuring the input is valid.
 *
 * @param wordDisplay The current state of the word being guessed, with unguessed letters represented as asterisks.
 * @param guessedLetters A list of letters that have already been guessed.
 * @return The letter guessed by the user.
 */



    public static char userGuess(String wordDisplay, ArrayList<Character> guessedLetters){
        System.out.print("(Guess) Enter a letter in word " + wordDisplay + " > ");
        String input = keyboard.nextLine().trim().toLowerCase(); // Trim and convert to lower case
        
        while (input.isEmpty() || !Character.isLetter(input.charAt(0)) || input.length() > 1 || guessedLetters.contains(input.charAt(0))) {
            if (input.isEmpty()) {
                System.out.println("No input detected. Please enter a letter.");
            } else if (input.length() > 1) {
                System.out.println("Multiple characters detected. Please enter only one letter.");
            } else if (!Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid input. Please enter a letter.");
            } else if (guessedLetters.contains(input.charAt(0))) {
                System.out.println("You have already guessed that letter. Choose another.");
            }
            System.out.print("(Guess) Enter a letter in word " + wordDisplay + " > ");
            input = keyboard.nextLine().trim().toLowerCase();
        }
        
        return input.charAt(0);
    }

/**
 * Generates a list of words for the Hangman game. Uses command-line arguments if provided, otherwise uses a default set.
 *
 * @param args Command-line arguments specifying custom words for the game.
 * @return An array of words to be used in the game.
 */

    public static String[] wordList(String[] args) {
        String[] words;
        if (args.length > 0) {
            words = args;
        } else {
            words = new String[]{
                "quartz", "hydroflask", "jigsaw", "zephyr", "quixotic", "juxtapose",
                "oxygen", "syndrome", "vortex", "microwave", "kayak", "gazebo",
                "fjord", "glyph", "mystique", "rhythmic", "zodiac", "wizard",
                "quorum", "voyage", "whiskey", "sphinx", "quiver", "zigzag",
                "yacht", "flapjack", "joyful", "buzzard", "vexing", "jockey",
                "waltz", "queue", "jovial", "pixel", "zombie", "twelfth",
                "knapsack", "ivory", "jungle", "boxcar", "quiz", "yak"
            };
        }
        return words;
    }


/**
 * Selects a random word from the given list to be used as the target word for the Hangman game.
 *
 * @param list An array of words to choose from.
 * @return A randomly selected word from the list.
 */


    public static String selectRandomWord(String[] list) {
        Random rand = new Random();
        return list[rand.nextInt(list.length)];
    }


    /**
 * Converts a string word into an array of characters.
 *
 * @param chosenWord The word to convert.
 * @return An array of characters representing the word.
 */

    public static char[] convertWordToCharList(String chosenWord) {
        return chosenWord.toCharArray();
    }


    /**
 * Replaces each character in a word with an asterisk to hide its letters before guessing begins.
 *
 * @param wordAsList The word to be hidden, represented as an array of characters.
 * @return An array of asterisks of the same length as the word.
 */


    public static char[] changeWordToAsterisk(char[] wordAsList) {
        char[] asterisks = new char[wordAsList.length];
        Arrays.fill(asterisks, '*');
        return asterisks;
    }


    /**
 * Updates the current guess by replacing asterisks with correctly guessed letters and provides feedback.
 *
 * @param word The target word as an array of characters.
 * @param blankList The current state of the guessed word, with unguessed letters as asterisks.
 * @param userGuess The letter guessed by the user.
 * @return An integer indicating the result of the guess (0 for correct and new, 1 for correct but already revealed, 2 for incorrect).
 */

    public static int updateCurrentGuess(char[] word, char[] blankList, char userGuess) {
        int feedback = 2; // Assume the guess is incorrect initially
        for (int i = 0; i < word.length; i++) {
            if (word[i] == userGuess) {
                if (blankList[i] == userGuess) {
                    feedback = 1; // Guess is correct but already revealed
                } else {
                    blankList[i] = userGuess; // Reveal the letter
                    feedback = 0; // Guess is correct and newly revealed
                }
            }
        }
        return feedback;
    }

    
    /**
 * Prints feedback to the user based on the result of their guess.
 *
 * @param feedback The result of the user's guess (correct and revealed, correct but already revealed, or incorrect).
 * @param guess The letter guessed by the user.
 */

    public static void printFeedback(int feedback, char guess) {
        switch (feedback) {
            case 1:
                System.out.println("\t" + guess + " is already in the word");
                break;
            case 2:
                System.out.println("\t" + guess + " is not in the word");
                break;
        }
    }
}