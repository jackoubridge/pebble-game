## ECM2414 – Paired Programming Project

**Design Choices**

In the very beginning of this project, we established two keys to our success; planning and
organisation. We spent our first session discussing how we would tackle this project, and by
using our prior programming knowledge and lecture notes, we created a plan that would
shape the foundation of our development. We learned that version control would be an
essential tool, so a GitHub repository was created to ensure ease of access and
organisation of our project files.

Using our written plan, we formed the basis of our project. We began by creating the
important classes and methods which established the framework for the rest of the code. In
the beginning, there were six classes: Main, player, bag, white bag, black bag and pebble.
We quickly realised that many of these classes would be useless, as, for example, white and
black bags behave identically therefore there is no need for them to be distinct classes.
Therefore we chose to opt for a solution with the three classes that are in the final project.

The code is structured so that the Player class is nested within the Main class. The Bag
class is another file entirely, and acts as an auxiliary class to store the pebbles. This
happens to be this way as the player class must be nested inside the Main where the game
is run, as otherwise threads would not be able to start running, whereas the bag class is only
aiding the process of storing pebbles therefore there is no need for it to be nested, as in fact,
it would only make it harder to read through the code and debug. We therefore opted to
make it a separate class with a single attribute list PebblesInBag that stores all the pebbles
in the specific bag, and the necessary constructors for this attribute to be accessible. We
chose to make this attribute a list of integers instead of an ArrayList, because we understood
that atomic variables are important when it comes to developing a project that utilises
threading. If we were to use an ArrayList, we could possibly encounter the scenario where
multiple threads are trying to access the list at the same time, causing misinterpretation of
the list.

A design choice we took to ensure the organisation and readability of our code was to make
almost all functionality of the game run within different methods. For example, we know that
for every game that is played, the user will always be asked exactly three times for a path to
a black bag. It may have been easier to check the validity of these inputs using a loop, but
we prioritised the quality of the code over the time it took to write it, and chose to write a
method that takes the path and returns whether or not it is a valid CSV file. There are
several other auxiliary methods that make the buildup of the game largely tidier and easier to
read through, for example takeTurn. This method is called when it’s a player’s turn to
draw/discard pebbles, and it follows a rather simple criteria: If the player has 0 pebbles in
their hand, this means that the game is starting, and therefore 10 pebbles are drawn from a
bag at random to make up the starting hand. If the player already has 10 pebbles, this
means that the method will only have to remove a pebble (the one being discarded by the
player, in this case being randomly picked amongst the hand) into the correct white bag and
add another pebble to the player’s hand which is randomly picked from the black bag
mapped onto the player.

Most of the code, however, is written inside the main class. This class contains every single
method and procedure that the game needs to run. The first thing that this class contains is
an input validator for the number of players that are playing the game. It makes sure the user
enters a non-zero positive integer to let the game start. Posterior to that, the location of 3
files containing comma separated integer values is demanded, each number representing
the weight of an individual pebble, and the whole file itself representing a bag. The program
makes sure that the user inputs the filenames or paths of every single one of the files


(though it accepts a repetition of files, hence all three inputs can be the same file) and then
sets the contents of each black bag as desired by the user.

Once all the inputs are taken, the code simply starts n separate threads,{ _n_ ∣ _n_ ∈N, n being
player’s input} that behave like the players in the game. After launch, a thread will create its
own output file, using its own player turn, where every action taken by them will be logged,
followed by their hand after doing so. Once this is done, each thread simply calls the
takeTurn method over and over again, and writes every change that occurs onto the output
file until one of the players achieves a hand with a combined weight of exactly 100. Once
this happens, the winner is announced through the declareWinner function, which stops the
code and outputs the winner and their winning hand.

As may be expected, the creation of the project was not smooth sailing. Several
performance issues were encountered and solved during the development of the program.
For a start, the input validation seemed to be a struggle at the beginning of it all. It was also
easily overcome, implementing a while loop that made it impossible to progress if a range of
criteria was not met. Further on, the creation and writing of files seemed to be erroring. Too
many files were being created and the contents of them were being replaced every time the
file writer had to update these files. The issue was quickly dealt with after analysing the
code. Too many files were being created at once due to a misjudgement of an integer
variable inside a for loop responsible for creating these output files. One addition wasn’t
being performed as it should and it caused some players to create one more file than they
should. In terms of writing to them, after doing some research we discovered that the file
writer could be invoked on append mode, which would append new data instead of replacing
the already written data. We swiftly corrected the issue too. In terms of setbacks, the largest
one we encountered was the implementation of threading. Originally, our code was
implemented to run the game procedurally inside the main class, without having the player
class nested inside it or calling threads to play the game. We made the mistake of believing
this was a good way to start as it seemed more achievable and the implementation of
threading seemed like it would be easier once all the methods for the game had already
been created. However, we discovered that changing the code into a threading-based
program involved a very revolutionary impact to the skeleton the code was built upon. The
task that we erroneously labeled as simple and achievable became a complicated exercise
in which we had to tediously read through the code and change picked out lines so that they
were thread-friendly, which ended up being marginally longer than making the code threaded
from the start would have been. The whole logic loop that the game was running under also
had to be taken into the thread object, and edited so that each player thread could run it.
This meant that the idea the game was running by had to be changed, as it was originally
infinitely running under a while loop due to all the players being local objects, which then had
to be changed as that became obsolete when threading was implemented. Thankfully, most
of our methods were able to be called from inside the thread without any issues as they were
designed to be called by individual players.

After the performance issues specified above, no further complications stood in the way. We
did find that the code took a substantially long amount of time to proclaim a winner
depending on the given input, but we figured that’s because some data sets that we tested
the code with only had 1 possible winning hand, and the probability of randomly collecting
those 10 values was minuscule. However, when the input files were rather generous with the
winning probabilities, the winner was successfully announced seconds after the program
was started.


**Implementation of Unit Tests**

Throughout the production of our code, we tested our methods to ensure that they were
behaving as expected under any circumstance. Since we had created numerous methods to
aid a safer and neater run of the code, the number of functions to be tested was not small.
After installing JUnit, we created a new Java class to carry out all the tests.

**_chooseBags()_**

The choose bags method picks a random black bag from the list of all black bags. This will
be the bag that this specific player draws from in the following turn. Summoned every turn at
the start.

This function was tested by creating a list of bags and a player object, and making the player
pick bags. The assert methods check that the bags have been appointed accordingly, and
prove that this method works as intended and is reliability repeatable.

**_randomNumber()_**

The random number function generates a random integer between 2 given parameters, a
minimum and a maximum. This function is used in the code as an auxiliary method to pick a
random pebble from a bag and a random black bag from all black bags.

To test randomNumber(), two integer variables min and max were created, as well as an
integer myNum which would be randomly generated between the two boundaries. The
assert checks myNum is bigger than min and smaller than max.


**_validInput()_**

The validInput function validates the user’s input for the number of players in a pebble game.
It makes sure that the input is a non-zero positive integer, and returns true if the conditions
are met.

This test selects a group of String variables and makes sure the function validInput returns
the correct boolean value based on whether they would be acceptable inputs when choosing
the number of players.

**_checkTotalWeight()_**

This method checks the total weight of the pebbles in a player’s hand. It loops over all
pebbles stored as an attribute of the Player object and returns the sum of the pebbles’
values.

For this test, an ArrayList of pebbles was created and then the pebbles transferred to a
newly created player’s hand. The assert checks the combined weight of these pebbles is
correct.

**_addTenPebbles()_**

addTenPebbles is an auxiliary method invoked by takeTurn on the first turn of a pebble
game. As the name points out, it draws 10 pebbles from a black bag and gives them to the
player.

This function was tested by simulating a bag with 10 pebbles and moving them over to the
player’s hand. To prove it worked, an assert statement checks that the player has 10 pebbles
in their hand and the bag is empty.


**_addPebble()_**

Similarly to addTenPebbles, addPebble draws one pebble from a random black bag, acting
also as an auxiliary method of takeTurn. It is summoned every turn except for the Player’s
first turn.

addPebble() was tested by implementing a bag with a number of pebbles inside it, one of
which would then be moved over to the player’s hand. By checking the player’s hand is not
empty and adding the number of pebbles in that hand to the remainders in the bag we can
see all pebbles are still in the game therefore one was added to the hand.

**_discardPebble()_**

This method discards a random pebble from the player’s hand into the corresponding white
bag. Invoked every time the player has to discard a pebble.

Similarly to before, in this scenario the player object is not created with an empty hand. At
the start, one of the pebbles is discarded into a bag, and by checking that the bag gained a
new pebble and the player lost one, it proved that the function works correctly.

**_declareWinner()_**

This method declares the winner of the pebble game and kills the running threads. This only
happens when a player manages to collect a hand with a combined pebble weight of exactly
100.

In order to test this method, a player class is created (the winner) and an integer expected
value of 1 is set. The player is then declared the winner and the assert checks for it to
happen correctly.