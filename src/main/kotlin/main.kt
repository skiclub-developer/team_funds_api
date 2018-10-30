import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import service.FlywayService

val DB_USER = "db_user"
val DB_PASSWORD = "db_password"
val DB_URL = "db_url"
val ENV = "env"
val LOCAL = "local"

fun main() {
    FlywayService.instance.migrate()

    ApiContextInitializer.init()

    val botsApi = TelegramBotsApi()

    try {
        botsApi.registerBot(TeamFundsBot())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}
