package flashcards


import java.io.File
import kotlin.random.Random

val cards = emptyMap<String, String>().toMutableMap()
val cardsStats = emptyMap<String, Int>().toMutableMap()
var ON = true

val logs = mutableListOf<String>()

val ADD = "add"
val REMOVE = "remove"
val IMPORT = "import"
val EXPORT = "export"
val ASK = "ask"
val LOG = "log"
val HARDESTCARD = "hardest card"
val RESETSTATS = "reset stats"
val EXIT = "exit"


fun addCard() {
    // Generate a card
    println("Card:")
    logs.add("Card:")
    var term = readLine()!!
    logs.add(term)
    if (cards.containsKey(term)) {
        println("The card \"$term\" already exists.")
        logs.add("The card \"$term\" already exists.")
        return
    }
    println("The definition of the card:")
    logs.add("The definition of the card:")
    var definition = readLine()!!
    logs.add(definition)
    if (cards.containsValue(definition)) {
        println("The definition \"$definition\" already exists.")
        logs.add("The definition \"$definition\" already exists.")
        return
    }
    cards[term] = definition
    cardsStats[term] = 0
    println("The pair (\"$term\":\"$definition\") has been added.")
    logs.add("The pair (\"$term\":\"$definition\") has been added.")
}

fun removeCard() {
    println("Which card?")
    logs.add("Which card?")
    val term = readLine()!!
    logs.add(term)
    if (cards.containsKey(term)) {
        cards.remove(term)
        cardsStats.remove(term)
        println("The card has been removed.")
        logs.add("The card has been removed.")
    } else {
        println("Can't remove \"$term\": there is no such card.")
        logs.add("Can't remove \"$term\": there is no such card.")
    }

}

fun import() {
    println("File name:")
    logs.add("File name:")
    val fileName = ".\\" + readLine()!!
    logs.add(fileName.substring(1, fileName.length))
    val readFile = File(fileName)
    if (readFile.exists()) {
        var alreadyExistsCount = 0
        val readCardsList = readFile.readLines()
        readCardsList.forEach {
            val term = it.split(" ")[0]
            val definition = it.split(" ")[1]
            val stat = it.split(" ")[2] ?: "0"
            if (cards.containsKey(term)) ++alreadyExistsCount
            cards[term] = definition
            try { cardsStats[term] = stat.toInt() }
            catch (e: NumberFormatException) {
                cardsStats[term] = 0
            }
        }
        println("${readCardsList.size} cards have been loaded.")
        logs.add("${readCardsList.size} cards have been loaded.")
    } else {
        println("File not found.")
        logs.add("File not found.")
    }
}

fun importByCommandLine(fileName: String) {
    val readFile = File(fileName)
    if (readFile.exists()) {
        var alreadyExistsCount = 0
        val readCardsList = readFile.readLines()
        readCardsList.forEach {
            val term = it.split(" ")[0]
            val definition = it.split(" ")[1]
            val stat = it.split(" ")[2] ?: "0"
            if (cards.containsKey(term)) ++alreadyExistsCount
            cards[term] = definition
            try { cardsStats[term] = stat.toInt() }
            catch (e: NumberFormatException) {
                cardsStats[term] = 0
            }
        }
        println("${readCardsList.size} cards have been loaded.")
        logs.add("${readCardsList.size} cards have been loaded.")
    } else {
        println("File not found.")
        logs.add("File not found.")
    }
}

fun export() {
    println("File name:")
    logs.add("File name:")
    val fileName = ".\\" + readLine()!!
    logs.add(fileName.substring(2, fileName.length))
    val writeFile = File(fileName)
    writeFile.writeText("")
    cards.keys.forEach { term -> writeFile.appendText("$term ${cards[term]} ${cardsStats[term]}\n") }
    println("${cards.size} cards have been saved.")
    logs.add("${cards.size} cards have been saved.")
}

fun exportByCommandLine(fileName: String) {
    logs.add(fileName.substring(2, fileName.length))
    val writeFile = File(fileName)
    writeFile.writeText("")
    cards.keys.forEach { term -> writeFile.appendText("$term ${cards[term]} ${cardsStats[term]}\n") }
    println("${cards.size} cards have been saved.")
    logs.add("${cards.size} cards have been saved.")
}

fun exit() {
    println("Bye bye!")
    logs.add("Bye bye!")
    ON = false
}

fun askCard() {
    println("How many times to ask?")
    logs.add("How many times to ask?")
    val n = readLine()!!.toInt()
    logs.add(n.toString())
    for (i in 1..n) {
        if (cards.isNotEmpty()) {
            val cardKeys = cards.keys.toList()
            val term = cardKeys[Random.nextInt(0, cards.size)]
            println("Print the definition of \"$term\"")
            logs.add("Print the definition of \"$term\"")
            var answer = readLine()!!
            logs.add(answer)
            val definition = cards[term]
            if (term != null) {
                if (answer == definition) {
                    println("Correct!")
                    logs.add("Correct!")
                } else if (cards.containsValue(answer)) {
                    val cardStat = cardsStats[term] ?: 0
                    cardsStats[term] = cardStat + 1
                    println("Wrong. The right answer is \"$definition\", but your definition is correct for \"${cards.filter { it.value == answer }.keys.first()}\".")
                    logs.add("Wrong. The right answer is \"$definition\", but your definition is correct for \"${cards.filter { it.value == answer }.keys.first()}\".")
                } else {
                    val cardStat = cardsStats[term] ?: 0
                    cardsStats[term] = cardStat + 1
                    println("Wrong. The right answer is \"$definition\".")
                    logs.add("Wrong. The right answer is \"$definition\".")
                }
            }

        }

    }
}

fun saveLogs() {
    println("File name")
    logs.add("File name:")
    val fileName = readLine()!!
    val logsFile = File(fileName)
    logs.forEach { logsFile.appendText("$it\n") }
    println("The log has been saved.")
    logsFile.appendText("The log has been saved.\n")
}

fun hardestCard() {
    val largestMistakeCount = cardsStats.maxByOrNull { it.value }
    var cardCount = 0 // How many cards has largest mistake count.
    cardsStats.forEach { (_, mistakeCount) ->
        if (largestMistakeCount != null) {
            if (largestMistakeCount.value != 0 && mistakeCount == largestMistakeCount.value) { ++cardCount }
        }
    }

    if (cardsStats.isEmpty() || cardCount == 0) {
        println("There are no cards with errors.")
        logs.add("There are no cards with errors.")
    } else if (cardCount == 1) {
        if (largestMistakeCount != null) {
            println("The hardest card is \"${largestMistakeCount.key}\". You have ${largestMistakeCount.value} errors answering it.")
            logs.add("The hardest card is \"${largestMistakeCount.key}\". You have ${largestMistakeCount.value} errors answering it.")
        }
    } else {
        var allTerms = "The hardest cards are "
        cardsStats.forEach { (term, mistakeCount) ->
            if (largestMistakeCount != null) {
                if (largestMistakeCount.value != 0 && mistakeCount == largestMistakeCount.value) {
                    allTerms += "\"$term\", "
                }
            }
        }
        if (largestMistakeCount != null) {
            allTerms = allTerms.substring(0, allTerms.length-2) + ". " + "You have ${largestMistakeCount.value} errors answering them."
        }
        println(allTerms)
        logs.add(allTerms)
    }

}

fun resetStats() {
    cardsStats.forEach { (t, _) -> cardsStats[t] = 0 }
    println("Card statistics have been reset.")
    logs.add("Card statistics have been reset.")
}

fun importByCommandLine(args: Array<String>){
    if (args.contains("-import")) {
        val index = args.indexOf("-import")
        val fileName = args[index + 1]
        importByCommandLine(fileName)
    }
}

fun exportByCommandLine(args: Array<String>){
    if (args.contains("-export")) {
        val index = args.indexOf("-export")
        val fileName = args[index + 1]
        exportByCommandLine(fileName)
    }
}

fun main(args: Array<String>) {
    importByCommandLine(args = args)
    while (true) {
        println("Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):")
        logs.add("Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):")
        var action = readLine()!!
        logs.add(action)
        if (action == ADD) {
            addCard()
        } else if (action == REMOVE) {
            removeCard()
        } else if (action == IMPORT) {
            import()
        } else if (action == EXPORT) {
            export()
        } else if (action == ASK) {
            askCard()
        } else if (action == LOG) {
            saveLogs()
        } else if (action == HARDESTCARD) {
            hardestCard()
        } else if (action == RESETSTATS) {
            resetStats()
        }else if (action == EXIT) {
            exit()
        }  else {
            println("Enter a legitimate action.")
        }
        println()
        if (!ON) break
    }
    exportByCommandLine(args = args)
}
