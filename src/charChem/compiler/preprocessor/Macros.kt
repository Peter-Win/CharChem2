package charChem.compiler.preprocessor

class Macros(val name: String) {
    var body: String = ""
}

val globalMacros: MutableMap<String, Macros> = mutableMapOf()