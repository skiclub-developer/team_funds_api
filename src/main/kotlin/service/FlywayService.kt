package service

import DB_PASSWORD
import DB_URL
import DB_USER
import ENV
import LOCAL
import org.flywaydb.core.Flyway

class FlywayService private constructor() {
    private object Holder {
        val OBJECT = if (System.getProperty(ENV) != null && System.getProperty(ENV).equals(LOCAL)) {
            Flyway
                    .configure()
                    .dataSource(System.getProperty(DB_URL),
                            System.getProperty(DB_USER),
                            System.getProperty(DB_PASSWORD)
                    )
                    .load()
        } else {
            Flyway.configure()
                    .dataSource(System.getenv(DB_URL),
                            System.getenv(DB_USER),
                            System.getenv(DB_PASSWORD)
                    )
                    .load()
        }
    }

    companion object {
        val instance: Flyway by lazy { Holder.OBJECT }
    }
}