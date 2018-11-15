import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.records.PenaltiesRecord
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import service.*


class TeamFundsBot constructor(val envBotToken: String, val botName: String) : AbilityBot(envBotToken, botName) {
    override fun creatorId() = 704551541

    fun addArbitraryPenalty(): Ability {
        return Ability
                .builder()
                .name("betragplus")
                .locality(Locality.GROUP)
                .privacy(Privacy.GROUP_ADMIN)
                .action { messageContext ->
                    val listOfTransactions = mutableListOf<String>()
                    messageContext.firstArg().getPlayersAndAmounts().forEach { (name, amount) ->
                        val player = UserService.instance.getByName(name)
                        if (player != null) {
                            UserService.instance.addPenalty(player.id, amount, Users.USERS.CURRENT_PENALTIES)
                            listOfTransactions.add("Spieler ${name} wurden ${amount}€ zu seinen Strafen hinzugefügt")
                        } else {
                            listOfTransactions.add("Spieler ${name} konnte NICHT in der Datenbank gefunden werden. " +
                                    "Bezahlung erneut durchführen!!!")
                        }
                    }
                    listOfTransactions.print(silent, messageContext.chatId())
                }

                .build()
    }

    override fun onUpdateReceived(update: Update?) {
        val test: KeyboardButton
    }

    fun addArbitraryBeerPenalty(): Ability {
        return Ability
                .builder()
                .name("kisteplus")
                .locality(Locality.GROUP)
                .privacy(Privacy.GROUP_ADMIN)
                .action { messageContext ->
                    val listOfTransactions = mutableListOf<String>()
                    messageContext.firstArg().getPlayersAndAmounts().forEach { (name, amount) ->
                        val player = UserService.instance.getByName(name)
                        if (player != null) {
                            UserService.instance.addPenalty(player.id, amount, Users.USERS.CASE_OF_BEER)
                            listOfTransactions.add("Spieler ${name} wurden ${amount} Kisten zu seinen Strafen hinzugefügt.")
                        } else {
                            listOfTransactions.add("Spieler ${name} konnte NICHT in der Datenbank gefunden werden. " +
                                    "Bezahlung erneut durchführen!!!")
                        }
                    }
                    listOfTransactions.print(silent, messageContext.chatId())
                }
                .build()
    }

    fun listPenaltiesOfPlayer(): Ability {
        return Ability
                .builder()
                .name("listeallerstrafen")
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action { messageContext ->
                    val listOfPenalties = mutableListOf<String>()
                    val playerName = messageContext.firstArg()
                    val player = UserService.instance.getByName(playerName)
                    if (player != null) {
                        val penalties = UserPenaltyService.instance.getPenaltiesByUser(player.id)
                        penalties.forEach {
                            listOfPenalties.add("Strafe ${it.penaltyName} wurde am ${it.createdAt} " +
                                    " ${it.amount} mal hinzugefügt")
                        }
                    } else {
                        listOfPenalties.add("Spieler ${messageContext.firstArg()} konnte nicht in unserer Datenbank" +
                                "gefunden werden")
                    }

                    listOfPenalties.print(silent, messageContext.chatId())
                }
                .build()
    }

    fun listPaymentsOfPlayer(): Ability {
        return Ability
                .builder()
                .name("listeallerzahlungen")
                .locality(Locality.GROUP)
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
                .locality(Locality.GROUP)
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
                .locality(Locality.GROUP)
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
                .locality(Locality.GROUP)
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
                    listOfTransactions.print(silent, messageContext.chatId())
                }
                .build()
    }

    fun payCrateOfBeer(): Ability {
        return Ability
                .builder()
                .name("bezahlenkiste")
                .locality(Locality.GROUP)
                .privacy(Privacy.GROUP_ADMIN)
                .action { messageContext ->
                    val listOfTransactions = mutableListOf<String>()
                    messageContext.firstArg().getPlayersAndAmounts().forEach { (name, amount) ->
                        val user = UserService.instance.getByName(name)
                        if (user != null) {
                            listOfTransactions.add(" ${name} hat ${amount} Kisten geschmissen!")
                            UserService.instance.pay(name, amount, Users.USERS.CASE_OF_BEER)

                            val messageUser = messageContext.user()
                            val userName = messageUser.userName ?: messageUser.firstName+" "+messageUser.lastName

                            UserPenaltyBeerPaymentService.instance.pay(
                                    messageUser.id,
                                    amount,
                                    userName
                            )
                        } else {
                            listOfTransactions.add("${name} konnte NICHT in der Datenbank gefunden werden. Bezahlung " +
                                    "erneut durchführen!!!")
                        }

                    }
                    listOfTransactions.print(silent, messageContext.chatId())
                }
                .build()
    }

    fun pay(): Ability {
        return Ability
                .builder()
                .name("bezahlen")
                .locality(Locality.GROUP)
                .privacy(Privacy.GROUP_ADMIN)
                .action { messageContext ->
                    val listOfTransactions = mutableListOf<String>()
                    messageContext.firstArg().getPlayersAndAmounts().forEach { (name, amount) ->
                        val user = UserService.instance.getByName(name)
                        if (user != null) {
                            listOfTransactions.add("${name} hat ${amount}€ bezahlt!")
                            UserService.instance.pay(name, amount, Users.USERS.CURRENT_PENALTIES)

                            val messageUser = messageContext.user()
                            val userName = messageUser.userName ?: messageUser.firstName+" "+messageUser.lastName
                            UserPenaltyPaymentService.instance.pay(
                                    user.id,
                                    amount,
                                    userName
                            )
                        } else {
                            listOfTransactions.add("${name} konnte NICHT in der Datenbank gefunden werden. Bezahlung " +
                                    "erneut durchführen!!!")
                        }
                    }
                    listOfTransactions.print(silent, messageContext.chatId())
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
                    .locality(Locality.GROUP)
                    .privacy(Privacy.GROUP_ADMIN)
                    .action { messageContext ->
                        val listOfTransactions = mutableListOf<String>()
                        messageContext.firstArg().getPlayersAndAmounts().forEach { (name, amount) ->
                            val cost = amount * penaltyRecord.penaltyCost
                            val numberOfCasesOfBeer = amount * penaltyRecord.caseOfBeerCost
                            val userRecord = UserService.instance.getByName(name)
                            if (userRecord != null) {
                                //update summed up penalties
                                userRecord.currentPenalties = userRecord.currentPenalties + cost
                                userRecord.caseOfBeer = userRecord.caseOfBeer + numberOfCasesOfBeer
                                UserService.instance.updateUser(name, userRecord)

                                //create new single penalty entry
                                val user = messageContext.user()
                                val userName = user.userName ?: user.firstName+" "+user.lastName

                                UserPenaltyService.instance.createUserPenalty(
                                        userRecord.id,
                                        penaltyRecord.id,
                                        amount,
                                        userName
                                )

                                //create bot replies
                                listOfTransactions.add("Update Strafen für  ${userRecord.name}")
                                listOfTransactions.add("${name}s Strafen wurden um ${cost}€ und seine Kistenanzahl" +
                                        " um ${numberOfCasesOfBeer} erhöht")
                                listOfTransactions.add("${name}s Strafen belaufen sich auf ${userRecord.currentPenalties}€" +
                                        " und er muss noch ${userRecord.caseOfBeer} Kisten schmeißen!")
                            } else {
                                listOfTransactions.add("${name} befindet sich nicht in unserer Datenbank")
                            }
                            listOfTransactions.add("----------------------------------------------")
                        }
                        listOfTransactions.print(silent, messageContext.chatId())
                    }
                    .build()

            abilities.add(ability)
        }

        return abilities
    }
}