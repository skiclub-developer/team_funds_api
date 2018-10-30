import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.records.PenaltiesRecord
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.Privacy
import service.PenaltyService
import service.UserPenaltyService
import service.UserService

val BOT_TOKEN = "756737895:AAG1P4tnW6PRnrAI5wVkzr-zvHkLFTcyh54"
val BOT_USERNAME = "mannschaftskasse_skiclub_bot"

class TeamFundsBot : AbilityBot(BOT_TOKEN, BOT_USERNAME) {
    override fun creatorId() = 704551541

    private fun getPlayersAndAmount(message: String): List<PlayerAmountModel> {
        val result = mutableListOf<PlayerAmountModel>()
        val players = message.split(",")
        players.forEach {
            var counter = 1;
            while (it.substring(it.length - counter, it.length - counter + 1).single().isDigit()) {
                counter++
            }
            counter--
            val amount = it.substring(it.length - counter).toInt();

            val name = it.substring(0, it.length - counter)
            result.add(PlayerAmountModel(name, amount))
        }

        return result
    }

    fun listAllPlayers(): Ability {
        return Ability
                .builder()
                .name("zeigespieler")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action {
                    val players = UserService.instance.getAll()
                    var playerNames = ""
                    players.forEach {
                        playerNames = playerNames + it.name + ","
                    }
                    silent.send(playerNames, it.chatId())
                }
                .build()
    }

    fun openPenalties(): Ability {
        return Ability
                .builder()
                .name("offen")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action { messageContext ->
                    val listOfTransactions = mutableListOf<String>()
                    val players = messageContext.firstArg().split(",")
                    players.forEach {
                        val userRecord = UserService.instance.getByName(it)
                        if (userRecord != null) {
                            listOfTransactions.add("${userRecord.name}s Strafen belaufen sich auf ${userRecord.currentPenalties}€" +
                                    " und er muss noch ${userRecord.caseOfBeer} Kisten schmeißen!")
                        } else {
                            listOfTransactions.add("${it} befindet sich nicht in unserer Datenbank")
                        }
                    }
                    listOfTransactions.forEach { silent.send(it, messageContext.chatId()) }
                }
                .build()
    }

    fun payCrateOfBeer(): Ability {
        return Ability
                .builder()
                .name("bezahlenkiste")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action {messageContext ->
                    val playerAmounts = getPlayersAndAmount(messageContext.firstArg())
                    val listOfTransactions = mutableListOf<String>()
                    playerAmounts.forEach { (name, amount) ->
                        listOfTransactions.add(" ${name} hat ${amount} Kisten geschmissen!")
                        UserService.instance.pay(name, amount, Users.USERS.CASE_OF_BEER)
                    }
                    listOfTransactions.forEach { silent.send(it, messageContext.chatId()) }
                }
                .build()
    }

    fun pay(): Ability {
        return Ability
                .builder()
                .name("bezahlen")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action { messageContext ->
                    val playerAmounts = getPlayersAndAmount(messageContext.firstArg())
                    val listOfTransactions = mutableListOf<String>()
                    playerAmounts.forEach { (name, amount) ->
                        listOfTransactions.add("${name} hat ${amount}€ bezahlt!")
                        UserService.instance.pay(name, amount, Users.USERS.CURRENT_PENALTIES)
                    }
                    listOfTransactions.forEach { silent.send(it, messageContext.chatId()) }
                }
                .build()
    }

    fun addPenalty(): List<Ability> {
        val abilities = mutableListOf<Ability>()
        PenaltyService.instance.getAll().forEach { result ->
            val penaltyRecord = result.into(PenaltiesRecord::class.java)
            val ability = Ability
                    .builder()
                    .name(penaltyRecord.penaltyName.toLowerCase())
                    .locality(Locality.ALL)
                    .privacy(Privacy.PUBLIC)
                    .action { messageContext ->
                        val listOfTransactions = mutableListOf<String>()
                        val playerAmounts = getPlayersAndAmount(messageContext.firstArg())
                        playerAmounts.forEach { (name, amount) ->
                            val cost = amount * penaltyRecord.penaltyCost
                            val numberOfCasesOfBeer = amount * penaltyRecord.caseOfBeerCost
                            val userRecord = UserService.instance.getByName(name)
                            if (userRecord != null) {
                                listOfTransactions.add("Update Strafen für  ${userRecord.name}")
                                userRecord.currentPenalties = userRecord.currentPenalties + cost
                                userRecord.caseOfBeer = userRecord.caseOfBeer + numberOfCasesOfBeer
                                listOfTransactions.add("${name}s Strafen wurden um ${cost}€ und seine Kistenanzahl" +
                                        " um ${numberOfCasesOfBeer} erhöht")
                                UserService.instance.updateUser(name, userRecord)
                                val userPenaltyRecord = UserPenaltyService.instance.getUserPenalty(userRecord.id, penaltyRecord.id)
                                if (userPenaltyRecord != null) {
                                    UserPenaltyService.instance.updateUserPenaltyAmount(userPenaltyRecord, amount)
                                    listOfTransactions.add("${name} wurde die Strafe ${penaltyRecord.penaltyName}" +
                                            " ${amount} mal hinzugefügt")
                                } else {
                                    UserPenaltyService.instance.createUserPenalty(userRecord.id, penaltyRecord.id, amount)
                                    listOfTransactions.add("${name} wurde die Strafe ${penaltyRecord.penaltyName}" +
                                            "${amount} mal hinzugefügt")
                                }
                                listOfTransactions.add("${name}s Strafen belaufen sich auf ${userRecord.currentPenalties}€" +
                                        " und er muss noch ${userRecord.caseOfBeer} Kisten schmeißen!")
                            } else {
                                listOfTransactions.add("${name} befindet sich nicht in unserer Datenbank")
                            }
                            listOfTransactions.add("----------------------------------------------")
                        }
                        listOfTransactions.forEach { silent.send(it, messageContext.chatId()) }
                    }
                    .build()

            abilities.add(ability)
        }

        return abilities
    }
}

data class PlayerAmountModel constructor(val name: String, val amount: Int)