package de.groovybyte.gamejam.styxreactor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.groovybyte.gamejam.styxreactor.datatransfer.GameController
import de.groovybyte.gamejam.styxreactor.datatransfer.Message
import de.groovybyte.gamejam.styxreactor.fixtures.WORLD_1
import de.groovybyte.gamejam.styxreactor.utils.handler.DefaultHeadersHandler
import de.groovybyte.gamejam.styxreactor.utils.handler.TraceIdHandler
import io.jooby.Kooby
import io.jooby.MediaType
import io.jooby.json.JacksonModule
import io.jooby.runApp

const val APP_TITLE = "StyxReactor-Server"

fun main(args: Array<String>) {
    runApp(args, StyxReactorServer::class)
}

class StyxReactorServer : Kooby({
    name = APP_TITLE
    serverOptions {
        port = config.getInt("port")
        gzip = true
        ioThreads = 8
        workerThreads = 2 * ioThreads
        maxRequestSize = 16384
    }

    install(JacksonModule())

    //    decorator(AccessLogHandler())
    before(TraceIdHandler())
    before(DefaultHeadersHandler(
        "Server" to APP_TITLE //"CERN httpd/3.0A"
    ))
    assets("/", "www/index.html")

    val playerController = GameController(log)
    ws("/ws", playerController)
        .consumes(MediaType.json)
        .produces(MediaType.json)
})
