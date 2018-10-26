package service

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.lang.Exception
import java.sql.DriverManager

class Jooq private constructor() {

    private object Holder {
        val userName = "root"
        val password = "root"
        val url = "jdbc:mysql://localhost:8889/team_funds?serverTimezone=UTC"
        val connection = try {
            DriverManager.getConnection(url, userName, password)
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