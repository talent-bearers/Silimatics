package wiresegal.silimatics.common.potions

/**
 * @author WireSegal
 * Created at 1:18 PM on 8/6/16.
 */
object ModPotions {
    val disoriented: PotionMod

    private var i = 0

    init {
        disoriented = PotionMod("disoriented", true, 0x404040, i++)
    }
}
