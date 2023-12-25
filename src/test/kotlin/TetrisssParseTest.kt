import com.lab3.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TetrisssParseTest {

    @Test
    fun testParseGameFieldClearField() {
        /* input     3 4    expected input     3 4
                    ....                      ....
                    .p..                      .p..
                    #...                      #...
        */
        val inputStr = "3 4\n....\n.p..\n#...\n"
        val expectedGameField = GameField(4, 3, mutableSetOf(PinFigure(1, 1), PinBlock(0, 2)))

        val actualGameField = parseGameField(inputStr)

        assertEquals(expectedGameField, actualGameField)
    }

    @Test
    fun testParseGameFieldCollide() {
        /* input    .p..    expected input    .p..
                    ....                      ....
                    .#..                      .#..
        */
        val inputStr = "3 4\n.p..\n....\n.#..\n"
        val expectedGameField = GameField(4, 3, mutableSetOf(PinFigure(1, 0), PinBlock(1, 2)))

        val actualGameField = parseGameField(inputStr)

        assertEquals(expectedGameField, actualGameField)
    }

    @Test
    fun testReturnGameFieldClearField() {
        /* input    ....    output    ....
                    .p..              ....
                    #...              #...
                    ....              .p..
        */
        val gameField = GameField(4, 4, mutableSetOf(PinFigure(1, 1), PinBlock(0, 2)))
        val expectedOutput = "....\n....\n#...\n.p..\n"

        val actualOutput = returnGameField(gameField)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testReturnGameFieldEmpty() {
        /* input    ....    output    ....
                    ....              ....
                    ....              ....
                    ....              ....
        */
        val gameField = GameField(4, 4, mutableSetOf())
        val expectedOutput = "....\n....\n....\n....\n"

        val actualOutput = returnGameField(gameField)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testReturnGameFieldCollide() {
        /* input    .p..    output    ....
                    ....              ....
                    ....              .p..
                    .#..              .#..
        */
        val gameField = GameField(4, 4, mutableSetOf(PinFigure(1, 0), PinBlock(1, 3)))
        val expectedOutput = "....\n....\n.p..\n.#..\n"

        val actualOutput = returnGameField(gameField)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testReturnGameFieldBetweenTowers() {
        /* input    p.p.p    output    .....
                    .....              .....
                    .....              .....
                    .....              .....
                    .#.#.              p#p#p

         */
        val gameField = GameField(5, 5, mutableSetOf(PinFigure(2, 0), PinFigure(0, 0), PinFigure(4, 0), PinBlock(1, 4),PinBlock(3, 4)))
        val expectedOutput = ".....\n.....\n.....\n.....\np#p#p\n"

        val actualOutput = returnGameField(gameField)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testReturnGameFieldBySteps(){
        /* input               output  STEP 0           STEP4
                    p.p.p              p.p.p            .....
                    .....              .....            .....
                    .....              .....    ....    .....
                    .....              .....            .....
                    .#.#.              .#.#.            p#p#p

         */
        val gameField = GameField(5, 5, mutableSetOf(PinFigure(2, 0), PinFigure(0, 0), PinFigure(4, 0), PinBlock(1, 4),PinBlock(3, 4)))
        val expectedOutput =    "STEP 0\np.p.p\n.....\n.....\n.....\n.#.#.\n\n" +
                                "STEP 1\n.....\np.p.p\n.....\n.....\n.#.#.\n\n" +
                                "STEP 2\n.....\n.....\np.p.p\n.....\n.#.#.\n\n" +
                                "STEP 3\n.....\n.....\n.....\np.p.p\n.#.#.\n\n" +
                                "STEP 4\n.....\n.....\n.....\n.....\np#p#p\n\n"

        val actualOutput = returnGameFieldBySteps(gameField)

        assertEquals(expectedOutput, actualOutput)
    }
}