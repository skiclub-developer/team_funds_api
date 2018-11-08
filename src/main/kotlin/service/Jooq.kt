package service

import DB_PASSWORD
import DB_URL
import DB_USER
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.DriverManager

class Jooq private constructor() {

    private object Holder {
        val connection = try {
            DriverManager.getConnection(
                    System.getenv(DB_URL),
                    System.getenv(DB_USER),
                    System.getenv(DB_PASSWORD)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        val CREATE = DSL.using(connection, SQLDialect.MYSQL)
    }

    companion object {
        val instance: DSLContext by lazy { Holder.CREATE }
    }
}