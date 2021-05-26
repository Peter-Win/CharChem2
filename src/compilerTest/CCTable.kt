package compilerTest

import java.io.File

typealias Row = List<String?>
typealias CCTable = List<Row>

private fun unescape(value: String): String? =
        if (value == "\\0") null else value
                .replace("\\t", "\t")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\\\", "\\")

private fun parseRow(line: String): Row =
        line.split('\t').map { unescape(it) }

fun loadTable(fileName: String): CCTable {
    return File(fileName).readLines().map { parseRow(it) }
}
