package service

import DB_PASSWORD
import DB_URL
import DB_USER
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.EnumConverter
import java.sql.DriverManager

class Jooq private constructor() {

    private object Holder {
        val URL = System.getenv(DB_URL) ?: System.getProperty(DB_URL)
        val USER = System.getenv(DB_USER) ?: System.getProperty(DB_USER)
        val PASSWORD = System.getenv(DB_PASSWORD) ?: System.getProperty(DB_PASSWORD)

        val connection = try {
            DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        val CREATE = DSL.using(connection, SQLDialect.POSTGRES_10)
    }

    companion object {
        val instance: DSLContext by lazy { Holder.CREATE }
    }
}

enum class UserType {
    PLAYER,
    COACH,
    ADVISOR
}

class UserTypeConverter : EnumConverter<String, UserType>(String::class.java, UserType::class.java)