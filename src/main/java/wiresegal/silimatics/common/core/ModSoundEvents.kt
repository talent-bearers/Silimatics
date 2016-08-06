package wiresegal.silimatics.common.core

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 1:18 PM on 4/19/16.
 */
object ModSoundEvents {

    val WOOSH: SoundEvent

    init {
        val loc = ResourceLocation(LibMisc.MODID, "woosh")
        WOOSH = SoundEvent(loc)
        GameRegistry.register(WOOSH, loc)
    }
}
