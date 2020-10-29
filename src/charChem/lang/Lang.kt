package charChem.lang

typealias LocalDict = Map<String, String>
typealias LangParams = List<Pair<String, Any>>

object Lang {
    /**
     * Current language
     * Format uses from https://tools.ietf.org/html/rfc7231#section-3.1.3.1
     * Examples: en, ru - internal languages; zh, zh-TW - external (by addDict)
     */
    var curLang: String = "en"

    /**
     * Translate phrase
     * example: Lang.tr("Hello, [first] [last]", listOf("first" to "John", "last" to "Connor"))
     */
    fun tr(key: String, params: LangParams? = null, langId: String = ""): String {
        // actual language
        val lang = (if (langId.isEmpty()) curLang else langId).toLowerCase()
        // find local dictionary
        var curDict: LocalDict? = dict[lang]
        if (curDict == null) {
            val k = lang.indexOf('-')
            if (k >= 0) curDict = dict[lang.substring(0, k)]
        }
        val finalDict: LocalDict = curDict ?: enDict
        // find phrase
        val text = finalDict[key] ?: key
        // parameters
        return params?.fold(text) { acc, pair ->
            acc.replace("[${pair.first}]", pair.second.toString())
        } ?: text
    }

    fun findPhrase(key: String): String? = dict[curLang]?.get(key)

    private val ruDict: LocalDict = baseDictRu

    private val enDict: LocalDict = baseDictEn

    private val dict: Map<String, LocalDict> = mapOf("en" to enDict, "ru" to ruDict)
}