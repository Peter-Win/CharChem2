package charChem.compiler.main

abstract class StackItem(val pos: Int) {
    abstract fun msgInvalidClose(): String
}

