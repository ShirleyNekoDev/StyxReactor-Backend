package de.groovybyte.gamejam.styxreactor.datatransfer.client2server

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import de.groovybyte.gamejam.styxreactor.datatransfer.GameSession
import de.groovybyte.gamejam.styxreactor.datatransfer.exceptions.InvalidPayloadException
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController

class ClientMessage(
    val ctx: WebSocketController<GameSession>.ClientContext,
    val packetData: String,
    val objectMapper: ObjectMapper,
    val json: JsonNode
) {
    val command by lazy { json["command"].asText() }

    companion object {
        fun parse(
            ctx: WebSocketController<GameSession>.ClientContext,
            objectMapper: ObjectMapper,
            packetData: String
        ) = ClientMessage(
            ctx,
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
