import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Application extends App {

  implicit val system: ActorSystem = ActorSystem("akka-http-cache")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val config = ConfigFactory.load()

  val defaultCachingSettings = CachingSettings(system)
  val lfuCacheSettings =
    defaultCachingSettings.lfuCacheSettings
      .withInitialCapacity(config.getInt("cache.initialCapacity"))
      .withMaxCapacity(config.getInt("cache.maxCapacity"))

  val cachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)

  val cache: Cache[Long, BigInt] = LfuCache(cachingSettings)
  val route = new Factorial(cache).routes


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
