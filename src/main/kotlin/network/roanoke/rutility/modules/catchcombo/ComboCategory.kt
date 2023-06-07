package network.roanoke.rutility.modules.catchcombo

class ComboCategory(
    private val startValue: Int,
    private val endValue: Int,
    private val expBonus: Double,
    private val perfectIvs: Int,
    private val shinyChance: Double
) {

    fun inCategory(n: Int): Boolean {
        return n in startValue..endValue
    }

    fun getExpBonus(): Double {
        return expBonus
    }

    fun getPerfectIvs(): Int {
        return perfectIvs
    }

    fun getShinyChance(): Double {
        return shinyChance
    }

}