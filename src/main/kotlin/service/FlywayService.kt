package service

import DB_PASSWORD
import DB_URL
import DB_USER
import org.flywaydb.core.Flyway

class FlywayService private constructor() {
    private object Holder {
        val OBJECT = Flyway.configure()
                .dataSource(System.getenv(DB_URL),
                        System.getenv(DB_USER),
                        System.getenv(DB_PASSWORD)
                )
                .load()
    }

    companion object {
        val instance: Flyway by lazy { Holder.OBJECT }
    }
}