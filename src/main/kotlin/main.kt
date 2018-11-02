import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import service.FlywayService

val DB_USER = "DB_USER"
val DB_PASSWORD = "DB_PASSWORD"
val DB_URL = "DB_URL"
val ENV = "ENV"
val LOCAL = "LOCAL"


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
