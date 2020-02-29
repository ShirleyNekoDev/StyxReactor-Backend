package de.groovybyte.gamejam.styxreactor.game.datatransfer.client2server

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import de.groovybyte.gamejam.styxreactor.game.datatransfer.exceptions.InvalidPayloadException

class ClientMessage(
    val packetData: String,
    val objectMapper: ObjectMapper,
    val json: JsonNode
) {
    val command: String by lazy { json["command"].asText() }

    companion object {
        fun parse(
            objectMapper: ObjectMapper,
            packetData: String
        ) = ClientMessage(
            packetData,
            objectMapper,
            objectMapper.readTree(packetData)
        )
    }

    inline fun <reified T> payload(): T = try {
        json["payload"]?.let { objectMapper.treeToValue<T>(it) }!!
    } catch (ex: Exception) {
        when (ex) {
            is JsonMappingException,
            is NullPointerException ->
                throw InvalidPayloadException(json["payload"], ex)
            else -> throw ex
        }
    }
}
