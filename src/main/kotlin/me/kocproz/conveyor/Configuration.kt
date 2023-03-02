package me.kocproz.conveyor

import me.kocproz.conveyor.data.ConfigEntry
import me.kocproz.conveyor.data.ConfigFile
import me.kocproz.conveyor.data.DeviceDefinition

class Configuration(
    private val configuration: ConfigFile
) {

    /**
     * Null asserted since we are sure that the device exists
     */
    fun getDevice(deviceRef: String): DeviceDefinition = configuration.devices[deviceRef]!!

    fun getEntities(): Iterable<ConfigEntry> =
        configuration.sensors.entries.map { ConfigEntry(it.key, it.value) } +
        configuration.binarySensors.entries.map { ConfigEntry(it.key, it.value) }

}
