package de.groovybyte.gamejam.styxreactor.utils.handler

import io.jooby.Context
import io.jooby.Route

class DefaultHeadersHandler(
    private vararg val headers: Pair<String, String>
) : Route.Before {
    override fun apply(ctx: Context) {
        headers.forEach { (header, value) ->
            ctx.setResponseHeader(header, value)
        }
    }
}
