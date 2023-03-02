package me.kocproz.conveyor.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ConfigRequest(
    val name: String,
    @field:JsonProperty("device_class")
    val deviceClass: String,
    @field:JsonProperty("state_topic")
    val stateTopic: String,
    @field:JsonProperty("unit_of_measurement")
    val unitOfMeasurement: String?,
    val device: DeviceRequest,
    @field:JsonProperty("unique_id")
    val uniqueId: String
)

data class DeviceRequest(
    val name: String,
    val manufacturer: String,
    val model: String,
    val identifiers: List<String>,
)
