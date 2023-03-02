package me.kocproz.conveyor

import com.fasterxml.jackson.databind.ObjectMapper
import me.kocproz.conveyor.data.BinarySensorDefinition
import me.kocproz.conveyor.data.ConfigEntry
import me.kocproz.conveyor.data.SensorDefinition

class RegistrationHandler(
    private val connection: MqttConnection,
    private val configuration: Configuration,
    private val objectMapper: ObjectMapper
) {

    //TODO select registration path
    fun unregister(entityRecord: ConfigEntry) {
        when (entityRecord.second) {
            is SensorDefinition -> unregisterSensor(entityRecord)
            is BinarySensorDefinition -> unregisterBinarySensor(entityRecord)
        }
    }

    fun register(entityRecord: ConfigEntry) {
        when (entityRecord.second) {
            is SensorDefinition -> registerSensor(entityRecord)
            is BinarySensorDefinition -> registerBinarySensor(entityRecord)
        }
    }

    fun registerSensor(record: ConfigEntry) {
        connection.publish(
            topic = "homeassistant/sensor/${record.first}/config",
            payload = objectMapper.writeValueAsString(record.second),
            retained = true
        )
    }

    fun unregisterSensor(record: ConfigEntry) {
        connection.publish(
            topic = "homeassistant/sensor/${record.first}/config",
            payload = "",
            retained = true
        )
    }

    fun registerBinarySensor(record: ConfigEntry) {
        connection.publish(
            topic = "homeassistant/binary_sensor/${record.first}/config",
            payload = objectMapper.writeValueAsString(record.second),
            retained = true
        )
    }

    fun unregisterBinarySensor(record: ConfigEntry) {
        connection.publish(
            topic = "homeassistant/binary_sensor/${record.first}/config",
            payload = "",
            retained = true
        )
    }

    fun registerAll() {
        configuration.getEntities().forEach { register(it) }
    }

    fun unregisterAll() {
        configuration.getEntities().forEach { unregister(it) }
    }

}
