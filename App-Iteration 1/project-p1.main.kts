// -----------------------------------------------------------------
// Project: Part 1, Summary
// -----------------------------------------------------------------

// You are going to design an application to allow a user to
// self-study using flash cards. In this part of the project,
// a user will...

// 1. Be prompted to choose from a menu of available flash
//    card decks. The menu and prompt will be repeated until
//    a valid selection is made.
//
// 2. Proceed through each card in the selected deck,
//    one-by-one. For each card, the front is displayed,
//    and the user is allowed time to reflect. After they
//    press return/enter, the back is displayed and the user
//    is asked if they got the correct answer.
//
// 3. Once the deck is exhausted, the program outputs the
//    number of self-reported correct answers and ends.
//

// Of course, we'll design this program step-by-step, AND
// you've already done pieces of this in homework!!
// (Note: you are welcome to leverage your prior work and/or
// code found in the sample solutions & lecture notes.)
//

// Lastly, here are a few overall project requirements...
// - Since mutation hasn't been covered in class, your design is
//   NOT allowed to make use of mutable variables, including
//   mutable lists.
// - As included in the instructions, all interactive parts of
//   this program MUST make effective use of the reactConsole
//   framework.
// - Staying consistent with our Style Guide...
//   * All functions must have:
//     a) a preceding comment specifying what it does
//     b) an associated @EnabledTest function with sufficient
//        tests using testSame
//   * All data must have:
//     a) a preceding comment specifying what it represents
//     b) associated representative examples
// - You will be evaluated on a number of criteria, including...
//   * Adherence to instructions and the Style Guide.
//   * Correctly producing the functionality of the program.
//   * Design decisions that include choice of tests, appropriate
//     application of list abstractions, and task/type-driven
//     decomposition of functions.
//

// -----------------------------------------------------------------
// Data design
// (Hint: see Homework 3, Problem 2)
// -----------------------------------------------------------------

// TODO 1/2: Design the data type FlashCard to represent a single
//           flash card. You should be able to represent the text
//           prompt on the front of the card as well as the text
//           answer on the back. Include at least 3 example cards
//           (which will come in handy later for tests).
//

// libraries imported from the khoury library
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.fileExists
import khoury.fileReadAsList
import khoury.linesToString
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame


// new variable flashCard that contains two variables, each representing a side of a flashcard, one being the front with the question, and one being the back with the answer
data class FlashCard(val front: String, val back: String)

// Example flashCards for data class FlashCards
val flashCard1 = FlashCard("What is the capital of England?", "London")
val flashCard2 = FlashCard("What is the capital of USA?", "Washington D.C")
val flashCard3 = FlashCard("What is the capital of China?", "Beijing")
val flashCard4 = FlashCard("What is the capital of Australia?", "Canberra")
val flashCard5 = FlashCard("What is the capital of India?", "Dehli")

// TODO 2/2: Design the data type Deck to represent a deck of
//           flash cards. The deck should have a name, as well
//           as a Kotlin list of flash cards.
//
//           Include at least 2 example decks based upon the
//           card examples above.
//

// new variable Deck that contains a deck name as well as a list that contains variables with the flashCard data type only
data class Deck(val deckName: String, val flashCardList: List<FlashCard>)

// Example Decks for data class Dekc
val deck1 = Deck("Capitals 1", listOf(flashCard1, flashCard2, flashCard3))
val deck2 = Deck("Capitals 2", listOf(flashCard4, flashCard5, flashCard1))
val deck3 = Deck("Empty deck", listOf())
val deck4 = Deck("Capitals 5", listOf(flashCard3, flashCard1, flashCard4))
// -----------------------------------------------------------------
// Generating flash cards
// -----------------------------------------------------------------

// One benefit of digital flash cards is that sometimes we can
// use code to produce cards that match a known pattern without
// having to write all the fronts/backs by hand!
//

// TODO 1/1: Design the function perfectSquares that takes a
//           count (assumed to be positive) and produces the
//           list of flash cards that tests that number of the
//           first squares.
//
//           For example, the first three perfect squares...
//
//            1. front (1^2 = ?), back (1)
//            2. front (2^2 = ?), back (4)
//            3. front (3^2 = ?), back (9)
//
//           have been supplied as named values.
//
//           Hint: you might consider combining your
//                 kthPerfectSquare function from Homework 1
//                 with the list constructor in Homework 3.
//

// Example front and backs of perfect square cards
val square1Front = "1^2 = ?"
val square2Front = "2^2 = ?"
val square3Front = "3^2 = ?"
val square4Front = "4^2 = ?"
val square5Front = "5^2 = ?"

val square1Back = "1"
val square2Back = "4"
val square3Back = "9"
val square4Back = "16"
val square5Back = "25"

// function that takes in a count, and returns the perfect squares uptil that count in a arrangement of the flashCard datatype previously created
fun perfectSquares(count: Int): List<FlashCard> {
    // function that squares any number provided and then returns it
    fun kthPerfectSquare(num2: Int): Int {
        val perfectSquareVal: Int = num2 * num2
        return perfectSquareVal
    }

    // function that creates a new flashCard in the perfect squares deck based on the current index of the card, that is looped through count in the list creator function below
    fun flashCardCreation(index: Int): FlashCard {
        val newCard: FlashCard = FlashCard("${index + 1}^2 = ?", "${kthPerfectSquare(index + 1)}")
        return newCard
    }

    // variable that creates a list of FlashCards basde on the count provided, and calling the flashCardList function that creates flashCards
    val flashCardList =
        List<FlashCard>(
            count,
            ::flashCardCreation,
        )
    return flashCardList
}

// function to test function: perfectSquares
@EnabledTest
fun testPerfectSquares() {
    testSame(
        perfectSquares(3),
        listOf(FlashCard(square1Front, square1Back), FlashCard(square2Front, square2Back), FlashCard(square3Front, square3Back)),
        "3",
    )

    testSame(
        perfectSquares(5),
        listOf(
            FlashCard(square1Front, square1Back),
            FlashCard(square2Front, square2Back),
            FlashCard(square3Front, square3Back),
            FlashCard(square4Front, square4Back),
            FlashCard(square5Front, square5Back),
        ),
        "5",
    )
}

// -----------------------------------------------------------------
// Files of cards
// -----------------------------------------------------------------

// Consider a simple format for storing flash cards in a file:
// each card is a line in the file, where the front comes first,
// separated by a "pipe" character ('|'), followed by the text
// on the back of the card.
//

// TODO 1/3: Design the function cardToString that takes a flash
//           card as input and produces a string according to the
//           specification above ("front|back"). Make sure to
//           test all your card examples!
//

// function that takes in a flashcard and calls inbuilt function in order to create a string out of the card
fun cardToString(flash: FlashCard): String {
    val charSep = "|"
    val card = flash.front + charSep + flash.back
    return card
}

// function to test function: cardToString
@EnabledTest
fun testCardToString() {
    testSame(
        cardToString(FlashCard("front 1", "back 1")),
        fileReadAsList("example.txt").first(),
        "first line",
    )
    testSame(
        cardToString(FlashCard("front 2", "back 2")),
        fileReadAsList("example.txt").last(),
        "last line",
    )

    testSame(
        cardToString(FlashCard("What is the capital of South Korea?", "Seoul")),
        fileReadAsList("capitals3.txt").first(),
        "first line",
    )
}

// TODO 2/3: Design the function stringToCard that takes a string,
//           assumed to be in the format described above, and
//           produces the corresponding flash card.
//
//           Hints:
//           - look back to how we extracted data from CSV
//             (comma-separated value) files (such as in
//             Homework 3)!
//           - a great way to test: for each of your card
//             examples, pass them through the function in TODO
//             1 to convert them to a string; then, pass that
//             result to this function... you *should* get your
//             original flash card back :)
//

// function that takes in a string and splits it using inbuilt function after recognizing pattern and using list indexing to assign it within the flashCard variable to create a new variable
fun stringToCard(stringForCard: String): FlashCard {
    val stringAsList: List<String> = stringForCard.split("|")
    val newFlashCard = FlashCard(stringAsList[0], stringAsList[1])
    return newFlashCard
}

// function to test function: stringToCard
@EnabledTest
fun testStringToCard() {
    testSame(
        stringToCard(fileReadAsList("example.txt").first()),
        FlashCard("front 1", "back 1"),
        "test1",
    )

    testSame(
        stringToCard(fileReadAsList("example.txt").last()),
        FlashCard("front 2", "back 2"),
        "test 2",
    )

    testSame(
        stringToCard(fileReadAsList("capitals3.txt").first()),
        FlashCard("What is the capital of South Korea?", "Seoul"),
        "test 3",
    )
}

// TODO 3/3: Design the function readCardsFile that takes a path
//           to a file and produces the corresponding list of
//           flash cards found in the file.
//
//           If the file does not exist, return an empty list.
//           Otherwise, you can assume that every line is
//           formatted in the string format we just worked with.
//
//           Hint:
//           - Think about how HW3-P1 effectively used an
//             abstraction to process all the lines in a
//             file assuming a known pattern.
//           - We've provided an "example.txt" file that you can
//             use for testing if you'd like. Also make sure to
//             test your function when the supplied file does not
//             exist!
//

// function thta reads a file path provided, and uses list abstraction methods to convert all list items into flashCards and returns a final list containing only flashCards
fun readCardsFile(file: String): List<FlashCard> {
    val flashCardsFound: List<FlashCard>
    val incaseEmpty: List<FlashCard> = listOf()
    if (fileExists(file) == true) {
        val fileList: List<String> = fileReadAsList(file)
        val fileSize: Int = fileList.size

        flashCardsFound = fileList.map(::stringToCard)
        return flashCardsFound
    } else {
        return incaseEmpty
    }
}

// function to test function: readCardsFile
@EnabledTest
fun testReadCardsFile() {
    testSame(
        readCardsFile("example.txt"),
        listOf(FlashCard("front 1", "back 1"), FlashCard("front 2", "back 2")),
        "test file provided",
    )

    testSame(
        readCardsFile("capitals3.txt"),
        listOf(
            FlashCard("What is the capital of South Korea?", "Seoul"),
            FlashCard("What is the capital of Russia?", "Moscow"),
            FlashCard("What is the capital of USA?", "Washington D.C"),
        ),
        "test file created and provided provided",
    )

    testSame(
        readCardsFile("FILE-DOES-NOT-EXIST.txt"),
        listOf(),
        "No file found test",
    )
}

// -----------------------------------------------------------------
// Processing a self-report
// (Hint: see Homework 2)
// -----------------------------------------------------------------

// In our program, we will ask for a self-report as to whether
// the user got the correct answer for a card, SO...

// TODO 1/1: Finish designing the function isPositive() that
//           determines if the supplied string starts with
//           the letter "y" (either upper or lowercase).
//
//           You've been supplied with a number of tests -- make
//           sure you understand what they are doing!
//

// function to check if the first character of the provided input starts with the letter "Y"
fun isPositive(suppliedString: String): Boolean {
    return suppliedString[0].uppercase() == "Y"
}

// function to test function: isPositive
@EnabledTest
fun testIsPositive() {
    fun helpTest(
        str: String,
        expected: Boolean,
    ) {
        testSame(isPositive(str), expected, str)
    }

    helpTest("yes", true)
    helpTest("Yes", true)
    helpTest("YES", true)
    helpTest("yup", true)

    helpTest("nope", false)
    helpTest("NO", false)
    helpTest("nah", false)
    helpTest("not a chance", false)

    // should pass,
    // despite doing the wrong thing
    helpTest("indeed", false)
}

// -----------------------------------------------------------------
// Choosing a deck from a menu
// -----------------------------------------------------------------

// Now let's work on providing a menu of decks from which a user
// can choose what they want to study.

// TODO 1/2: Finish design the function choicesToText() that takes
//           a list of strings (assumed to be non-empty) and
//           produces the textual representation of a menu of
//           those options.
//
//           For example, given...
//
//           ["a", "b", "c"]
//
//           The menu would be...
//
//           "1. a
//            2. b
//            3. c
//
//            Enter your choice:"
//
//            As you have probably guessed, this will be a key
//            piece of our rendering function :)
//
//            Hints:
//            - Think back to Homework 3 when we used a list
//              constructor to generate list elements based
//              upon an index.
//            - If you can produce a list of strings, the
//              linesToString() function in the Khoury library
//              will bring them together into a single string.
//            - Make sure to understand the supplied tests!
//

val promptMenu = "Enter your choice:"

// function to convert a list of options into a presentable format using khoury library functions, indexing and string templates
fun choicesToText(optionsList: List<String>): String {
    // function that decides what to return depending on the index count
    fun updateListItem(index: Int): String {
        if (index in optionsList.indices) {
            return "${index + 1}. ${optionsList[index]}"
        } else if (index == optionsList.size) {
            return ""
        } else {
            return promptMenu
        }
    }

    // creates a list of string by passing in updateListItem function and the length of the desired list
    val result = List<String>(optionsList.size + 2, ::updateListItem)
    return linesToString(result)
}

// function to test function: choicesToText
@EnabledTest
fun testChoicesToText() {
    val optA = "apple"
    val optB = "banana"
    val optC = "carrot"

    testSame(
        choicesToText(listOf(optA)),
        linesToString(
            "1. $optA",
            "",
            promptMenu,
        ),
        "one",
    )

    testSame(
        choicesToText(listOf(optA, optB, optC)),
        linesToString(
            "1. $optA",
            "2. $optB",
            "3. $optC",
            "",
            promptMenu,
        ),
        "three",
    )
}

// TODO 2/2: Finish designing the function chooseOption() that takes
//           a list of decks, produces a corresponding numbered
//           menu (1-# of decks, each showing its name), and
//           returns the deck corresponding to the number entered.
//           (Of course, keep displaying the menu until a valid
//           number is entered.)
//
//           Hints:
//            - Review the "Valid Number Example" of reactConsole
//              as one example of how to validate input. In this
//              case, however, since we know that we have a valid
//              range of integers, we can simplify the state
//              representation significantly :)
//            - To help you get started, the chooseOption() function
//              has been written, but you must complete the helper
//              functions; look to the comments below for guidance.
//              You can then play "signature detective" to figure
//              out the parameters/return type of the functions you
//              need to write :)
//            - Lastly, as always, don't forget to sufficiently
//              test all the functions you write in this problem!

// a function to allow the user to interactively select
// a deck from the supplied non-empty list of decks

// function that gets a name of a deck based on the deck provided
fun getDeckName(deck: Deck): String {
    return deck.deckName
}

// function to test function: getDeckName
@EnabledTest
fun testGetDeckName() {
    testSame(
        getDeckName(deck1),
        "Capitals 1",
        "Test 1",
    )

    testSame(
        getDeckName(deck2),
        "Capitals 2",
        "Test 2",
    )

    testSame(
        getDeckName(deck3),
        "Empty deck",
        "Test 3",
    )
}

// function that takes in a input and IntRange of indices of a deck, and checks if the input corresponds to any of the deck Indices
fun keepIfValid(
    input: String,
    deckIndices: IntRange,
): Int {
    if ((input.toInt() - 1) in deckIndices) {
        return input.toInt() - 1
    } else {
        return -1
    }
}

// function to test function: keepIfValid
@EnabledTest
fun testKeepIfValid() {
    testSame(
        keepIfValid("1", 0..2),
        0,
        "Test 1",
    )

    testSame(
        keepIfValid("2", 0..2),
        1,
        "Test 2",
    )

    testSame(
        keepIfValid("3", 0..2),
        2,
        "Test 3",
    )
    testSame(
        keepIfValid("5", 0..2),
        -1,
        "Invalid Input",
    )
}

// function that returns a message back to the user informing them what deck they have chosen
fun choiceAnnouncement(deckName: String): String {
    return "You chose: $deckName"
}

// function to test function: choiceAnnouncement
@EnabledTest
fun testChoiCeAnnouncement() {
    testSame(
        choiceAnnouncement("Capitals 1"),
        "You chose: Capitals 1",
        "Test 1",
    )

    testSame(
        choiceAnnouncement("Capitals 2"),
        "You chose: Capitals 2",
        "Test 2",
    )

    testSame(
        choiceAnnouncement("Perfect Squares"),
        "You chose: Perfect Squares",
        "Test 3",
    )
}

// function that continuously returns the deck options until a valid input has been provided by the user using by using react Console
fun chooseOption(decks: List<Deck>): Deck {
    // since the event handlers will need some info about
    // the supplied decks, the functions inside
    // chooseOption() provide info about them while the
    // parameter is in scope

    // TODO: Above chooseOption(), design the function
    //       getDeckName(), which returns the name of
    //       a supplied deck.

    // function that uses list abstraction method to get all decks names from the list: Decks, and using choiceToText function to return it in a presentable format
    fun renderDeckOptions(state: Int): String {
        return choicesToText(decks.map(::getDeckName))
    }

    // // TODO: Above chooseOption(), design the function
    // //       keepIfValid(), that takes the typed input
    // //       as a string, as well as the valid
    // //       indices of the decks; note that the list indices
    // //       will be in the range [0, size), whereas the
    // //       user will see and work with [1, size].
    // //
    // //       If the user did not type a valid integer,
    // //       or not one in [1, size], return -1; otherwise
    // //       return the string converted to an integer, but
    // //       subtract 1, which makes it a valid list index.

    // function that takes in a state and input and passed it through the keepIfValid function and returns it
    fun transitionOptionChoice(
        ignoredState: Int,
        kbInput: String,
    ): Int {
        return keepIfValid(kbInput, decks.indices)
    }

    // TODO: nothing, but understand this :)
    fun validChoiceEntered(state: Int): Boolean {
        return state in decks.indices
    }

    // TODO: Above chooseOption(), design the function
    //       choiceAnnouncement() that takes the selected
    //       deck name and returns an announcement that
    //       makes you happy. For a simple example, given
    //       "fundies" as the chosen deck name, you might
    //       return "you chose: fundies"

    // function that calls the choiceAnnouncement function to get a string to be returned regarding what deck has been chosen
    fun renderChoice(state: Int): String {
        return choiceAnnouncement(getDeckName(decks[state]))
    }

    // react console being called that calls all helper functions to create an event-driven framework
    return decks[
        reactConsole(
            initialState = -1,
            stateToText = ::renderDeckOptions,
            nextState = ::transitionOptionChoice,
            isTerminalState = ::validChoiceEntered,
            terminalStateToText = ::renderChoice,
        ),
    ]
}

// function to test function: chooseOption
@EnabledTest
fun testChooseOption() {
    // makes a captureResults-friendly function
    fun helpTest(decks: List<Deck>): () -> Deck {
        fun chooseMyOption(): Deck {
            return chooseOption(decks)
        }
        return ::chooseMyOption
    }

    testSame(
        captureResults(
            helpTest(listOf(deck1, deck2, deck3)),
            "1",
        ),
        CapturedResult(
            deck1,
            "1. Capitals 1",
            "2. Capitals 2",
            "3. Empty deck",
            "",
            promptMenu,
            "You chose: Capitals 1",
        ),
        "Test 1",
    )

    testSame(
        captureResults(
            helpTest(listOf(deck1, deck2, deck3)),
            "4",
            "5",
            "2",
        ),
        CapturedResult(
            deck2,
            "1. Capitals 1",
            "2. Capitals 2",
            "3. Empty deck",
            "",
            promptMenu,
            "1. Capitals 1",
            "2. Capitals 2",
            "3. Empty deck",
            "",
            promptMenu,
            "1. Capitals 1",
            "2. Capitals 2",
            "3. Empty deck",
            "",
            promptMenu,
            "You chose: Capitals 2",
        ),
        "Test 2",
    )
}
// -----------------------------------------------------------------
// Studying a deck
// -----------------------------------------------------------------

// Now let's design a program to allow a user to study a
// supplied deck of flash cards.

// TODO 1/2: Design the data type StudyState to keep track of...
//           - which card you are currently studying in the deck
//           - whether you are looking at the front or back
//           - how many correct answers have been self-reported
//             thus far
//
//           Create sufficient examples so that you convince
//           yourself that you can represent any situation that
//           might arise when studying a deck.
//
//           Hints:
//           - Look back to the reactConsole problems in HW2 and
//             HW3. The former involved keeping track of a count
//             of loops (similar to the count of correct answers),
//             and the latter involved options for keeping track
//             of where you are in a list with reactConsole.
//

// data class studyDeck that stores list of flashcards, as well as weather front is being currently shwon or the back, and finally the number of correct answered so far
data class StudyDeck(val currentFlash: List<FlashCard>, val frontCard: Boolean, val correct: Int)

// example study Decks for data class StudyDeck
val exampleStudyDeck1 = StudyDeck(deck1.flashCardList, true, 0)
val exampleStudyDeck2 = StudyDeck(deck2.flashCardList, true, 0)
val exampleStudyDeck3 = StudyDeck(deck1.flashCardList, false, 0)
val exampleStudyDeck4 = StudyDeck(deck2.flashCardList, false, 0)
val exampleStudyDeck5 = StudyDeck(deck3.flashCardList, false, 0)

// TODO 2/2: Now, using reactConsole, design the function studyDeck()
//           that for each card in a supplied deck, allows the
//           user to...
//
//           1. see the front (pause and think)
//           2. see the back
//           3. respond as to whether they got the answer
//
//           At the end, the user is told how many they self-
//           reported as correct (and this number is returned).
//
//           You have been supplied some prompts for steps #1
//           and #2 - feel free to change them if you'd like :)
//
//           Suggestions...
//           - Review the reactConsole videos/examples
//           - Start with studyDeck():
//             * write some tests to convince yourself you know
//               what your program is supposed to do!
//             * figure out how you'll create the initial state
//             * give names to the handlers you'll need
//             * how will you return the number correct?
//             * now, comment-out this function, so that you can
//               design/test the handlers without interference :)
//           - For each handler...
//             * Play signature detective: based upon how it's
//               being used with reactConsole, what data will it
//               be given and what does it produce?
//             * Write some tests to convince yourself you know
//               its job.
//             * Write the code and don't move on until your tests
//               pass.
//            - Suggested ordering...
//              1. Am I done studying yet?
//              2. Rendering
//                 - It's a bit simpler to have a separate
//                   function for the terminal state.
//                 - The linesToString() function is your friend to
//                   combine the card with the prompts.
//                 - Think about good decomposition when making
//                   the decision about front vs back content.
//              3. Transition
//                 - Start with the two main situations
//                   you'll find yourself in...
//                   > front->back
//                   > back->front
//                 - Then let a helper figure out how to handle
//                   the details of self-report
//
//            You've got this :-)
//

val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

// function that calls reactConsole to loop through list of Flashcards in Deck
fun studyDeck(deck: Deck): Int {
    return reactConsole(
        initialState = StudyDeck(deck.flashCardList, true, 0),
        stateToText = ::showFace,
        nextState = ::determineNext,
        isTerminalState = ::isDone,
        terminalStateToText = ::returnText,
    ).correct
}

// function to test function: studyDeck
@EnabledTest
fun testStudyDeck() {
    // makes a captureResults-friendly function :
    fun helpTest(deck: Deck): () -> Int {
        fun showStudyDeck(): Int {
            return studyDeck(deck)
        }
        return ::showStudyDeck
    }

    testSame(
        captureResults(
            helpTest(deck1),
            "",
            "Yes",
            "",
            "Yes",
            "",
            "Yes",
        ),
        CapturedResult(
            3,
            "What is the capital of England? ",
            "$studyThink",
            "London ",
            "$studyCheck",
            "What is the capital of USA? ",
            "$studyThink",
            "Washington D.C ",
            "$studyCheck",
            "What is the capital of China? ",
            "$studyThink",
            "Beijing ",
            "$studyCheck",
            "",
            "3 answered correctly",
        ),
        "Test 1",
    )

    testSame(
        captureResults(
            helpTest(deck2),
            "",
            "Yes",
            "",
            "No",
            "",
            "Yes",
        ),
        CapturedResult(
            2,
            "What is the capital of Australia? ",
            "$studyThink",
            "Canberra ",
            "$studyCheck",
            "What is the capital of India? ",
            "$studyThink",
            "Dehli ",
            "$studyCheck",
            "What is the capital of England? ",
            "$studyThink",
            "London ",
            "$studyCheck",
            "",
            "2 answered correctly",
        ),
        "Test 2",
    )
}

// function to show the question or answer whilst also displaying default prompts
fun showFace(studyState: StudyDeck): String {
    return when (studyState.frontCard) {
        true -> "${accessFront(studyState.currentFlash)} \n$studyThink"
        false -> "${accessBack(studyState.currentFlash)} \n$studyCheck"
    }
}

// function to test function: showFace
@EnabledTest
fun testShowFace() {
    testSame(
        showFace(exampleStudyDeck1),
        "What is the capital of England? \n$studyThink",
        "Test 1",
    )

    testSame(
        showFace(exampleStudyDeck2),
        "What is the capital of Australia? \n$studyThink",
        "Test 2",
    )

    testSame(
        showFace(exampleStudyDeck3),
        "London \n$studyCheck",
        "Test 3",
    )

    testSame(
        showFace(exampleStudyDeck4),
        "Canberra \n$studyCheck",
        "Test 4",
    )
}

// function that takes in user input and study deck and compares it to answers set in
fun determineNext(
    studyState: StudyDeck,
    userInput: String,
): StudyDeck {
    return when (studyState.frontCard) {
        true -> StudyDeck(studyState.currentFlash, false, studyState.correct)

        false ->
            if (isPositive(userInput) == true) {
                StudyDeck(dropFlashCard(studyState.currentFlash), true, studyState.correct + 1)
            } else {
                StudyDeck(dropFlashCard(studyState.currentFlash), true, studyState.correct)
            }
    }
}

// function to test function: determineNext
@EnabledTest
fun testDetermineNext() {
    testSame(
        determineNext(exampleStudyDeck1, "Yes"),
        StudyDeck(exampleStudyDeck1.currentFlash, false, exampleStudyDeck1.correct),
        "Test 1",
    )

    testSame(
        determineNext(exampleStudyDeck2, "no"),
        StudyDeck(exampleStudyDeck2.currentFlash, false, exampleStudyDeck2.correct),
        "Test 2",
    )

    testSame(
        determineNext(exampleStudyDeck3, "yes"),
        StudyDeck(dropFlashCard(exampleStudyDeck3.currentFlash), true, exampleStudyDeck3.correct + 1),
        "Test 3",
    )

    testSame(
        determineNext(exampleStudyDeck4, "no"),
        StudyDeck(dropFlashCard(exampleStudyDeck4.currentFlash), true, exampleStudyDeck4.correct),
        "Test 4",
    )
}

// function to check if we have reached end of the deck
fun isDone(studyState: StudyDeck): Boolean {
    if (studyState.currentFlash.isEmpty()) {
        return true
    } else {
        return false
    }
}

// function to test function: isDone
@EnabledTest
fun testIsDone() {
    testSame(
        isDone(exampleStudyDeck1),
        false,
        "Test 1",
    )

    testSame(
        isDone(exampleStudyDeck5),
        true,
        "Test 2",
    )
}

// function to drop first card in the flashCard List
fun dropFlashCard(flashCardList: List<FlashCard>): List<FlashCard> {
    return flashCardList.drop(1)
}

// function to test function: dropFlashCard
@EnabledTest
fun testDropFlashCard() {
    testSame(
        dropFlashCard(exampleStudyDeck1.currentFlash).size,
        exampleStudyDeck1.currentFlash.size - 1,
        "Test 1",
    )

    testSame(
        dropFlashCard(exampleStudyDeck2.currentFlash).size,
        exampleStudyDeck2.currentFlash.size - 1,
        "Test 2",
    )
}

// function to return number of correct and incorrect answers made whilst answering the entire deck of flashcard questions
fun returnText(studyState: StudyDeck): String {
    return "\n${studyState.correct} answered correctly"
}

// function to test function: returnText
@EnabledTest
fun testReturnText() {
    testSame(
        returnText(exampleStudyDeck1),
        "\n0 answered correctly",
        "Test 1",
    )

    testSame(
        returnText(exampleStudyDeck2),
        "\n0 answered correctly",
        "Test 2",
    )
}

// helper funmction to get the first item in a flashCardList
fun acessFirst(flashCardList: List<FlashCard>): FlashCard {
    return flashCardList.first()
}

// function to test function: acessFirst
@EnabledTest
fun testAcessFirst() {
    testSame(
        acessFirst(deck1.flashCardList),
        flashCard1,
        "Test 1",
    )

    testSame(
        acessFirst(deck2.flashCardList),
        flashCard4,
        "Test 2",
    )
}

// helper function to return a size of flashList
fun flashSize(flashList: List<FlashCard>): Int {
    return flashList.size
}

// function to test function: flashSize
@EnabledTest
fun testflashSize() {
    testSame(
        flashSize(exampleStudyDeck1.currentFlash),
        3,
        "Test 1",
    )
    testSame(
        flashSize(exampleStudyDeck1.currentFlash),
        3,
        "Test 2",
    )

    testSame(
        flashSize(exampleStudyDeck5.currentFlash),
        0,
        "Test 3",
    )
}

// helper function to return front of a card
fun accessFront(flashCardList: List<FlashCard>): String {
    return acessFirst(flashCardList).front
}

// function to test function: accessFront
@EnabledTest
fun testAccessFront() {
    testSame(
        accessFront(deck1.flashCardList),
        "What is the capital of England?",
        "Test 1",
    )

    testSame(
        accessFront(deck2.flashCardList),
        "What is the capital of Australia?",
        "Test 2",
    )
}

// helper function to return back of a card
fun accessBack(flashCardList: List<FlashCard>): String {
    return acessFirst(flashCardList).back
}

// function to test function: accessBack
@EnabledTest
fun testAccessBack() {
    testSame(
        accessBack(deck1.flashCardList),
        "London",
        "Test 1",
    )

    testSame(
        accessBack(deck2.flashCardList),
        "Canberra",
        "Test 2",
    )
}

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// Now you just get to put this all together 💃

// TODO 1/1: Design the function chooseAndStudy(), where you'll
//           follow the comments in the supplied code to leverage
//           your prior work to allow the user to choose a deck,
//           study it, and return the number of correct self-
//           reports.
//
//           Your deck options MUST include at least one from each
//           of the following categories...
//
//           - Coded by hand (such as an example in data design)
//           - Read from a file (ala readCardsFile)
//           - Generated by code (ala perfectSquares)
//
//           Note: while this is an interactive program, you won't
//                 directly use reactConsole. Instead, just call
//                 the functions you already designed above :)
//
//           And of course, don't forget to test at least two runs
//           of this completed program!
//
//           (And, consider adding this to main() so you can see the
//           results of all your hard work so far this semester!)
//

// lets the user choose a deck and study it,
// returning the number self-reported correct
fun chooseAndStudy(): Int {
    // 1. Construct a list of options
    // (ala the instructions above)
    val fileDeck = readCardsFile("capitals3.txt")
    val perfectSquareDeck = perfectSquares(3)
    val codeDeck = deck1.flashCardList
    val deckOptions =
        listOf(
            // TODO: at least...
            // deck from file via readCardsFile,
            // deck from code via perfectSquares
            // deck hand-coded
            Deck("Capitals 1", fileDeck),
            Deck("Perfect Squares", perfectSquareDeck),
            Deck("Capitals 2", codeDeck),
        )

    // 2. Use chooseOption to let the user
    //    select a deck
    val deckChosen = chooseOption(deckOptions)

    // 3. Let the user study, return the
    //    number correctly answered
    return studyDeck(deckChosen)
}

// -----------------------------------------------------------------

fun main() {
    chooseAndStudy()
}

runEnabledTests(this)
main()

// -----------------------------------------------------------------
// Submission
// -----------------------------------------------------------------
