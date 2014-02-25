import com.aphyr.riemann.client.RiemannClient
import mikera.cljutils.Clojure

object Main extends App {
  Clojure.require("riemann.bin")
  Clojure.eval("""(riemann.bin/-main "./etc/riemann.config")""")

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

  Clojure.eval(
    """ (try
      |   (riemann.time/reset-tasks!)
      |   (riemann.config/clear!)
      |   (riemann.pubsub/sweep! (:pubsub @riemann.config/core))
      |   (riemann.config/stop!)
      |   nil
      |   (catch Exception e
      |     e))
    """.stripMargin)
}
