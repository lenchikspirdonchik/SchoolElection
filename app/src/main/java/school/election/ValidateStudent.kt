package school.election

class ValidateStudent {
    private val c5A = arrayOf("РД", "АД", "ВА", "ДА", "АВ", "ЕА", "МД")
    private val c5B = arrayOf("КА", "АА")
    private val c5V = arrayOf("АИ", "ЮВ", "ВА", "МА", "АА")
    private val c6A = arrayOf("АА", "ВВ")
    private val c6B = arrayOf("АВ", "ВД", "ВА")
    private val c6V = arrayOf("АА", "ДА", "КВ")
    private val c7A = arrayOf("АИ", "МА", "АС")
    private val c7B = arrayOf("ВВ")
    private val c7V = arrayOf("АА", "МС", "ИИ")
    private val c7G = arrayOf("ДВ")
    private val c8A = arrayOf("АВ")
    private val c8B = arrayOf("МА")
    private val c8V = arrayOf("АА", "ЯВ", "СВ")
    private val c8G = arrayOf("АД", "ЕА", "ИЕ")
    private val c9A = arrayOf("ММ", "РА", "ДД")
    private val c9B = arrayOf("АА", "ДА", "АМ")
    private val c9V = arrayOf("АВ", "ЕВ", "ЮА")
    private val c10A = arrayOf("ВА", "МК")
    private val c10V = arrayOf("АД", "ВА", "СА", "МД", "ГМ", "АВ", "ВД")
    private val c11A = arrayOf("АЮ", "ВА", "ДА", "АА")
    private val c11B = arrayOf("АА", "ИА", "ЕА")
    private val c11D = arrayOf("АА", "МА", "ДА", "ДС", "ДВ", "ИА")


    fun validateStudent(classes: String, name: String): Boolean {
        return when (classes) {
            "5А" -> return c5A.contains(name)
            "5Б" -> return c5B.contains(name)
            "5В" -> return c5V.contains(name)
            "6А" -> return c6A.contains(name)
            "6Б" -> return c6B.contains(name)
            "6В" -> return c6V.contains(name)
            "7А" -> return c7A.contains(name)
            "7Б" -> return c7B.contains(name)
            "7В" -> return c7V.contains(name)
            "7Г" -> return c7G.contains(name)
            "8А" -> return c8A.contains(name)
            "8Б" -> return c8B.contains(name)
            "8В" -> return c8V.contains(name)
            "8Г" -> return c8G.contains(name)
            "9А" -> return c9A.contains(name)
            "9Б" -> return c9B.contains(name)
            "9В" -> return c9V.contains(name)
            "10А" -> return c10A.contains(name)
            "10В" -> return c10V.contains(name)
            "11А" -> return c11A.contains(name)
            "11Б" -> return c11B.contains(name)
            "11Д" -> return c11D.contains(name)
            else -> false
        }
    }
}