package me.kocproz.conveyor

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.kocproz.conveyor.data.ConfigFile
import java.io.File

/**
 * Loads the configuration from the file
 * Checks the configuration for errors
 */
object ConfigurationLoader {

    private val objectMapper = ObjectMapper(YAMLFactory())
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun loadConfiguration(): Configuration {
        val configFile = File(Arguments.configFile)
        if (!configFile.exists()) throw IllegalArgumentException("Configuration file does not exist")
        val config = objectMapper.readValue(configFile, ConfigFile::class.java)
        validateConfiguration(config)
        return Configuration(config)
    }

    /**
     * Things to validate:
     * - No repeating identifiers
     * - No references to non-existent devices
     * - Correct device classes
     */
    private fun validateConfiguration(configFile: ConfigFile) {
        val identifiers = mutableSetOf<String>()
        val deviceRefs = mutableSetOf<String>()
        configFile.devices.forEach { (deviceRef, deviceDefinition) ->
            if (identifiers.contains(deviceDefinition.identifier)) {
                throw IllegalArgumentException("Device identifier ${deviceDefinition.identifier} is not unique")
            }
            identifiers.add(deviceDefinition.identifier)
            deviceRefs.add(deviceRef)
        }
        configFile.sensors.forEach { (_, sensorDefinition) ->
            if (!deviceRefs.contains(sensorDefinition.deviceRef)) {
                throw IllegalArgumentException("Sensor ${sensorDefinition.name} references non-existent device ${sensorDefinition.deviceRef}")
            }
        }
        configFile.binarySensors.forEach { (_, binarySensorDefinition) ->
            if (!deviceRefs.contains(binarySensorDefinition.deviceRef)) {
                throw IllegalArgumentException("Binary sensor ${binarySensorDefinition.name} references non-existent device ${binarySensorDefinition.deviceRef}")
            }
        }
    }

}
