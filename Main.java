import java.util.*;
import java.io.*;

public class Main {
    private static boolean gameOver;
    static List<Bag> bags = Collections.synchronizedList(new ArrayList<Bag>());
    public static void main(String[] args){

        Random randomGenerator; // Create random object for random numbers

        // Intro to the game
        System.out.println("""
                Welcome to the PebbleGame!!
                You'll be asked to enter the number of players and the location of the 3 files containing comma separated integer values for the pebble weights.
                The values must be strictly positive.
                The game will then be simulated, and the output will be written to files in this directory.

                Enter the number of players:\s""");

        // User inputs number of players
        int nOfPlayers = 0; // Initialise variable
        Scanner inputScanner = new Scanner(System.in); // Create scanner for input
        String input = inputScanner.nextLine(); // Get user input

        // Check if input is valid -- prompt user to re-enter value if invalid
        while (!validInput(input)) {
            System.out.println("Invalid input. Please enter a non-zero integer:");
            input = inputScanner.nextLine();
        }
        nOfPlayers = Integer.parseInt(input); // Already checked if input is integer, so it's safe to parse

        // User inputs paths for bags
        final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z")); // Alphabet list to name Bags

        // Create 3 empty white bags
        for (int i = 0; i < 3; i++) {
            bags.add(new Bag(new ArrayList<Integer>()));
        }

        boolean enoughPebbles = false;
        int totalPebbles = 0;

        // Check if CSV can be converted to ArrayList
        while (!enoughPebbles) {
            for (int i = 0; i < 3; i++) { // Loop 3 times to enter 3 black Bags
                boolean isValid = false;
                while(!isValid) {
                    String currentBag = alphabet.get(i + 3); // Get name of current pebblepackage.Bag
                    System.out.println("Enter the location of bag " + currentBag + " to load:");
                    String path = inputScanner.nextLine(); // User input
                    try {
                        String line = "";
                        // Create CSV file reader
                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            line = line.replaceAll("\\s+", ""); // Remove spaces
                            String[] lineArray = line.split(","); // Remove commas

                            // Convert to list
                            List<String> tempList = Arrays.asList(lineArray);
                            List<Integer> tempIntList = new ArrayList<>();
                            for (String s : tempList) {
                                tempIntList.add(Integer.valueOf(s)); // Convert to list of integers
                                totalPebbles += 1;
                            }
                            isValid = true;
                            List<Integer> finalList = Collections.synchronizedList(tempIntList);
                            bags.add(new Bag(finalList)); // Add pebblepackage.Bag to list of Bags
                        }
                        // Error handling
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found.\n");
                    } catch (IOException e) {
                        System.out.println("Error loading file.\n");
                    }
                }
            }
            if (totalPebbles >= (nOfPlayers * 11)) { // Check if total number of pebbles is at least 11 times the number of players
                enoughPebbles = true;
            } else {
                System.out.println("A total of " + nOfPlayers * 11 + " pebbles is needed to start the game.\n");
            }
        }

        // Creating threads
        for (int p = 0; p < nOfPlayers; p++) {
            Thread playerThread = new Thread(new Player(p, new ArrayList<Integer>(), 0, 0)); // Create new Player thread for each player
            playerThread.start();
        }
        System.out.println("Simulating game..."); // User interface
    }

    // Nested Player class
    public static class Player implements Runnable{

        // Initialise variables
        private int turn;
        private ArrayList<Integer> pebbles;
        private int whiteBag;
        private int blackBag;

        @Override
        public synchronized void run() { // Method to run on thread creation
            final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z"));

            //Creation of player output file for thread
            try {
                File myFile = new File("player" + (this.getTurn() + 1) + "_output.txt");
                if (myFile.exists()) {
                    myFile.delete();
                }
                if (myFile.createNewFile()) {
                }
                // Error handling
            } catch (IOException e) {
                System.out.println("An error occurred when creating the files.");
                e.printStackTrace();
            }

            // pebblepackage.Main logic loop -- will loop until a Player wins the game
            while (!gameOver) {
                takeTurn(this, bags);
                if (checkTotalWeight(this) == 100) {
                    gameOver = true;
                    declareWinner(this);
                }
            }
        }

        // Player attribute constructor
        public Player(int turn, ArrayList<Integer> pebbles, int whiteBag, int BlackBag) {
        this.turn = turn;
        this.pebbles = pebbles;
        this.whiteBag = whiteBag;
        this.blackBag = blackBag;
        }

        // Player getters and setters
        public int getTurn() {return turn;}

        public void setTurn(int turn) {this.turn = turn;}

        public ArrayList<Integer> getPebbles() {return pebbles;}

        public void setPebbles(ArrayList<Integer> pebbles) {this.pebbles = pebbles;}

        public int getWhiteBag() {return whiteBag;}

        public void setWhiteBag(int whiteBag) {this.whiteBag = whiteBag;}

        public int getBlackBag() {return blackBag;}

        public void setBlackBag(int blackBag) {this.blackBag = blackBag;}
    }

    // Method to choose random pebblepackage.Bag on new turn
    public static void chooseBags(Player player, List<Bag> bags){
        final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z"));
        int x = randomNumber(3, 5); // Generate random index of pebblepackage.Bag list

        if(bags.get(x).getPebblesInBag().size() == 0){ // If the black pebblepackage.Bag has no pebbles, refill from corresponding white pebblepackage.Bag

            // Empty white pebblepackage.Bag into Black bag
            List<Integer> oldWhite;
            oldWhite = bags.get(x-3).getPebblesInBag();
            bags.get(x).setPebblesInBag(oldWhite);
            bags.get(x-3).setPebblesInBag(new ArrayList<>());
        }
        player.setBlackBag(x);
        player.setWhiteBag((x-3));
    }

    // Method to generate random number between two constraints
    public static int randomNumber(Integer min, Integer max){
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;
        return randomNumber;
    }

    // Method to check if Player number input is valid
    public static boolean validInput(String input){ // Check validity of input
        boolean isValid = false;
        try {
            if (Integer.parseInt(input) > 0){
                isValid = true;
            }
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return isValid;
    }

    // Method to manage each Player's turn
    public static void takeTurn(Player player, List<Bag> bags){
        if(player.getPebbles().size() == 0){ // If they have no pebbles (i.e. it's their first turn)
            chooseBags(player, bags);
            addTenPebbles(player, bags);
        }
        else { // If it is not their first turn, discard a random pebble, choose a random black pebblepackage.Bag and add one random pebble to hand
            discardPebble(player, bags);
            chooseBags(player, bags);
            addPebble(player, bags);
        }
    }

    // Get the total weight of a Player's hand
    public static int checkTotalWeight(Player player){
        ArrayList<Integer> pebbles = player.getPebbles(); // Get list of player's pebbles
        int totalWeight = 0;
        for(int i = 0; i < pebbles.size(); i++) {
            totalWeight += pebbles.get(i); // Sum all pebbles
        }
        return(totalWeight);
    }

    // Method to add a Player's first 10 pebbles
    public static void addTenPebbles(Player player, List<Bag> bags){
        final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z"));
        for (int i = 0; i < 10; i++) {
            List<Integer> pebbles = Collections.synchronizedList(bags.get(player.getBlackBag()).getPebblesInBag()); // Get all pebbles in player's black bag

            // Randomly select pebble from black bag
            int randomNumber = randomNumber(0, (pebbles.size()-1)); // This is just the index of the list
            int pebbleValue = pebbles.get(randomNumber);

            // Remove pebble from black pebblepackage.Bag
            pebbles.remove(randomNumber);
            bags.get(player.getBlackBag()).setPebblesInBag(pebbles);

            // Add pebble to Player's hand
            ArrayList playerInv = player.getPebbles();
            playerInv.add(pebbleValue);
            player.setPebbles(playerInv);
        }

        // Write to player log file
        try {
            FileWriter myWriter = new FileWriter("player" + (player.getTurn() + 1) + "_output.txt", true);
            myWriter.write("player" + (player.getTurn() + 1) +" has drawn 10 pebbles from bag " + alphabet.get(player.getBlackBag()) + "\nplayer" + (player.getTurn()+1) + "hand is " +  player.getPebbles() + "\n");
            myWriter.close();
            // Error handling
        } catch (IOException e){
            System.out.println("An error occurred when writing to player files.");
            e.printStackTrace();
        }
    }

    // Method to add random pebble to Player's hand
    public static void addPebble(Player player, List<Bag> bags){
        final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z"));
        int pos = player.getTurn() + 1;

        ArrayList<Integer> pebbles = new ArrayList<Integer>(bags.get(player.getBlackBag()).getPebblesInBag());// Get all pebbles in player's black bag
        // Randomly select pebble from black bag
        int randNum = randomNumber(0, (pebbles.size() -1)); // This is just the index of the list
        int pebbleValue = pebbles.get(randNum);

        // Remove pebble from Black bag
        pebbles.remove(randNum);
        List<Integer> pebblesTemp = Collections.synchronizedList(pebbles);
        bags.get(player.getBlackBag()).setPebblesInBag(pebblesTemp);

        // Add pebble to Player's hand
        ArrayList<Integer> playerInv = player.getPebbles();
        playerInv.add(pebbleValue);
        player.setPebbles(playerInv);

        // Write to Player's log file
        try {
            FileWriter myWriter = new FileWriter("player" + pos + "_output.txt", true);
            myWriter.write("player" + pos +" has drawn a "+ pebbleValue + " from bag " + alphabet.get(player.getBlackBag()) + "\n");
            myWriter.write("player" + pos +" hand is "+ player.getPebbles() + "\n");
            myWriter.close();
            // Error handling
        } catch (IOException e){
            System.out.println("An error occurred when writing to player files.");
            e.printStackTrace();
        }
    }

    // Method to discard a pebble from Player's hand to white bag
    public static void discardPebble(Player player, List<Bag> bags){
        final List<String> alphabet = Collections.synchronizedList(Arrays.asList("A", "B", "C", "X", "Y", "Z"));
        int pos = player.getTurn() + 1;

        ArrayList<Integer> pebbles = player.getPebbles(); // Get all pebbles in player's black bag

        // Randomly select pebble from black bag
        int randomNumber = randomNumber(0, pebbles.size()-1); // This is just the index of the list
        int pebbleValue = pebbles.get(randomNumber);

        // Remove pebble from Player's hand
        pebbles.remove(randomNumber);
        player.setPebbles(pebbles);

        // Add pebble to white pebblepackage.Bag
        List<Integer> whiteBag;
        whiteBag = bags.get(player.getWhiteBag()).getPebblesInBag();
        whiteBag.add(pebbleValue);
        bags.get(player.getWhiteBag()).setPebblesInBag(whiteBag);

        // Write to Player's log file
        try {
            FileWriter myWriter = new FileWriter("player" + pos + "_output.txt", true);
            myWriter.write("player" + pos +" has discarded a "+ pebbleValue + " to bag " + alphabet.get(player.getWhiteBag()) + "\n");
            myWriter.write("player" + pos +" hand is "+ player.getPebbles() + "\n");
            myWriter.close();
            // Error handling
        } catch (IOException e){
            System.out.println("An error occurred when writing to player files.");
            e.printStackTrace();
        }
    }

    // Method to output winner when Player's hand weighs 100
    public static int declareWinner(Player player){
        int turn = player.getTurn();
        System.out.println("Player " + (turn + 1) +  " wins the game!\nThe winning hand is " + player.getPebbles());
        return turn;
    }
}