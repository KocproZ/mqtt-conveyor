package me.kocproz.conveyor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.kocproz.conveyor.util.measureTimeMillisAndPrintTime
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    measureTimeMillisAndPrintTime {
        Arguments.parse(args)
    }
    //TODO expand env variables in config file

    val connection = MqttConnection(
        System.getenv("MQTT_URL"),
        System.getenv("MQTT_USERNAME"),
        System.getenv("MQTT_PASSWORD")
    )

    val config = ConfigurationLoader.loadConfiguration()
    val registrationHandler = RegistrationHandler(connection, config, jacksonObjectMapper())
    registrationHandler.registerAll()

    Runtime.getRuntime().addShutdownHook(Thread {
        registrationHandler.unregisterAll()
    })


    exitProcess(0)
}
