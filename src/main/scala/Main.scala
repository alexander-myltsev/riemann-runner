import clojure.lang.Symbol
import clojure.lang.RT
import com.aphyr.riemann.client.RiemannClient
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Main extends App {
  private[this] val blockingIOThreadPool = Executors.newFixedThreadPool(1)
  private[this] val blockingIOEC = ExecutionContext.fromExecutorService(blockingIOThreadPool)

  Future {
    def invokeClojure(namespace: String, name: String, arg: Object) = RT.`var`(namespace, name).invoke(arg)
    invokeClojure(namespace = "clojure.core", name = "require", arg = Symbol.intern("riemann.bin"))
    invokeClojure(namespace = "riemann.bin", name = "-main", arg = "./etc/riemann.config")
  }(blockingIOEC)

  Thread.sleep(5000)

  val client = RiemannClient.tcp("localhost", 5555)
  client.connect()
  client.event().
    service("fridge").
    state("running").
    metric(5.123).
    tags("appliance", "cold").
    send()

  val result = client.query("tagged \"cold\" and metric > 0")
  client.disconnect()

  println(result)
}
