package app

import de.groovybyte.gamejam.styxreactor.StyxReactorServer
import io.jooby.JoobyTest
import io.jooby.StatusCode
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.assertEquals

@JoobyTest(StyxReactorServer::class)
class IntegrationTest {

  companion object {
    val client = OkHttpClient()
  }

  @Test
  fun shouldSayHi(serverPort: Int) {
    val req = Request.Builder()
        .url("http://localhost:$serverPort")
        .build()

    client.newCall(req).execute().use { rsp ->
      assertEquals("Welcome to Jooby!", rsp.body!!.string())
      assertEquals(StatusCode.OK.value(), rsp.code)
    }
  }
}
