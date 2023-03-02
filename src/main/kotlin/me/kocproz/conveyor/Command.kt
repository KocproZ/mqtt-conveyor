package me.kocproz.conveyor

import org.tinylog.kotlin.Logger
import java.util.concurrent.TimeUnit

class Command(
    command: String
) {

    private val template: ProcessBuilder

    init {
        template = ProcessBuilder()
            .command(command)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
        // TODO save errors to file?
    }

    // TODO should work on coroutines
    fun execute(): String {
        Logger.info("Executing command: ${template.command()}")
        val process = template.start()
        if (process.waitFor(5, TimeUnit.SECONDS)) {
            Logger.info("Command executed successfully")
        } else {
            process.destroyForcibly()
            Logger.warn("Command execution timed out")
            throw RuntimeException("Command execution timed out")
        }
        return process.inputStream.bufferedReader().use { it.readText() }
    }


}
