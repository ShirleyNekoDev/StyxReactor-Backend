package de.groovybyte.gamejam.styxreactor.utils.handler

import io.jooby.Context
import io.jooby.Route
import java.util.*

class TraceIdHandler : Route.Before {
    override fun apply(ctx: Context) {
        UUID.randomUUID().also { traceId ->
            ctx.attribute("Trace-ID", traceId)
            ctx.setResponseHeader("Trace-ID", traceId.toString())
        }
    }
}
