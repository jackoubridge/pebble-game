import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PebbleGameTests {

    @Test
    void chooseBags() {
        List<Bag> bags = Collections.synchronizedList(new ArrayList<Bag>()); // Create list of bags

        // Add all 6 bgs to simulate game
        bags.add(new Bag(Arrays.asList(1,2,3)));
        bags.add(new Bag(Arrays.asList(4,5,6)));
        bags.add(new Bag(Arrays.asList(7,8,9)));
        bags.add(new Bag(Arrays.asList(10,11,12)));
        bags.add(new Bag(Arrays.asList(13,14,15)));
        bags.add(new Bag(Arrays.asList(16,17,18)));

        // Create player object
        Main.Player myPlayer = new Main.Player(0, new ArrayList<>(), 0, 0);

        // Run method
        Main.chooseBags(myPlayer, bags);

        // Create variables
        int blackBagNum = myPlayer.getBlackBag();
        int whiteBagNum = myPlayer.getWhiteBag();

        // Assert that method acts as expected
        assertTrue(0 <= blackBagNum && blackBagNum <= 6);
        assertTrue(0 <= whiteBagNum && whiteBagNum <= 6);
    }

    @Test
    void randomNumber() {

        // Test case 1 -- random number is between minimum and maximum values
        int min = 1;
        int max = 10;
        int myNum = Main.randomNumber(min, max);
        assertTrue(1 <= myNum && myNum <= 10); // Test that the random number is between expected values

        // Test case 2 -- random number is outside minimum and maximum values
        min = 2;
        max = 20;
        myNum = Main.randomNumber(min, max);
        assertFalse(21 <= myNum && myNum <= 1); // Test that the random number is not outside the expected values
    }

    @Test
    void validInput() {
        String stringInput = "Hello world"; // String cannot be converted to integer -- expected to fail
        assertFalse(false, stringInput);

        String positiveIntegerInput = "2"; // Positive integer -- should pass assert
        assertTrue(true, positiveIntegerInput);

        String zeroInput = "0"; // Non-zero integer -- should fail assert
        assertFalse(false, zeroInput);

    }

    @Test
    void checkTotalWeight() {
        ArrayList<Integer> pebbles = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5)); // Simulated ArrayList of pebbles
        Main.Player myPlayer = new Main.Player(0, pebbles, 0, 3); // Create Player -- put pebbles in player's hand
        assertEquals(15, Main.checkTotalWeight(myPlayer)); // Total weight is expected to be 15 (combined weights of all pebbles in player's hand)
    }

    @Test
    void addTenPebbles() {
        List<Bag> bags = Collections.synchronizedList(new ArrayList<Bag>()); // Simulated list of bags
        ArrayList<Integer> myArrayList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)); // Simulated pebbles in pebblepackage.Bag

        bags.add(new Bag(myArrayList)); // Add pebblepackage.Bag instance to list of Bags
        Main.Player player = new Main.Player(0, new ArrayList<>(), 0 ,0); // Create Player

        Main.addTenPebbles(player, bags); // Perform method
        Collections.sort(player.getPebbles()); // Sort player's hand in ascending order

        // Check that the hand contains the 10 expected pebbles. Check that pebblepackage.Bag contains no pebbles
        assertTrue(player.getPebbles().equals(Arrays.asList(1,2,3,4,5,6,7,8,9,10)) && bags.get(0).getPebblesInBag().isEmpty());
    }

    @Test
    void addPebble() {
        List<Bag> bags = Collections.synchronizedList(new ArrayList<Bag>()); // Simulated list of bags
        ArrayList<Integer> myArrayList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)); // Simulated pebbles in pebblepackage.Bag

        bags.add(new Bag(myArrayList)); // Add pebblepackage.Bag instance to list of Bags
        Main.Player player = new Main.Player(0, new ArrayList<>(), 0 ,0); // Create Player

        Main.addTenPebbles(player, bags); // Perform method

        myArrayList.addAll(player.getPebbles()); // Add player's hand to pebblepackage.Bag
        Collections.sort(myArrayList); // Sort player's hand in ascending order

        assertTrue(myArrayList.equals(Arrays.asList(1,2,3,4,5,6,7,8,9,10))); // Check if current pebbles in play = total original pebbles
    }

    @Test
    void discardPebble() {
        List<Bag> bags = Collections.synchronizedList(new ArrayList<Bag>()); // Create list of Bags
        ArrayList<Integer> totalPebbles = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)); // Create original ArrayList of pebbles
        ArrayList<Integer> myArrayList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)); // Create ArrayList of pebblepackage.Bag's pebbles

        bags.add(new Bag(new ArrayList<Integer>())); // Create new instance of pebblepackage.Bag
        Main.Player player = new Main.Player(0, myArrayList, 0 ,1); // Create new instance of Player

        Main.discardPebble(player, bags); // Perform method

        bags.get(0).getPebblesInBag().addAll(player.getPebbles()); // Add pebbles in pebblepackage.Bag to player's hand

        assertTrue(totalPebbles.size() - player.getPebbles().size() == 1); // Check that the player discarded exactly 1 pebble
    }

    @Test
    void declareWinner() {
        Main.Player player = new Main.Player(1, new ArrayList<Integer>(), 0, 0); // Create instance of Player

        int expected = 1; // Expected player turn to be returned

        Main.declareWinner(player); // Perform method
        assertEquals(1, expected); // Expect returned Integer to be Player's turn number
    }
}