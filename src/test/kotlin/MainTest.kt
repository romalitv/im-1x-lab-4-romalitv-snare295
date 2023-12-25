import com.lab3.*
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter

internal class MainTest {
    private val errorMessagesCntr = ErrorMessages()
    val messages = mutableListOf<String>()
    private val outputMock: MainOutput = mockk()
//    private val outputMock: MainOutput = object : MainOutput {
//        override fun printLine(msg: String) {
//            messages.add(msg)
//        }
//    }

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
        every { outputMock.printLine(any()) } just Runs
    }

    @Test
    internal fun `it should show How-to-use instructions if there is no input file param`() {
        // Given
        val fsMock: IO = mockk()

//        val fsMock = object : IO {
//            override fun ifFileExist(filePath: String): Boolean {
//                throw RuntimeException("ifFileExist has been called unexpectedly")
//            }
//            override fun readFileAsString(filePath: String): String {
//                throw RuntimeException("readFileAsString has been called unexpectedly")
//            }
//            override fun writeOutputToFile(filePath: String, output: String) = Unit
//            override fun ifFileEmpty(filePath: String): Boolean = true
//        }

        // When
        mainHandler(emptyArray(), outputMock, fsMock)

        // Then
        verify(exactly = 1) { outputMock.printLine(errorMessagesCntr.noArgs) }

    }

    @Test
    internal fun `it should check if input file exists and if not then show a message`() {
        // Given
        val ioMock: IO = mockk()
        every { ioMock.ifFileExist(any()) } returns false
        // When
        mainHandler(arrayOf("input.txt"), outputMock, ioMock)

        // Then
//        assertEquals(1, messages.size)
//        assertEquals(errorMessagesCntr.inputFileDoesNotExist, messages[0])
        verify(exactly = 1) {outputMock.printLine(errorMessagesCntr.inputFileDoesNotExist)}
    }

    @Test
    internal fun `it should parse input file and throw an error if it is wrong`() {
        // Given
        val ioMock: IO = mockk()
        every { ioMock.ifFileExist(any()) } returns true
        every { ioMock.readFileAsString(any()) } returns "Wrong input file body"
        every { ioMock.ifFileEmpty(any()) } returns true
        // When
        mainHandler(arrayOf("input.txt"), outputMock, ioMock)

        // Then
//        assertEquals(1, messages.size)
//        assertEquals(errorMessagesCntr.inputFileContainsSmthWrong, messages[0])
        verify(exactly = 1){outputMock.printLine(errorMessagesCntr.inputFileContainsSmthWrong)}

    }
    @Test
    internal fun `it should print final field state`() {
        // Given
        val ioMock: IO = mockk()
        every { ioMock.ifFileExist(any()) } returns true
        every { ioMock.readFileAsString(any()) } returns "3 4\n.p.p\n....\n#.#."
        every { ioMock.writeOutputToFile(any(),any()) } just Runs
        every { ioMock.ifFileEmpty(any()) } returns true
//        val ioMock = object : IO {
//            override fun ifFileExist(filePath: String): Boolean = true
//            override fun readFileAsString(filePath: String): String =
//                "3 4\n" +
//                        ".p.p\n" +
//                        "....\n" +
//                        "#.#."
//            override fun writeOutputToFile(filePath: String, output: String) = Unit
//            override fun ifFileEmpty(filePath: String): Boolean = true
//
//        }

        // When
        mainHandler(arrayOf("input.txt"), outputMock, ioMock)

        // Then
//        assertEquals(1, messages.size)

        val finalBoard =
            "....\n" +
                    "....\n" +
                    "#p#p\n"
//        assertEquals(finalBoard , messages[0])
        verify(exactly = 1) { outputMock.printLine(finalBoard) }
    }

    @Test
    internal fun `it should write to file final field state`() {
        // Given
        val testFilePath : String = "src/test/resources/output.txt"

        File(testFilePath).writeText("")

        var outputFileString  = slot<String>()
        val ioMock: IO = mockk()
        every { ioMock.ifFileExist(any()) } returns true
        every { ioMock.readFileAsString(any()) } returns "3 4\n.p.p\n....\n#.#."
        every { ioMock.writeOutputToFile(any(),capture(outputFileString))} answers { File(testFilePath).writeText(outputFileString.captured) }
        every { ioMock.ifFileEmpty(any()) } returns true
//        val ioMock = object : IO {
//            override fun ifFileExist(filePath: String): Boolean = true
//            override fun readFileAsString(filePath: String): String =
//                "3 4\n" +
//                        ".p.p\n" +
//                        "....\n" +
//                        "#.#."
//            override fun writeOutputToFile(filePath: String, output: String) {
//                outputFileString = output
//            }
//            override fun ifFileEmpty(filePath: String): Boolean = true
//
//        }



        // When
        mainHandler(arrayOf("input.txt"), outputMock, ioMock)

        // Then
//        assertEquals(1, messages.size)

        val finalBoard = "....\n....\n#p#p\n"
        assertEquals(finalBoard , File(testFilePath).readText())

    }

    @Test
    internal fun `it should verify that file is empty`() {
        // Given
        val ioMock: IO = mockk()
        every { ioMock.ifFileExist(any()) } returns true
        every { ioMock.readFileAsString(any()) } returns "3 4\n.p.p\n....\n#.#."
        every { ioMock.ifFileEmpty(any()) } returns false
//        val ioMock = object : IO {
//            override fun ifFileExist(filePath: String): Boolean = true
//            override fun readFileAsString(filePath: String): String =
//                "3 4\n" +
//                        ".p.p\n" +
//                        "....\n" +
//                        "#.#."
//            override fun writeOutputToFile(filePath: String, output: String) {
//                outputFileString = output
//            }
//            override fun ifFileEmpty(filePath: String): Boolean = false
//        }

        // When
        mainHandler(arrayOf("input.txt"), outputMock, ioMock)

        // Then
        verify(exactly = 1) { outputMock.printLine(errorMessagesCntr.outpuFileIsntEmpty) }
//        assertEquals(1, messages.size)
//        assertEquals(messages[0] , errorMessagesCntr.outpuFileIsntEmpty)
    }
}