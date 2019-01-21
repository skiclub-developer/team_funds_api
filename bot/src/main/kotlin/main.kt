//import org.telegram.telegrambots.ApiContextInitializer
//import org.telegram.telegrambots.meta.TelegramBotsApi
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException
//
//val BOT_TOKEN = "TEAM_FUNDS_BOT_TOKEN"
//val BOT_NAME = "TEAM_FUNDS_BOT_NAME"
//
//
//fun main() {
//    FlywayService.instance.migrate()
//
//    ApiContextInitializer.init()
//
//    val botsApi = TelegramBotsApi()
//
//    val botToken = System.getenv(BOT_TOKEN) ?: System.getProperty(BOT_TOKEN)
//    val botName = System.getenv(BOT_NAME) ?: System.getProperty(BOT_NAME)
//
//    try {
//        botsApi.registerBot(TeamFundsBot(botToken, botName))
//    } catch (e: TelegramApiException) {
//        e.printStackTrace()
//    }
//}
