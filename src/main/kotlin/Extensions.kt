import org.telegram.abilitybots.api.sender.SilentSender

data class PlayerAmountModel constructor(val name: String, val amount: Int)

fun String.getPlayersAndAmounts(): List<PlayerAmountModel> {
    val result = mutableListOf<PlayerAmountModel>()
    val players = this.split(",")
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

fun List<String>.print(silent: SilentSender, chatId: Long) {
    this.forEach {
        silent.send(it, chatId)
    }
}