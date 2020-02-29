package de.groovybyte.gamejam.styxreactor

import de.groovybyte.gamejam.styxreactor.styxreactor.PlayerController
import de.groovybyte.gamejam.styxreactor.utils.handler.DefaultHeadersHandler
import de.groovybyte.gamejam.styxreactor.utils.handler.TraceIdHandler
import io.jooby.Kooby
import io.jooby.json.JacksonModule
import io.jooby.runApp

const val APP_TITLE = "StyxReactor-Server"

class StyxReactorServer : Kooby({
    name = APP_TITLE
    serverOptions {
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

    val playerController = PlayerController(log)
    ws("/ws", playerController)
})

fun main(args: Array<String>) {
    runApp(args, StyxReactorServer::class)
}
