package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.mainPreProcess
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMainPreProcess {
    @Test
    fun testAll() {
        assertEquals(mainPreProcess(""), "")
        assertEquals(mainPreProcess("no macro"), "no macro")
        // first declare then use
        assertEquals(mainPreProcess("@:MyMacro()...some text...@;@MyMacro()"), "...some text...")
        // Simultaneous declaration and call the macro
        assertEquals(mainPreProcess("@:MyMacro()...some text...@()"), "...some text...")
        // The use of positional parameters
        assertEquals(mainPreProcess("@:MyMacro(a,b)...&a...&b...@(First,Second)"), "...First...Second...")
        // Using named parameters
        assertEquals(mainPreProcess("@:MyMacro(a,b)...&a...&b...@(b:Second,a:First)"), "...First...Second...")
        // Default values
        assertEquals(mainPreProcess("@:MyMacro(a:First,b:Second)...&a...&b...@()+@MyMacro(,Last)"),
                "...First...Second...+...First...Last...")
        // Recursive calls
        assertEquals(mainPreProcess("@:MyMacro(a,b)[&a...&b]@(@MyMacro(A,B),@MyMacro(C,D))"),
                "[[A...B]...[C...D]]")
    }
}