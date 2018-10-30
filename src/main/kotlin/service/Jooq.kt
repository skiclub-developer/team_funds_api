package service

import DB_PASSWORD
import DB_URL
import DB_USER
import ENV
import LOCAL
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.DriverManager

class Jooq private constructor() {

    private object Holder {

        val userName = System.getenv("db_user")
        val password = System.getenv("db_password")
        val url = System.getenv("db_url")
        //        val url = "jdbc:mysql://db:3306/team_funds?serverTimezone=UTC"
        val connection = try {
            if (System.getProperty(ENV).equals(LOCAL)) {
                DriverManager.getConnection(
                        System.getProperty(DB_URL),
                        System.getProperty(DB_USER),
                        System.getProperty(DB_PASSWORD)
                )
            } else {
                DriverManager.getConnection(
                        System.getenv(DB_URL),
                        System.getenv(DB_USER),
                        System.getenv(DB_PASSWORD)
                )
            }
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