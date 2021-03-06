package charChem.core

/**
 * Find Chemical element by string id ('H', 'He', 'Li', ...)
 */
fun findElement(id: String): ChemAtom? =
        PeriodicTable.dict[id] ?: PeriodicTable.isotopesDict[id]

object PeriodicTable {
    val list: List<ChemAtom> by lazy {
        shortDef.mapIndexed { index: Int, item: String ->
            val (id, mass) = item.split(',')
            ChemAtom(index + 1, id, mass.toDouble())
        }
    }
    val dict: Map<String, ChemAtom> by lazy { list.associateBy { it.id } }
    val isotopes = listOf(
            ChemAtom(1, "D", 2.01410177811),
            ChemAtom(1, "T", 3.0160492),
    )
    val isotopesDict = isotopes.associateBy { it.id }

    val H: ChemAtom by lazy { list[0] }
    val D: ChemAtom by lazy { isotopes[0] }
    val T: ChemAtom by lazy { isotopes[1] }
    val He: ChemAtom by lazy { list[1] }
    val Li: ChemAtom by lazy { list[2] }
    val Be: ChemAtom by lazy { list[3] }
    val B: ChemAtom by lazy { list[4] }
    val C: ChemAtom by lazy { list[5] }
    val N: ChemAtom by lazy { list[6] }
    val O: ChemAtom by lazy { list[7] }
    val F: ChemAtom by lazy { list[8] }
    val Ne: ChemAtom by lazy { list[9] }
    val Na: ChemAtom by lazy { list[10] }
    val Mg: ChemAtom by lazy { list[11] }
    val Al: ChemAtom by lazy { list[12] }
    val Si: ChemAtom by lazy { list[13] }
    val P: ChemAtom by lazy { list[14] }
    val S: ChemAtom by lazy { list[15] }
    val Cl: ChemAtom by lazy { list[16] }
    val Ar: ChemAtom by lazy { list[17] }
    val K: ChemAtom by lazy { list[18] }
    val Ca: ChemAtom by lazy { list[19] }
    val Sc: ChemAtom by lazy { list[20] }
    val Ti: ChemAtom by lazy { list[21] }
    val V: ChemAtom by lazy { list[22] }
    val Cr: ChemAtom by lazy { list[23] }
    val Mn: ChemAtom by lazy { list[24] }
    val Fe: ChemAtom by lazy { list[25] }
    val Co: ChemAtom by lazy { list[26] }
    val Ni: ChemAtom by lazy { list[27] }
    val Cu: ChemAtom by lazy { list[28] }
    val Zn: ChemAtom by lazy { list[29] }
    val Ga: ChemAtom by lazy { list[30] }
    val Ge: ChemAtom by lazy { list[31] }
    val As: ChemAtom by lazy { list[32] }
    val Se: ChemAtom by lazy { list[33] }
    val Br: ChemAtom by lazy { list[34] }
    val Kr: ChemAtom by lazy { list[35] }

    private val shortDef = listOf(
            "H,1.008", "He,4.003",
            "Li,6.941", "Be,9.0122", "B,10.811", "C,12.011", "N,14.007", "O,15.999", "F,18.998", "Ne,20.179",
            "Na,22.99", "Mg,24.312", "Al,26.092", "Si,28.086", "P,30.974", "S,32.064", "Cl,35.453", "Ar,39.948",
            "K,39.102", "Ca,40.08", "Sc,44.956", "Ti,47.956", "V,50.941", "Cr,51.996", "Mn,54.938", "Fe,55.849", "Co,58.933", "Ni,58.7",
            "Cu,63.546", "Zn,65.37", "Ga,69.72", "Ge,72.59", "As,74.922", "Se,78.96", "Br,79.904", "Kr,83.8",
            "Rb,85.468", "Sr,87.62", "Y,88.906", "Zr,91.22", "Nb,92.906", "Mo,95.94", "Tc,99", "Ru,101.07", "Rh,102.906", "Pd,106.4",
            "Ag,107.868", "Cd,112.41", "In,114.82", "Sn,118.69", "Sb,121.75", "Te,127.6", "I,126.905", "Xe,131.3",
            "Cs,132.905", "Ba,137.34",
            "La,138.906", "Ce,140.115", "Pr,140.908", "Nd,144.24", "Pm,145", "Sm,150.4", "Eu,151.96", "Gd,157.25", "Tb,158.926",
            "Dy,162.5", "Ho,164.93", "Er,167.26", "Tm,168.934", "Yb,173.04", "Lu,174.97",
            "Hf,178.49", "Ta,180.948", "W,183.85", "Re,186.207", "Os,190.2", "Ir,192.22", "Pt,195.09",
            "Au,196.967", "Hg,200.59", "Tl,204.37", "Pb,207.19", "Bi,208.98", "Po,210", "At,210", "Rn,222",
            "Fr,223", "Ra,226",
            "Ac,227", "Th,232.038", "Pa,231", "U,238.29", "Np,237", "Pu,244", "Am,243", "Cm,247", "Bk,247", "Cf,251", "Es,254",
            "Fm,257", "Md,258", "No,259", "Lr,260",
            "Rf,261", "Db,262", "Sg,271", "Bh,267", "Hs,269", "Mt,276", "Ds,281", "Rg,280", "Cn,285",
            "Nh,286", "Fl,289", "Mc,289", "Lv,293", "Ts,294", "Og,294",
    )
}
