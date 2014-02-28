import mikera.cljutils.Clojure
import com.aphyr.riemann.client.RiemannClient

object RunnerMultiJvmRiemannServer extends App {
  Clojure.require("riemann.bin")
  Clojure.eval("""(riemann.bin/-main "./etc/riemann.config")""")

  Thread.sleep(5000) // Give some time to client to interact with `riemann`

  Clojure.eval("""(riemann.core/stop! @riemann.config/core)""")

  println("I am dead, Jim...")
  sys.exit(0)
}

object RunnerMultiJvmRiemannClient extends App {
  Thread.sleep(5000) // Give some time to `riemann-server` to warm-up

  println("Let's interact with `riemann`")

  val client = RiemannClient.tcp("localhost", 5555)
  client.connect()
  client.event()
    .service("fridge")
    .state("running")
    .metric(5.123)
    .tags("appliance", "cold")
    .send()

  val result = client.query("tagged \"cold\" and metric > 0")

  client.disconnect()

  println(result)
}
