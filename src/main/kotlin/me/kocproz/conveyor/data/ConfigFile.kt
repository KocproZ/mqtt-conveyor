package me.kocproz.conveyor.data

import com.fasterxml.jackson.annotation.JsonAlias

typealias ConfigEntry = Pair<String, Any>

data class ConfigFile(
    val devices: Map<String, DeviceDefinition>,
    val sensors: Map<String, SensorDefinition>,
    @JsonAlias("binary_sensors")
    val binarySensors: Map<String, BinarySensorDefinition>,
)

data class SensorDefinition(
    val name: String,
    @JsonAlias("device_class")
    val deviceClass: String,
    @JsonAlias("unit_of_measurement")
    val unitOfMeasurement: String?,
    val deviceRef: String,
    val icon: String?,
    val exec: String
)

data class BinarySensorDefinition(
    val name: String,
    @JsonAlias("device_class")
    val deviceClass: String,
    val deviceRef: String,
    val icon: String?,
    val exec: String
)

data class DeviceDefinition(
    val name: String,
    val manufacturer: String,
    val model: String,
    val identifier: String,
    @field:JsonAlias("configuration_url")
    val configurationUrl: String?
)
