package config

import akka.actor.ActorSystem
import com.google.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

//@Singleton
class DatabaseExecutionContext @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "database.dispatcher")