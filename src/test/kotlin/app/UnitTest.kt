package app

import de.groovybyte.gamejam.styxreactor.StyxReactorServer
import io.jooby.MockRouter
import io.jooby.StatusCode
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.assertEquals

class UnitTest {
  @Test
  fun welcome() {
    val router = MockRouter(StyxReactorServer())
    router.get("/") { rsp ->
      assertEquals("Welcome to Jooby!", rsp.value())
      assertEquals(StatusCode.OK, rsp.getStatusCode())
    }
  }
}
