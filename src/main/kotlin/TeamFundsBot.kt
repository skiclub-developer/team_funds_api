import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.records.PenaltiesRecord
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.Privacy
import service.*

class TeamFundsBot constructor(val envBotToken: String, val botName: String) : AbilityBot(envBotToken, botName) {
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

    fun listPaymentsOfPlayer(): Ability {
        return Ability
                .builder()
                .name("listeallerzahlungen")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action { messageContext ->
                    val playerName = messageContext.firstArg()
                    val player = UserService.instance.getByName(playerName)
                    if (player != null) {
                        val payments = UserPenaltyPaymentService.instance.getPaymentsForUser(player.id)
                        payments.forEach {
                            silent.send("${playerName} hat am ${it.paidAt} ${it.amount}€ gezahlt", messageContext.chatId())
                        }
                    } else {
                        silent.send("Der Spieler ${playerName} befindet sich nicht in unserer Datenbnak", messageContext.chatId())
                    }
                }
                .build()
    }

    fun listBeerPaymentsOfPlayer(): Ability {
        return Ability
                .builder()
                .name("listeallerbierzahlungen")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action { messageContext ->
                    val playerName = messageContext.firstArg()
                    val player = UserService.instance.getByName(playerName)
                    if (player != null) {
                        val payments = UserPenaltyBeerPaymentService.instance.getPaymentsForUser(player.id)
                        payments.forEach {
                            silent.send("${playerName} hat am ${it.paidAt} ${it.amount} Kiste(n) geschmissen", messageContext.chatId())
                        }
                    } else {
                        silent.send("Der Spieler ${playerName} befindet sich nicht in unserer Datenbnak", messageContext.chatId())
                    }
                }
                .build()
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
                        playerNames = playerNames + it.name + "\n"
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
                        val user = UserService.instance.getByName(name)
                        if (user != null) {
                            listOfTransactions.add(" ${name} hat ${amount} Kisten geschmissen!")
                            UserService.instance.pay(name, amount, Users.USERS.CASE_OF_BEER)
                            UserPenaltyBeerPaymentService.instance.pay(user.id, amount)
                        } else {
                            listOfTransactions.add("${name} konnte NICHT in der Datenbank gefunden werden. Bezahlung " +
                                    "erneut durchführen!!!")
                        }

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
                        val user = UserService.instance.getByName(name)
                        if (user != null) {
                            listOfTransactions.add("${name} hat ${amount}€ bezahlt!")
                            UserService.instance.pay(name, amount, Users.USERS.CURRENT_PENALTIES)
                            UserPenaltyPaymentService.instance.pay(user.id, amount)
                        } else {
                            listOfTransactions.add("${name} konnte NICHT in der Datenbank gefunden werden. Bezahlung " +
                                    "erneut durchführen!!!")
                        }
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