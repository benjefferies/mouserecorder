import com.google.gson.Gson
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import java.awt.AWTException
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

@Throws(NativeHookException::class, InterruptedException::class, AWTException::class, IOException::class)
fun main(args: Array<String>) {
    val path = Paths.get(System.getProperty("user.home"), "Documents")
    val recording = Files.createTempFile(path, "mouserecording", ".json")
    println("Starting recorder saving records to ${recording.toAbsolutePath().toAbsolutePath()}")
    requestUserinput()
    setupLogger()
    val mouseListener = MouseListener()
    addShutdownHook(mouseListener, recording)
}

private fun requestUserinput() {
    println("Press enter to start recording")
    System.`in`.read()
    println("Recording starts in 3 seconds")
    TimeUnit.SECONDS.sleep(3)
}

private fun setupLogger() {
    val logger = Logger.getLogger(GlobalScreen::class.java.`package`.name)
    logger.level = Level.WARNING
}

private fun addShutdownHook(mouseListener: MouseListener, recording: Path) {
    Runtime.getRuntime().addShutdownHook(Thread {
        val events = mouseListener.getEvents()
        val batchedEvents = events.dropLast(1)
                .withIndex()
                .groupBy { it.index / 10 }
                .map { it.value.map { it.value } }
        val eventJson = Gson().toJson(batchedEvents)
        try {
            FileWriter(recording.toFile()).use { writer ->
                writer.write(eventJson)
            }
        } catch (e: IOException) {
            println(e.localizedMessage)
            println("Failed to store events to file\n${eventJson}")
        }
    })
}