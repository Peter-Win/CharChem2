package charChem.core

// Начало конструкции, умножающей последующее содержимое на указанный коэффициент
// Кроме того, является мостиком, т.е. образует новую подцепь
// example: CuSO4*5H2O
class ChemMul(val n: ChemK) : ChemObj() {
    override fun walk(visitor: Visitor) {
        visitor.mul(this)
    }
}

// Конец множителя.
// Не участвует в выводе.
// Предназначен для вычислительных алгоритмов, использующих стек, чтобы выполнить pop
class ChemMulEnd(val begin: ChemMul) : ChemObj() {
    override fun walk(visitor: Visitor) {
        visitor.mulEnd(this)
    }
}