package de.groovybyte.gamejam.styxreactor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.groovybyte.gamejam.styxreactor.game.WebSocketAPI
import de.groovybyte.gamejam.styxreactor.framework.handler.DefaultHeadersHandler
import de.groovybyte.gamejam.styxreactor.framework.handler.TraceIdHandler
import io.jooby.Kooby
import io.jooby.MediaType
import io.jooby.json.JacksonModule
import io.jooby.require
import io.jooby.runApp

const val APP_TITLE = "StyxReactor-Server"

fun main(args: Array<String>) {
    runApp(args, StyxReactorServer::class)
}

class StyxReactorServer : Kooby({
    name = APP_TITLE
    serverOptions {
        port = config.getInt("port")
        host = config.getString("host")
        gzip = true
        ioThreads = 8
        workerThreads = 2 * ioThreads
        maxRequestSize = 16384
    }

    install(JacksonModule())
    val objectMapper = require<ObjectMapper>()
    objectMapper.registerKotlinModule()

    //    decorator(AccessLogHandler())
    before(TraceIdHandler())
    before(
        DefaultHeadersHandler(
            "Server" to APP_TITLE //"CERN httpd/3.0A"
        )
    )
    assets("/", "www/index.html")

    val playerController = WebSocketAPI(log, objectMapper)
    ws("/ws", playerController)
        .consumes(MediaType.json)
        .produces(MediaType.json)
})
