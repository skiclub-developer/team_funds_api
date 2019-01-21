package services

import DB_PASSWORD
import DB_URL
import DB_USER
import org.flywaydb.core.Flyway

class FlywayService private constructor() {
    private object Holder {
        val URL = System.getenv(DB_URL) ?: System.getProperty(DB_URL)
        val USER = System.getenv(DB_USER) ?: System.getProperty(DB_USER)
        val PASSWORD = System.getenv(DB_PASSWORD) ?: System.getProperty(DB_PASSWORD)

        val OBJECT = Flyway.configure()
                .dataSource(
                        URL,
                        USER,
                        PASSWORD
                )
                .load()
    }

    companion object {
        val instance: Flyway by lazy { Holder.OBJECT }
    }
}