// -----------------------------------------------------------------
// Project: Part 2, Summary
// -----------------------------------------------------------------

// Since working on part 1 of the project, you've learned many
// approaches that will allow us to improve both the design of
// data/functions, as well as add new functionality!
//
// == Data/Function Design ==
// - You'll enhance each flash card to support an arbitrary
//   number of "tags" (i.e., string labels).
// - You'll generalize the meaning of a deck, such as to be
//   agnostic as to the very meaning of cards (and thus
//   support a wider variety of decks).
// - You'll enhance the menu system to be re-usable, as
//   well as to support quitting (i.e., leave without forcing a
//   selection).
//
// == Application Features ==
// - You'll implement a second method for interpreting
//   self-reported correctness of a card, this time using
//   some machine learning (ML) to process natural language (NLP);
//   the user will be able to select which method to use (since
//   both methods have their tradeoffs!).
// - When a user doesn't get a card correct (via self-report),
//   that card is placed at the back of the deck; thus, a deck
//   is only completed when a user gets all cards correct.
// - You'll provide deck options that are a subset of cards
//   containing a particular tag (e.g., all "hard" cards, or
//   those in the topic of "science").
// - Once the program is run, the user will be able to study
//   as many decks as they wish, selecting subsequent decks
//   from the menu until they quit.

// Of course, we'll design this program step-by-step :)

// When designing this enhanced project, you are welcome to draw
// upon your project part 1, our sample solutions (for part 1, and
// any homework), and/or lecture notes as you see fit & helpful.

// Lastly, here are a few overall project requirements...
// - Now that mutation has been covered, you may use it (unless
//   otherwise stated in the instructions); however, your usage
//   will be evaluated based upon the guidelines from class.
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
//     c) for classes with member functions, an associated
//        @EnabledTest function with sufficient tests for all
//        the member functions of the class
// - You will be evaluated on a number of criteria, including...
//   * Adherence to instructions and the Style Guide
//   * Correctly producing the functionality of the program
//   * Design decisions that include choice of tests, appropriate
//     application of programming approaches (e.g., sequence
//     abstractions, recursion, mutation), and task/type-driven
//     decomposition of functions.
//

import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.fileExists
import khoury.fileReadAsList
import khoury.isAnInteger
import khoury.linesToString
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame

// -----------------------------------------------------------------
// Flash Card data design
// (Hint: see Homework 5, Problem 3)
// -----------------------------------------------------------------

// TODO 1/1: Design the data type TaggedFlashCard to represent a
//           single flash card.
//
//           You should be able to represent the text prompt on
//           the front of the card, the text answer on the back,
//           as well as any number of textual tags (such as "hard"
//           or "science" -- this shouldn't come from any fixed
//           set of options, but truly open to however someone
//           wishes to categorize their cards).
//
//           Each card should have two member functions:
//           - isTagged, which determines if the card has a
//             supplied tag (e.g., has this card been tagged
//             as "hard"?)
//           - fileFormat, which produces a textual representation
//             of the card as "front|back|tag1,tag2,..."; that is
//             all three parts of the card separated with the pipe
//             ('|') character, and further separate any tags with
//             a comma
//
//           Include *at least* 3 example cards (which will come
//           in handy later for tests!), and make sure to test
//           the required member functions.
//

// (just useful values for
// the separation characters)
val sepCard = "|"
val sepTag = ","

// dataclass and tests that takes in a front string, back String, and a list of categories
// code taken from homework 5-p3-1
data class TaggedFlashCard(val front: String, val back: String, val difficulty: List<String>) {
    // function that takes in a tag, and checks if it is part of the list of categories provided
    fun isTagged(suppliedTag: String): Boolean {
        return this.difficulty.contains(suppliedTag)
    }

    // function that returns data class in a string format
    fun fileFormat(): String {
        return "$front $sepCard $back $sepCard ${difficulty.joinToString()}"
    }
}

// Examples of Tagged Flash Cards
val flash1 = TaggedFlashCard("What is the capital of Australia?", "Canberra", listOf("Medium"))
val flash2 = TaggedFlashCard("What is the capital of England?", "London", listOf("Easy", "Medium", "Hard"))
val flash3 = TaggedFlashCard("What is the capital of USA?", "Washington", listOf("Hard"))

// Test for functions within data class of: TaggedFlashCard
@EnabledTest
fun testTaggedFlashCard() {
    testSame(
        flash1.isTagged("Medium"),
        true,
        "Card 1 True",
    )

    testSame(
        flash1.isTagged("Easy"),
        false,
        "Card 1 False",
    )

    testSame(
        flash2.isTagged("Medium"),
        true,
        "Card 2 True",
    )

    testSame(
        flash2.isTagged("Super Easy"),
        false,
        "Card 2 False",
    )

    testSame(
        flash3.isTagged("Hard"),
        true,
        "Card 2 True",
    )

    testSame(
        flash3.isTagged("Right in the middle"),
        false,
        "Card 2 False",
    )

    testSame(
        flash1.fileFormat(),
        "What is the capital of Australia? | Canberra | Medium",
        "Card 1 String",
    )

    testSame(
        flash2.fileFormat(),
        "What is the capital of England? | London | Easy, Medium, Hard",
        "Card 1 String",
    )

    testSame(
        flash3.fileFormat(),
        "What is the capital of USA? | Washington | Hard",
        "Card 1 String",
    )
}

// -----------------------------------------------------------------
// Files of tagged flash cards
// -----------------------------------------------------------------

// Now that we have our updated cards, let's update how we read
// them from files.

// TODO 1/2: Design the function stringToTaggedFlashCard that
//           takes a string, assumed to be in the format described
//           for the fileFormat member function above, and produces
//           the corresponding tagged flash card.
//
//           Hint: review part 1 of the project, TODO 2/3
//

// function that takes in a formatted string as defaulted in fileFormat() function and returns a TaggedFlashCard
fun stringToTaggedFlashCard(inputtedString: String): TaggedFlashCard {
    val list: List<String> = inputtedString.split(sepCard)
    if (list[2].isEmpty() == true) {
        return TaggedFlashCard(list[0], list[1], emptyList())
    } else {
        return TaggedFlashCard(list[0], list[1], list[2].split(sepTag))
    }
}

// function that tests function: stringToTaggedFlashCard
@EnabledTest
fun testStringToTaggedFlashCard() {
    testSame(
        stringToTaggedFlashCard("What is the capital of Australia|Canberra|Easy,Medium,Hard"),
        TaggedFlashCard("What is the capital of Australia", "Canberra", listOf("Easy", "Medium", "Hard")),
        "Test 1",
    )

    testSame(
        stringToTaggedFlashCard("What is the capital of England|London|Easy,Medium,Hard"),
        TaggedFlashCard("What is the capital of England", "London", listOf("Easy", "Medium", "Hard")),
        "Test 2",
    )
}

// TODO 2/2: Design the function readTaggedFlashCardsFile that
//           takes a path to a file and produces a list of
//           tagged flash cards.
//
//           If the file does not exist, return an empty list.
//           Otherwise, you can assume that every line is
//           formatted in the string format we just worked with.
//
//           Hint:
//           - Review part 1 of the project, TODO 3/3
//           - We've provided an "example_tagged.txt" file that you
//             can use for testing if you'd like; also make sure to
//             test your function when the supplied file does not
//             exist!
//

// function that takes in a string as a filePath and returns a list of TaggedFlashCards found from the file
// code taken from project 1 TODO 3/3
fun readTaggedFlashCardsFile(filePath: String): List<TaggedFlashCard> {
    val incaseEmpty: List<TaggedFlashCard> = listOf()
    if (fileExists(filePath) == true) {
        val fileList: List<String> = fileReadAsList(filePath)
        val fileSize: Int = fileList.size

        return fileList.map(::stringToTaggedFlashCard)
    } else {
        return incaseEmpty
    }
}

// function that tests function: readTaggedFlashCardsFile
@EnabledTest
fun testReadCardsFile() {
    testSame(
        readTaggedFlashCardsFile("example_tagged.txt"),
        listOf(TaggedFlashCard("c", "3", listOf("hard", "science")), TaggedFlashCard("d", "4", listOf("hard"))),
        "test file provided",
    )

    testSame(
        readTaggedFlashCardsFile("FILE-DOES-NOT-EXIST.txt"),
        listOf(),
        "No file found test",
    )
}

// -----------------------------------------------------------------
// Deck design
// -----------------------------------------------------------------

// If you think about it, once a deck has been selected, our study
// application doesn't need much information about cards to work...
// in fact, it doesn't even need the concept of a card. Consider
// the following:
//

// The deck is either exhausted,
// showing the question, or
// showing the answer
enum class DeckState {
    EXHAUSTED,
    QUESTION,
    ANSWER,
}

// Basic functionality of any deck
interface IDeck {
    // The state of the deck
    fun getState(): DeckState

    // The currently visible text
    // (or null if exhausted)
    fun getText(): String?

    // The number of question/answer pairs
    // (does not change when question are
    // cycled to the end of the deck)
    fun getSize(): Int

    // Shifts from question -> answer
    // (if not QUESTION state, returns the same IDeck)
    fun flip(): IDeck

    // Shifts from answer -> next question (or exhaustion);
    // if the current question was correct it is discarded,
    // otherwise cycled to the end of the deck
    // (if not ANSWER state, returns the same IDeck)
    fun next(correct: Boolean): IDeck
}

// This contract of operations will allow our study application to
// work with a variety of sources, including lists and even code
// that never explicitly stores cards!
//
// (For a similar problem, see Homework 6, Problem 3, TODO 2,
// where you implemented stateful classes to integrate with an
// object-oriented reactConsole.)
//

// TODO 1/2: Design TFCListDeck to implement the IDeck interface
//           for a supplied list of tagged flash cards. For this
//           problem your class must have *no* mutable state and
//           all member data should be private.
//
//           When testing, make sure to test the behavior of all
//           the member functions of the interface in a variety
//           of situations.
//
//           Hint: using default arguments can make your class
//                 easier to create initially, see...
//
//           kotlinlang.org/docs/functions.html#default-arguments
//

// Data class that takes list of TaggedFlashCards, boolean value if currently on front card or not, and a function that calculates the size of the deck
class TFCListDeck(val cards: List<TaggedFlashCard>, val front: Boolean, val size: () -> Int = { cards.size }) : IDeck {
    override fun getState(): DeckState {
        if (cards.isEmpty()) {
            return DeckState.EXHAUSTED
        } else {
            return when (front) {
                true -> DeckState.QUESTION
                false -> DeckState.ANSWER
            }
        }
    }

    override fun getText(): String? {
        return when (getState()) {
            DeckState.QUESTION -> cards[0].front
            DeckState.ANSWER -> cards[0].back
            DeckState.EXHAUSTED -> null
        }
    }

    override fun getSize(): Int {
        return cards.size
    }

    override fun flip(): IDeck {
        return when (getState() == DeckState.QUESTION) {
            true -> TFCListDeck(cards, false)
            false -> this
        }
    }

    override fun next(correct: Boolean): IDeck {
        return when (getState() == DeckState.ANSWER) {
            true ->
                TFCListDeck(
                    cards =
                        when (correct) {
                            true -> cards.drop(1)
                            false -> cards.drop(1) + listOf(cards[0])
                        },
                    true,
                )
            false -> this
        }
    }
}

// Example TFCListDecks Created
val exampleTFCList1 = TFCListDeck(listOf(flash1, flash2, flash3), true)
val exampleTFCList2 = TFCListDeck(listOf(flash1, flash2, flash3), false)
val exampleOfEmptyTFCList = TFCListDeck(listOf(), true)

// tester function for all functions within the dataclass: TFCListDeck
@EnabledTest
fun testTFCListDeck() {
    testSame(
        exampleTFCList1.getState(),
        DeckState.QUESTION,
        "Correct Output for getState: QUESTION",
    )

    testSame(
        exampleTFCList2.getState(),
        DeckState.ANSWER,
        "Correct Output for getState: ANSWER",
    )

    testSame(
        exampleOfEmptyTFCList.getState(),
        DeckState.EXHAUSTED,
        "Correct Output for getState: EXHAUSTED",
    )

    testSame(
        exampleTFCList1.getText(),
        flash1.front,
        "Correct Output for getText: Question Outputted",
    )

    testSame(
        exampleTFCList2.getText(),
        flash1.back,
        "Correct Output for getText: Answer Outputted",
    )

    testSame(
        exampleOfEmptyTFCList.getText(),
        null,
        "Correct Output for getText: Null Outputted",
    )

    testSame(
        exampleTFCList1.getSize(),
        3,
        "Correct Size Ouputted",
    )

    testSame(
        exampleTFCList2.getSize(),
        3,
        "Correct Size Ouputted",
    )

    testSame(
        exampleOfEmptyTFCList.getSize(),
        0,
        "Correct Size Ouputted",
    )

    val exampleTFCList1flipped = exampleTFCList1.flip()

    testSame(
        exampleTFCList1flipped.getState(),
        DeckState.ANSWER,
        "Flipping from Question To Answer",
    )

    testSame(
        exampleTFCList2.flip(),
        exampleTFCList2,
        "Flipping from Answer To Question",
    )

    val exampleTFCList2drop = exampleTFCList2.next(true)

    testSame(
        exampleTFCList2drop.getText(),
        flash2.front,
        "Card Dropped from the deck as a result of correct answer inputted",
    )

    val exampleTFCList2drop1 = exampleTFCList2.next(false)
    val exampleTFCList2drop2 = exampleTFCList2.next(false).flip().next(false).flip().next(false)

    testSame(
        exampleTFCList2drop1.getText(),
        flash2.front,
        "Card moved from the front of deck, next part is in next test",
    )

    testSame(
        exampleTFCList2drop2.getText(),
        flash1.front,
        "Card moved to the back as a result of wrong answer inputted",
    )

    testSame(
        exampleOfEmptyTFCList.next(false),
        exampleOfEmptyTFCList,
        "Since state returned is false, the same deck is returned with no changes",
    )
}

// TODO 2/2: Now design PerfectSquaresDeck to implement the IDeck
//           interface. You are *not* allowed to generate any
//           flash cards, nor have mutable state; the goal is to
//           act as though it had a list produced by the
//           perfectSquares function in part 1 of the project,
//           but without ever having to generate all those cards!
//           Again, as is generally good practice, keep all your
//           member data private!

//           Hint: you will still need to keep track of the
//                 *sequence* of upcoming numbers (particularly
//                 as some may get cycled back due to incorrect
//                 responses).

// Data class that takes in a integer, boolean value if currently on front card or not, and a list constructor that creates a List, whoose size depends on the integer passed
class PerfectSquaresDeck(val integer: Int, val front: Boolean, val size: List<Int> = List(integer) { it + 1 }) : IDeck {
    override fun getState(): DeckState {
        return when {
            size.isEmpty() -> DeckState.EXHAUSTED
            front -> DeckState.QUESTION
            else -> DeckState.ANSWER
        }
    }

    override fun getText(): String? {
        return when (getState()) {
            DeckState.QUESTION -> "${size[0]}^2 = ?"
            DeckState.ANSWER -> "${size[0] * size[0]}"
            DeckState.EXHAUSTED -> null
        }
    }

    override fun getSize(): Int {
        return size.size
    }

    override fun flip(): IDeck {
        return when (getState() == DeckState.QUESTION) {
            true -> PerfectSquaresDeck(integer, false, size)
            false -> this
        }
    }

    override fun next(correct: Boolean): IDeck {
        return when (getState() == DeckState.ANSWER) {
            true ->
                PerfectSquaresDeck(
                    integer,
                    true,
                    size =
                        when (correct) {
                            true -> size.drop(1)
                            false -> size.drop(1) + listOf(size[0])
                        },
                )
            false -> this
        }
    }
}

// example of PerfectSquaresDeck decks
val examplePerfectSquaresDeck1 = PerfectSquaresDeck(3, true)
val examplePerfectSquaresDeck1Continued = PerfectSquaresDeck(3, false)
val examplePerfectSquaresDeck2 = PerfectSquaresDeck(6, false)
val exampleEmptyPerfectSquaresDeck = PerfectSquaresDeck(0, true)

// tester function for all functions within the dataclass: PerfectSquaresDeck
@EnabledTest
fun testPerfectSquaresDeck() {
    testSame(
        examplePerfectSquaresDeck1.getState(),
        DeckState.QUESTION,
        "Correct Output for getState: QUESTION",
    )

    testSame(
        examplePerfectSquaresDeck2.getState(),
        DeckState.ANSWER,
        "Correct Output for getState: ANSWER",
    )

    testSame(
        exampleEmptyPerfectSquaresDeck.getState(),
        DeckState.EXHAUSTED,
        "Correct Output for getState: EXHAUSTED",
    )

    testSame(
        examplePerfectSquaresDeck1.getText(),
        "1^2 = ?",
        "Correct Output for getText: Question Outputted",
    )

    testSame(
        examplePerfectSquaresDeck2.getText(),
        "1",
        "Correct Output for getText: Answer Outputted",
    )

    testSame(
        exampleEmptyPerfectSquaresDeck.getText(),
        null,
        "Correct Output for getText: Null Outputted",
    )

    testSame(
        examplePerfectSquaresDeck1.getSize(),
        3,
        "Correct Size Ouputted",
    )

    testSame(
        examplePerfectSquaresDeck2.getSize(),
        6,
        "Correct Size Ouputted",
    )

    testSame(
        exampleEmptyPerfectSquaresDeck.getSize(),
        0,
        "Correct Size Ouputted",
    )

    val examplePerfectSquareDeckflipped = examplePerfectSquaresDeck1.flip()

    testSame(
        examplePerfectSquareDeckflipped.getState(),
        DeckState.ANSWER,
        "Flipping from Question To Answer",
    )

    testSame(
        examplePerfectSquaresDeck2.flip(),
        examplePerfectSquaresDeck2,
        "Flipping from Answer To Question",
    )

    val examplePerfectSquaredrop1 = examplePerfectSquaresDeck1Continued.next(false)
    val examplePerfectSquaredrop2 = examplePerfectSquaresDeck1Continued.next(false).flip().next(false).flip().next(false)

    testSame(
        examplePerfectSquaredrop1.getText(),
        "2^2 = ?",
        "Card moved from the front of deck, next part is in next test",
    )

    testSame(
        examplePerfectSquaredrop2.getText(),
        "1^2 = ?",
        "Card moved to the back as a result of wrong answer inputted",
    )
    testSame(
        exampleEmptyPerfectSquaresDeck.next(false),
        exampleEmptyPerfectSquaresDeck,
        "Since state returned is false, the same deck is returned with no changes",
    )
}

// -----------------------------------------------------------------
// Menu design
// -----------------------------------------------------------------

// The chooseOption function in part 1 of the project was good, but
// let's see what we can do to improve upon it in two core ways...
//
// a) Part 1 allowed you to select from amongst decks, which means
//    you'd have to copy-paste if you wanted to have a menu of
//    other data (such as files, or months of the year); let's
//    make the function agnostic as to the type of the list items
//    being selected.
// b) Part 1 didn't allow for the possibility of not selecting an
//    option; let's add a quit feature!
//
// To help with (a), consider the following interface, which
// requires that a menu option be able to return a textual
// representation (that is then displayed in the menu!)...
//

// the only required capability for a menu option
// is to be able to render a title
interface IMenuOption {
    fun menuTitle(): String
}

// as well as the following general implementation (great for
// tests & examples), which satisfies the contract via pairing
// a value (of any type) with a name...

// a menu option with a single value and name
data class NamedMenuOption<T>(val option: T, val name: String) : IMenuOption {
    override fun menuTitle(): String = name
}

// individual examples, as well as a list
// (an example for a list of menu options!)
val opt1A = NamedMenuOption(1, "apple")
val opt2B = NamedMenuOption(2, "banana")
val optsExample = listOf(opt1A, opt2B)

// TODO 1/1: Finish designing the program chooseMenuOption that
//           takes a list (assumed to be non-empty) of any type
//           (as long as it implements the IMenuOption interface),
//           produces a corresponding numbered menu (1-# of list
//           items, each showing its menuTitle), and returns the
//           list item corresponding to the number entered (or null
//           if 0 was entered to indicate a desire to quit without
//           choosing an option). Keep displaying the menu until a
//           valid menu selection (or quitting) is indicated.
//
//           Hints:
//           - You'll find the code from chooseOption (in part 1)
//             to be a *very* good starting point.
//           - Homework 5, Problem 4, has a very similar interface,
//             which can give you an idea for how you'd use it.
//           - To help you get started, you have some examples
//             above and prompts below; a "stub" for the
//             chooseMenuOption function (to help with the
//             signature and overall structure); and a set of
//             tests that should pass once the program has been
//             completed.
//

// Some useful outputs
val menuPrompt = "Enter your choice (or 0 to quit)"
val menuQuit = "You quit"
val menuChoicePrefix = "You chose: "

// function that presents choices to pick from for the user in a formatted manner
fun <T : IMenuOption> choicesToText(optionsList: List<T>): String {
    // function that decides what to return depending on the index count
    fun updateListItem(index: Int): String {
        if (index in optionsList.indices) {
            return "${index + 1}. ${optionsList[index].menuTitle()}"
        } else {
            if (index == optionsList.size) {
                return ""
            } else {
                return menuPrompt
            }
        }
    }
    // creates a list of string by passing in updateListItem function and the length of the desired list
    val result = List<String>(optionsList.size + 2, ::updateListItem)
    return linesToString(result)
}

// function to test function: choicesToText
@EnabledTest
fun testChoicesToText() {
    testSame(
        choicesToText(listOf(opt1A)),
        linesToString(
            "1. ${opt1A.name}",
            "",
            menuPrompt,
        ),
        "one",
    )

    testSame(
        choicesToText(listOf(opt1A, opt2B)),
        linesToString(
            "1. ${opt1A.name}",
            "2. ${opt2B.name}",
            "",
            menuPrompt,
        ),
        "two",
    )
}

// function that checks if user input is valid or not
fun keepIfValid(
    input: String,
    deckIndices: IntRange,
): Int {
    if (isAnInteger(input) == true) {
        if (input.toInt() - 1 == -1) {
            return -3
        } else if (input.toInt() - 1 in deckIndices) {
            return input.toInt() - 1
        } else {
            return -1
        }
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
        "Valid Test 1",
    )

    testSame(
        keepIfValid("2", 0..2),
        1,
        "Valid Test 2",
    )

    testSame(
        keepIfValid("3", 0..2),
        2,
        "Valid Test 3",
    )
    testSame(
        keepIfValid("5", 0..2),
        -1,
        "Invalid Input",
    )

    testSame(
        keepIfValid("0", 0..2),
        -3,
        "Input to Quit",
    )
}

// Provides an interactive opportunity for the user to choose
// an option or quit.
fun <T : IMenuOption> chooseMenuOption(options: List<T>): T? {
    // your code here!
    // - call reactConsole (with appropriate handlers)
    // - return the selected option (or null for quit)

    // function that calls string formatting function to present options to the user
    fun renderOptions(state: Int): String {
        return choicesToText(options)
    }

    // function that checks if returns user input, to be checked against keepIfValid Function
    fun transitionOptionChoice(
        ignoredState: Int,
        kbInput: String,
    ): Int {
        return keepIfValid(kbInput, options.indices)
    }

    // function that returns a boolean based on the output of kbInput function, signifying whether or not a valid choice has been entered
    fun validChoiceEntered(state: Int): Boolean {
        if (state == -3) {
            return true
        } else if (state in options.indices) {
            return true
        } else {
            return false
        }
    }

    // function that renders output depending on the user input
    fun renderChoice(state: Int): String {
        if (state == -3) {
            return menuQuit
        } else {
            return menuChoicePrefix + (options[state].menuTitle())
        }
    }

    val valid: Int =
        reactConsole(
            initialState = -1,
            stateToText = ::renderOptions,
            nextState = ::transitionOptionChoice,
            isTerminalState = ::validChoiceEntered,
            terminalStateToText = ::renderChoice,
        )

    if (valid == -3) {
        return null
    } else {
        return options[valid]
    }
}

// function that tests function: chooseMenuOption
@EnabledTest
fun testChooseMenuOption() {
    testSame(
        captureResults(
            { chooseMenuOption(listOf(opt1A)) },
            "howdy",
            "0",
        ),
        CapturedResult(
            null,
            "1. ${opt1A.name}",
            "",
            menuPrompt,
            "1. ${opt1A.name}",
            "",
            menuPrompt,
            menuQuit,
        ),
        "quit",
    )

    testSame(
        captureResults(
            { chooseMenuOption(optsExample) },
            "hello",
            "10",
            "-3",
            "1",
        ),
        CapturedResult(
            opt1A,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "${menuChoicePrefix}${opt1A.name}",
        ),
        "1",
    )

    testSame(
        captureResults(
            { chooseMenuOption(optsExample) },
            "3",
            "-1",
            "2",
        ),
        CapturedResult(
            opt2B,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "${menuChoicePrefix}${opt2B.name}",
        ),
        "2",
    )
}

// -----------------------------------------------------------------
// Machine learning for sentiment analysis
// -----------------------------------------------------------------

// In part 1 of the project, you designed isPositive as a way to
// interpret whether a student's self-report was positive or
// negative; in the world of Machine Learning (a subfield of
// Artificial Intelligence, or AI), this is an approach to
// "sentiment analysis" - a problem in Natural Language Processing
// (NLP) that seeks to analyze text to understand the emotional
// tone of some text.
//
// In this context, what you built was a "binary classifier" of
// text, meaning it output one of two values according to the input
// string. In Kotlin we can describe this input-output relationship
// using the following shortcut...

typealias PositivityClassifier = (String) -> Boolean

// This code simply means we can now use PositivityClassifier
// anywhere we would have used the type on the right (e.g.,
// as the type in a function's parameter or return type).
//
// Our goal is now to try and use a more sophisticated approach
// to sentiment analysis - one that learns positivity/negativity
// based upon a dataset of supplied examples. To represent such a
// dataset, consider the following type...

data class LabeledExample<E, L>(val example: E, val label: L)

// This associates a "label" (such as positive vs negative, or
// cat video vs boring) with an example. Here is one such dataset:

val datasetYN: List<LabeledExample<String, Boolean>> =
    listOf(
        LabeledExample("yes", true),
        LabeledExample("y", true),
        LabeledExample("indeed", true),
        LabeledExample("aye", true),
        LabeledExample("oh yes", true),
        LabeledExample("affirmative", true),
        LabeledExample("roger", true),
        LabeledExample("uh huh", true),
        LabeledExample("true", true),
        // just a visual separation of
        // the positive/negative examples
        LabeledExample("no", false),
        LabeledExample("n", false),
        LabeledExample("nope", false),
        LabeledExample("negative", false),
        LabeledExample("nay", false),
        LabeledExample("negatory", false),
        LabeledExample("uh uh", false),
        LabeledExample("absolutely not", false),
        LabeledExample("false", false),
    )

// FYI: we call this dataset "balanced" since it has an equal
//      number of examples of the labels (i.e., # true and #false).
//      Such a balance is *one* tool (of many) when trying to avoid
//      algorithmic bias (en.wikipedia.org/wiki/Algorithmic_bias).

// Notice that our simple heuristic of the first letter is pretty
// good according to this dataset, but will make some lucky
// guesses (e.g., "false") and some actual mistakes (e.g., "true").
// We have provided below that code, as well as a set of tests that
// reference our labeled dataset - make sure you understand all of
// this code (including the comments in the tests about when & how
// the heuristic is predictably getting the answer wrong).

// Heuristically determines if the supplied string
// is positive based upon the first letter being Y
fun isPositiveSimple(s: String): Boolean {
    return s.uppercase().startsWith("Y")
}

// tests that an element of the dataset matches
// with expectation of its correctness on a
// particular classifier
fun helpTestElement(
    index: Int,
    expectedIsCorrect: Boolean,
    isPos: PositivityClassifier,
) {
    testSame(
        isPos(datasetYN[index].example),
        when (expectedIsCorrect) {
            true -> datasetYN[index].label
            false -> !datasetYN[index].label
        },
        when (expectedIsCorrect) {
            true -> datasetYN[index].example
            false -> "${ datasetYN[index].example } <- WRONG"
        },
    )
}

// function that tests function: IsPositiveSimple
@EnabledTest
fun testIsPositiveSimple() {
    val classifier = ::isPositiveSimple

    // correctly responds with positive
    for (i in 0..1) {
        helpTestElement(i, true, classifier)
    }

    // incorrectly responds with negative
    for (i in 2..8) {
        helpTestElement(i, false, classifier)
    }

    // correctly responds with negative, sometimes
    // due to luck (i.e., anything not starting
    // with the letter Y is assumed negative)
    for (i in 9..17) {
        helpTestElement(i, true, classifier)
    }
}

// One approach we *could* take is just to have the computer learn
// by rote memorization: that is, respond with the labeled answer
// from the dataset. But what about if the student supplies an
// input not in this list? The approach we'll try as a way to
// handle this situation is the following...
// - If the response is known in the dataset (independent of
//   upper/lower-case), use the associated label
// - Otherwise...
//   Find the 3 "closest" examples and respond with a majority
//   vote of their associated labels
//
// This algorithm will represent our attempt to "generalize"
// from the dataset; we know we'll always get certain responses
// correct, and we'll let our dataset inform the response of
// unknown inputs. As with all approaches based upon machine
// learning, this approach is likely to make mistakes (even those
// that we'll find confusing/comical), and so we should be
// judicious in how we apply the system in the world.
//
// Now let's build up this classifier, step-by-step :)
//

// TODO 1/5: When finding closest examples, and majority vote, it
//           will be helpful to be able to get the "top-k" of a
//           list by some measure; meaning, a function that can
//           get the top-3 strings in a list by length, but
//           equally identify the top-1 (i.e., best) song by
//           ratings. To help, consider the following definition
//           of an "evaluation" function: one that takes an input
//           of some type and associates an output "score" (where
//           bigger scores are understood to be better):

typealias EvaluationFunction<T> = (T) -> Int

//          Design the function topK that takes a list of
//          items, k (assumed to be a postive integer), and a
//          corresponding evaluation function, and then returns
//          the k items in the list that get the highest score
//          (if there are ties, you are free to return any of the
//          winners; if there aren't enough items in the list,
//          return as many as you can).
//
//          Hint: You did this problem in Homework 7, Problem 1
//                - To simplify, you can avoid the ItemScore type
//                  by using the built-in `zip` function that you
//                  implemented in Homework 7, Problem 3.
//                - Later functions will use topK and assume the
//                  parameter ordering is as described above (which
//                  is a small swap from the sample solution).
//

// function that takes in a list of consistent type T, and a evaluation function to output a
// list with only the labels from the original list, in order of closer to furthest based off a score produced in the evaluation function
fun <T> topK(
    itemList: List<T>,
    k: Int,
    evalFunc: EvaluationFunction<T>,
): List<T> {
    // associate each item with its score

    val scores =
        itemList.map {
            evalFunc(it)
        }

    val pairing = itemList.zip(scores)

    // sort by score
    val sortedByEval =
        pairing.sortedByDescending {
            it.second
        }

    // strip away score
    val sortedWithoutScores =
        sortedByEval.map {
            it.first
        }

    // get the first-k (i.e., top-k via score)
    return sortedWithoutScores.take(k)
}

// function to test function: TopK
@EnabledTest
fun testTopK() {
    val singleLongestString = {
            strings: List<String> ->
        topK(
            strings,
            1,
            { s: String -> s.length },
        )
    }

    testSame(
        singleLongestString(emptyList<String>()),
        emptyList<String>(),
        "a/empty",
    )

    testSame(
        singleLongestString(
            listOf(
                "a",
                "do",
                "tri",
                "pneumonoultramicroscopicsilicovolcanoconiosis",
                "",
            ),
        ),
        listOf(
            "pneumonoultramicroscopicsilicovolcanoconiosis",
        ),
        "a/longer",
    )

    // b) The 2 smallest numbers in a list of integers
    val twoSmallestNums = {
            nums: List<Int> ->
        topK(
            nums,
            2,
            { -it },
        )
    }

    testSame(
        twoSmallestNums(emptyList<Int>()),
        emptyList<Int>(),
        "b/empty",
    )

    testSame(
        twoSmallestNums(listOf(42)),
        listOf(42),
        "b/shorter",
    )

    testSame(
        twoSmallestNums(
            listOf(
                8,
                6,
                7,
                5,
                3,
                0,
                9,
            ),
        ),
        listOf(0, 3),
        "b/longer",
    )
}

// TODO 2/5: Great! Now we have to answer the question from before:
//           what does it mean for two strings to be "close"?
//           There are actually multiple reasonable ways of
//           capturing such a distance, one of which is the
//           Levenshtein Distance, which describes the minimum
//           number of single-character changes (e.g., adding a
//           character, removing one, or substituting) required to
//           change one sequence into another
//           (https://en.wikipedia.org/wiki/Levenshtein_distance).
//           Your task is to design the function
//           levenshteinDistance that computes this distance for
//           two supplied strings.
//
//           Hint: Homework 7, Problem 2 :)
//

// function that calculates the distances between two strings used the lavenshtein equation
fun levenshteinDistance(
    a: String,
    b: String,
): Int {
    // shorthand for producing all the letters of
    // a string except the first
    fun tail(s: String): String = s.drop(1)

    // shorthand for recursive call, making this
    // look like the wikipedia definition
    val lev = ::levenshteinDistance

    return when {
        b.isEmpty() -> a.length
        a.isEmpty() -> b.length
        a[0] == b[0] -> lev(tail(a), tail(b))
        else ->
            1 +
                minOf(
                    lev(tail(a), b),
                    lev(a, tail(b)),
                    lev(tail(a), tail(b)),
                )
    }
}

// function that tests the function: levenshteinDistance
@EnabledTest
fun testLevenshteinDistance() {
    testSame(
        levenshteinDistance("", "howdy"),
        5,
        "'', 'howdy'",
    )

    testSame(
        levenshteinDistance("howdy", ""),
        5,
        "'howdy', ''",
    )

    testSame(
        levenshteinDistance("howdy", "howdy"),
        0,
        "'howdy', 'howdy'",
    )

    testSame(
        levenshteinDistance("kitten", "sitting"),
        3,
        "'kitten', 'sitting'",
    )

    testSame(
        levenshteinDistance("sitting", "kitten"),
        3,
        "'sitting', 'kitten'",
    )
}

// TODO 3/5: Great! Now let's design a "k-Nearest Neighbor"
//           classifier (you can read online description, such as
//           on Wikipedia, for lots of details & variants, but
//           we'll give you all the information you need here).
//
//           The goal here: given a dataset of labeled examples,
//           a distance function, and a number k, let the k
//           closest elements of the dataset "vote" (with their
//           label) as to what the label of a new element
//           should be. To be clear, here is a way of describing
//           a distance function, producing a integer distance
//           between two elements of a type...

typealias DistanceFunction<T> = (T, T) -> Int

//           Since this method might give an incorrect response,
//           we'll return not only predicted label, but the number
//           of "votes" received for that label (out of k)...

data class ResultWithVotes<L>(val label: L, val votes: Int)

//           Your task is to uncomment and then *test* the supplied
//           nnLabel function (note: you might need to fix up the
//           ordering of your topK arguments to play nicely with
//           the code here - you should NOT change this function).
//           You'll find guiding comments to help.
//

// uses k-nearest-neighbor (kNN) to predict the label
// for a supplied example given a labeled dataset
// and distance function
fun <E, L> nnLabel(
    queryExample: E,
    dataset: List<LabeledExample<E, L>>,
    distFunc: DistanceFunction<E>,
    k: Int,
): ResultWithVotes<L> {
    // 1. Use topK to find the k-closest dataset elements:
    //    finding the elements whose negated distance is the
    //    greatest is the same as finding those that are closest.
    val closestK =
        topK(dataset, k) {
            -distFunc(queryExample, it.example)
        }

    // 2. Discard the examples, we only care about their labels
    val closestKLabels = closestK.map { it.label }

    // 3. For each distinct label, count up how many time it
    //    showed up in step #2
    //    (Note: once we know the Map type, there are WAY simpler
    //           ways to do this!)
    val labelsWithCounts =
        closestKLabels.distinct().map {
                label ->
            Pair(
                // first = label
                label,
                // second = number of votes
                closestKLabels.filter({ it == label }).size,
            )
        }

    // 4. Use topK to get the label with the greatest count
    val topLabelWithCount = topK(labelsWithCounts, 1, { it.second })[0]

    // 5. Return both the label and the number of votes (of k)
    return ResultWithVotes(
        topLabelWithCount.first,
        topLabelWithCount.second,
    )
}

// function that tests function: nnLabel
@EnabledTest
fun testNNLabel() {
    // don't change this dataset:
    // think of them as points on a line...
    // (with ? referring to the example below)
    //
    //       a   a                  ?       b           b
    // |--- --- --- --- --- --- --- --- --- ---|
    //   1   2   3   4   5   6   7   8   9  10
    val dataset =
        listOf(
            LabeledExample(2, "a"),
            LabeledExample(3, "a"),
            LabeledExample(7, "b"),
            LabeledExample(10, "b"),
        )

    // A simple distance: just the absolute value
    fun myAbsVal(
        a: Int,
        b: Int,
    ): Int {
        val diff = a - b

        return when (diff >= 0) {
            true -> diff
            false -> -diff
        }
    }

    // TODO: to demonstrate that you understand how kNN is
    //       supposed to work (and what the supplied code returns),
    //       you are going to write tests here for a selection of
    //       cases that use the dataset and distance function above.
    //
    //       To help you get started, consider testing for point 5,
    //       with k=3:
    //       a) All the points with their distances are...
    //          a = |2 - 5| = 3
    //          a = |3 - 5| = 3
    //          b = |7 - 5| = 2
    //          b = |10 - 5| = 5
    //       b) SO, the labels of the three closest are...
    //          a (2 votes)
    //          b (1 vote)
    //       c) SO, kNN in this situation would predict the label
    //          for this point to be "a", with confidence 2/3 (medium)
    //
    //       We capture this test as...
    //

    testSame(
        nnLabel(1, dataset, ::myAbsVal, k = 3),
        ResultWithVotes("a", 2),
        "NN: 5->a, 2/3",
        // medium confidence
    )
    //       Now your task is to write tests for the following
    //       additional cases...
    //       1. 1 (k=1)
    //       2. 1 (k=2)
    //       3. 10 (k=1)
    //       4. 10 (k=2)

    testSame(
        nnLabel(1, dataset, ::myAbsVal, k = 1),
        ResultWithVotes("a", 1),
        "NN: 1->a, 1/1",
    )

    testSame(
        nnLabel(1, dataset, ::myAbsVal, k = 2),
        ResultWithVotes("a", 2),
        "NN: 1->a, 2/2 ",
    )

    testSame(
        nnLabel(10, dataset, ::myAbsVal, k = 1),
        ResultWithVotes("b", 1),
        "NN: 1->b, 1/1",
    )

    testSame(
        nnLabel(10, dataset, ::myAbsVal, k = 2),
        ResultWithVotes("b", 2),
        "NN: 10->b, 2/2",
    )
}

// TODO 4/5: Ok - now it's time to put some pieces together!!
//           Finish designing the function yesNoClassifier below -
//           you've been provided with guiding steps, as well as
//           tests that should pass, including those that are
//           incorrect (with lots of confidence!).
//

// we'll generally use k=3 in our classifier
val classifierK = 3

// function that outputs ResultWithVotes depending on the string inputted
fun yesNoClassifier(s: String): ResultWithVotes<Boolean> {
    // 1. Convert the input to lowercase
    //    (since) the data set is all lowercase
    // 2. Check to see if the lower-case input
    //    shows up exactly within the dataset
    //    (you can assume there are no duplicates
    // 3. If the input was found, simply return its label with 100%
    //    confidence (3/3); otherwise, return the result of
    //    performing a 3-NN classification using the dataset and
    //    Levenshtein distance metric.

    val lowerCaseInput = s.lowercase()

    for (element in datasetYN) {
        if (lowerCaseInput == element.example) {
            return ResultWithVotes(element.label, 3)
            break
        }
    }

    return nnLabel(s, datasetYN, ::levenshteinDistance, classifierK)
}

// function that tests function: yesNoClassifier
@EnabledTest
fun testYesNoClassifier() {
    testSame(
        yesNoClassifier("YES"),
        ResultWithVotes(true, 3),
        "YES: 3/3",
    )

    testSame(
        yesNoClassifier("no"),
        ResultWithVotes(false, 3),
        "no: 3/3",
    )

    testSame(
        yesNoClassifier("nadda"),
        ResultWithVotes(false, 2),
        "nadda: 2/3",
    ) // pretty good ML!

    testSame(
        yesNoClassifier("yerp"),
        ResultWithVotes(true, 3),
        "yerp: 3/3",
    ) // pretty good ML!

    testSame(
        yesNoClassifier("ouch"),
        ResultWithVotes(true, 3),
        "ouch: 3/3",
    ) // seems very confident in this wrong answer...

    testSame(
        yesNoClassifier("now"),
        ResultWithVotes(false, 3),
        "now 3/3",
    ) // seems very confident, given the input doesn't make sense?
}

// TODO 5/5: Now that you have a sense of how this approach works,
//           including some of the (confident) mistakes it can make,
//           uncomment the following lines to have a classifier
//           (that we could use side-by-side with our heuristic).

// function that uses yesNoClassifer function to produce a boolean based on passed in string
fun isPositiveML(s: String): Boolean = yesNoClassifier(s).label

// function that tests function: isPositiveML
@EnabledTest
fun testIsPositiveML() {
    // correctly responds with positive (rote memorization)
    for (i in 0..8) {
        helpTestElement(i, true, ::isPositiveML)
    }

    // correctly responds with negative (rote memorization)
    for (i in 9..17) {
        helpTestElement(i, true, ::isPositiveML)
    }
}

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// Whew! You've done a lot :)
//
// Now let's put it together and study!!
//

// TODO 1/2: Design the program studyDeck2 that uses the
//           reactConsole function to study through a
//           supplied deck using a supplied classifier to
//           interpret self-reported correctness.
//
//           The program should produce the following data:
//

// represents the result of a study session:
// how many questions were originally in the deck,
// how many total attempts were required to get
// them all correct!
data class StudyDeckResult(val numQuestions: Int, val numAttempts: Int)

// Data Class that stores values such as deck, attempts taken, and the classifer used in the program
data class StudyState(val deck: IDeck, val attempts: Int, val classifierUsed: PositivityClassifier)

val exampleStudyState1 = StudyState(exampleTFCList1, 0, ::isPositiveML)
val exampleStudyState2 = StudyState(exampleTFCList2, 0, ::isPositiveML)
val exampleEmptyStudyState = StudyState(exampleOfEmptyTFCList, 0, ::isPositiveML)

//           Look back to the process you followed for studyDeck in
//           part 1 of the project: you'll first want to design a
//           state type, then build the main reactConsole function,
//           and finally design all the handlers (and don't forget
//           to test ALL functions, including the program!).
//
//           In case it helps, here's a trace of a short example
//           study session (using the simple classifier), with
//           notes indicated by "<--"
//
//           What is the capital of Massachusetts, USA?
//           Think of the result? Press enter to continue
//                               <-- user just pressed enter, so ""
//           Boston
//           Correct? (Y)es/(N)o
//           yup
//           What is the capital of California, USA?
//           Think of the result? Press enter to continue
//
//           Sacramento
//           Correct? (Y)es/(N)o
//           no :(                     <-- cycles Cali to the back!
//           What is the capital of the United Kingdom?
//           Think of the result? Press enter to continue
//
//           London
//           Correct? (Y)es/(N)o
//           YES!
//           What is the capital of California, USA?
//           Think of the result? Press enter to continue
//
//           Sacramento
//           Correct? (Y)es/(N)o
//           yessir!
//           Questions: 3, Attempts: 4 <-- useful summary of return
//

// Some useful prompts
val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

// function that calls react console to loop through deck of cards and produce a number for attempts taken to get through the entire deck
fun studyDeck2(
    deck: IDeck,
    classifier: PositivityClassifier,
): StudyDeckResult {
    val deckSize: Int = deck.getSize()

    val results =
        StudyDeckResult(
            deck.getSize(),
            reactConsole(
                initialState = StudyState(deck, 0, classifier),
                stateToText = ::showFace,
                nextState = ::determineNext,
                isTerminalState = ::isDone,
                terminalStateToText = { StudyState -> finalText(StudyState, deckSize) },
            ).attempts,
        )

    return results
}

// function that tests function: studyDeck2
@EnabledTest
fun testStudyDeck2() {
    // makes a captureResults-friendly function :
    fun helpTest(
        deck: IDeck,
        classifier: PositivityClassifier,
    ): () -> StudyDeckResult {
        fun showStudyDeck(): StudyDeckResult {
            return studyDeck2(deck, classifier)
        }
        return ::showStudyDeck
    }

    testSame(
        captureResults(
            helpTest(exampleTFCList1, ::isPositiveML),
            "",
            "Yes",
            "",
            "Yes",
            "",
            "Yes",
        ),
        CapturedResult(
            StudyDeckResult(3, 3),
            "What is the capital of Australia? ",
            "$studyThink",
            "Canberra ",
            "$studyCheck",
            "What is the capital of England? ",
            "$studyThink",
            "London ",
            "$studyCheck",
            "What is the capital of USA? ",
            "$studyThink",
            "Washington ",
            "$studyCheck",
            "",
            "Questions: 3, Attempts: 3",
        ),
        "Test 1",
    )

    testSame(
        captureResults(
            helpTest(exampleTFCList1, ::isPositiveML),
            "",
            "Yes",
            "",
            "No",
            "",
            "Yes",
            "",
            "Yes",
        ),
        CapturedResult(
            StudyDeckResult(3, 4),
            "What is the capital of Australia? ",
            "$studyThink",
            "Canberra ",
            "$studyCheck",
            "What is the capital of England? ",
            "$studyThink",
            "London ",
            "$studyCheck",
            "What is the capital of USA? ",
            "$studyThink",
            "Washington ",
            "$studyCheck",
            "What is the capital of England? ",
            "$studyThink",
            "London ",
            "$studyCheck",
            "",
            "Questions: 3, Attempts: 4",
        ),
        "Test 2",
    )
}

// function that shows the front or back of the card depending on the state of the card, else if Exhausted, an empty string is produced
fun showFace(state: StudyState): String {
    return when (state.deck.getState()) {
        DeckState.QUESTION -> "${(state.deck.getText())} \n$studyThink"
        DeckState.ANSWER -> "${(state.deck.getText())} \n$studyCheck"
        DeckState.EXHAUSTED -> ""
    }
}

// function that tests function: showFace
@EnabledTest
fun testShowFace() {
    testSame(
        showFace(exampleStudyState1),
        "What is the capital of Australia? \n$studyThink",
        "Test 1 Show Front",
    )

    testSame(
        showFace(exampleStudyState2),
        "Canberra \n$studyCheck",
        "Test 2 Show Back",
    )

    testSame(
        showFace(exampleEmptyStudyState),
        "",
        "Test 3 Null",
    )
}

// function that determines the next deck Item to be called, depending on if answer provided is true or false,
// or if the question is currently be shown, where then the answer will be shown next
fun determineNext(
    state: StudyState,
    userInput: String,
): StudyState {
    return when (state.deck.getState()) {
        DeckState.QUESTION -> StudyState(state.deck.flip(), state.attempts, state.classifierUsed)

        DeckState.ANSWER ->
            if (state.classifierUsed(userInput) == true) {
                StudyState(state.deck.next(true), state.attempts + 1, state.classifierUsed)
            } else {
                StudyState(state.deck.next(false), state.attempts + 1, state.classifierUsed)
            }

        DeckState.EXHAUSTED -> StudyState(state.deck.next(false), state.attempts, state.classifierUsed)
    }
}

// function that checks if the entire deck is Exhausted
fun isDone(studyState: StudyState): Boolean {
    return (studyState.deck.getState() == DeckState.EXHAUSTED)
}

// function to test function: isDone
@EnabledTest
fun testIsDone() {
    testSame(
        isDone(exampleStudyState1),
        false,
        "Test 1: Not Exhausted",
    )

    testSame(
        isDone(exampleEmptyStudyState),
        true,
        "Test 2: Exhausted",
    )
}

// function that returns final string showing the questions in the deck, and the number of attempts taken to complete the entire deck
fun finalText(
    state: StudyState,
    deckSize: Int,
): String {
    return "\nQuestions: $deckSize, Attempts: ${state.attempts}"
}

// TODO 2/2: Finally, design the program study2 that...
//           a) Uses chooseMenuOption to select from amongst a
//              list of decks; the options must include at least
//              one deck read from a file (using
//              readTaggedFlashCardsFile), one generated by code
//              (using PerfectSquaresDeck), and one that filters
//              based upon a tag being present (e.g., only
//              "hard" cards from a list; this may be the cards
//              read from a file).
//           b) If the menu in (a) didn't result in quitting, then
//              uses chooseMenuOption again to select from amongst
//              the two sentiment analysis functions.
//           c) If the menu in (b) didn't result in quitting, then
//              uses studyDeck2 to study through the selected deck
//              with the selected sentiment analysis function.
//           d) Returns to (a) and continues until either of the
//              two menus indicate a desire to quit.
//
//           Make sure to provide tests that capture (at least)...
//           - Quitting at the selection of decks
//           - Quitting at the selection of sentiment analysis
//             functions
//           - Studying through at least one deck
//

// some useful labels
val optSimple = "Simple Self-Report Evaluation"
val optML = "ML Self-Report Evaluation"

val testCard = TaggedFlashCard("What is the Capital of China?", "Beijing", listOf("Hard"))
val testCard2 = TaggedFlashCard("What is the Capital of Taiwan?", "Taipei", listOf("Medium"))

// function that filters out all cards in a deck tagged with "Hard"
fun hardOnly(card: TaggedFlashCard): Boolean {
    if (card.difficulty.contains("Hard")) {
        return true
    } else {
        return false
    }
}

// function that tests function: hardOnly
@EnabledTest
fun testHardOnly() {
    testSame(
        hardOnly(testCard),
        true,
        "Test 1",
    )

    testSame(
        hardOnly(testCard2),
        false,
        "Test 2",
    )
}

// function that calls studyDeck2 function
fun study2() {
    val fileDeck = readTaggedFlashCardsFile("example_tagged.txt")
    val generatedPerfectSquaresDeck = PerfectSquaresDeck(5, true)
    val filteredHardDeck = fileDeck.filter(::hardOnly)

    // example NameMenu Options combined with previously created decks and analysis sentiments
    val deck1ToUse = NamedMenuOption(exampleTFCList1, "TFC List Deck: Capitals Deck")
    val deck2ToUse = NamedMenuOption(generatedPerfectSquaresDeck, "Perfect Square Deck")
    val deck3ToUse = NamedMenuOption(TFCListDeck(filteredHardDeck, true), "TFC List Deck: Captials Deck (Hard Only)")

    val analysis1 = NamedMenuOption(::isPositiveML, "Positive Sentiment")
    val analysis2 = NamedMenuOption(::isPositiveSimple, "Simple Sentiment")

    val deckList = listOf(deck1ToUse, deck2ToUse, deck3ToUse)
    val sentimentAnalysis = listOf(analysis1, analysis2)

    // function that checks if null a quit has occured at any point
    fun selection(): Boolean {
        val deckChosen = chooseMenuOption(deckList)
        if (deckChosen == null) {
            return false
        }

        val sentimentChosen = chooseMenuOption(sentimentAnalysis)

        if (sentimentChosen == null) {
            return false
        }

        studyDeck2(deckChosen.option, sentimentChosen.option)
        return true
    }

    while (selection()) {
    }
}

// -----------------------------------------------------------------

fun main() {
    study2()
}

runEnabledTests(this)
main()
