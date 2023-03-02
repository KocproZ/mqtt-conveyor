package me.kocproz.conveyor

import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence

class MqttConnection(
    uri: String,
    username: String,
    password: String
) : AutoCloseable, (MqttClient.() -> Unit) -> Unit {

    private val client = MqttClient(uri, "conveyor-${System.getenv("COMPUTERNAME")}", MemoryPersistence())

    init {
        val options = MqttConnectionOptions()
        options.isCleanStart = true
        options.isAutomaticReconnect = true
        options.userName = username
        options.password = password.toByteArray()
//        client.setCallback() TODO implement callback
        client.connect(options)
    }

    companion object {
        const val WAIT_ON_CLOSE = 5_000L
    }

    override fun invoke(action: MqttClient.() -> Unit) = client.action()

    fun publish(topic: String, payload: String, qos: Int = 0, retained: Boolean = false) =
        client.publish(topic, payload.toByteArray(), qos, retained)

    override fun close() {
        client.disconnect(WAIT_ON_CLOSE)
        client.close()
    }

}
