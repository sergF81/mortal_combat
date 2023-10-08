package com.example.mortal_combat

open class Creature(
    open val name: String = "",
    open var healthMax: Int = 0,
    open val attack: Int = 0,
    open val defense: Int = 0,
    open val damage: IntRange = 1..6,
    open var frameArray: Array<String> = arrayOf("", "", "", "", "", "", "")
) {
    open var healthCurrent: Int = 0
}

class UserHero(
    override var name: String = "",
    override var healthMax: Int = 0,
    override var attack: Int = 0,
    override var defense: Int = 0,
    override var damage: IntRange = 1..6,
    override var frameArray: Array<String> = arrayOf("", "", "", "", "", "", ""),
) : Creature() {
    var restoreTime: Int = 4
    override var healthCurrent: Int = healthMax


    fun restoreLife(current: Int, max: Int): Int {
        if (restoreTime != 0) {
            restoreTime--
            println("restoreTime: $restoreTime")
            println("current: $current")
            println("max: $max")
            healthCurrent = current + ("%.0f".format((max / 100.0) * 30)).toInt()
            if (healthCurrent > max) healthCurrent = max
            println(current)
        }
        return healthCurrent
    }
}

class Monster(
    override var name: String = "",
    override var healthMax: Int = 0,
    override val attack: Int = 0,
    override val defense: Int = 0,
    override val damage: IntRange = 1..6,
    override var frameArray: Array<String> = arrayOf("", "", "", "", "", "", ""),
) : Creature() {
    override var healthCurrent: Int = healthMax

}