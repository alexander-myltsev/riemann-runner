import com.aphyr.riemann.client.RiemannClient
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Main extends App {
  private[this] val blockingIOThreadPool = Executors.newFixedThreadPool(1)
  private[this] val blockingIOEC = ExecutionContext.fromExecutorService(blockingIOThreadPool)

  Future {
    riemann.bin_runner.main(Array("./etc/riemann.config"))
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
