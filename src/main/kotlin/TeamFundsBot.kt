import de.pengelkes.jooq.model.tables.Penalties
import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.records.PenaltiesRecord
import de.pengelkes.jooq.model.tables.records.UsersRecord
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
                        val argument = messageContext.firstArg()
                        val members = argument.split(",")
                        members.forEach { member ->
                            val amount = member.substring(member.length - 1).toInt()
                            val name = member.substring(0, member.length - 1)
                            val cost = amount * penaltyRecord.penaltyCost
                            val numberOfCasesOfBeer = amount * penaltyRecord.caseOfBeerCost
                            val userRecord = UserService.instance.getByName(name)
                            if (userRecord != null) {
                                listOfTransactions.add("Update Strafen für Spieler ${userRecord.name}")
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
                            } else {
                                silent.send("Der Spieler befindet sich nicht in unserer Datenbank", messageContext.chatId())
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