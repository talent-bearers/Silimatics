package wiresegal.silimatics.common.compat.refraction

import com.teamwizardry.refraction.api.Effect
import com.teamwizardry.refraction.common.light.EffectTracker
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.util.SilimaticEvents
import java.awt.Color

/**
 * Created by Elad on 11/16/2016.
 */
class BeamedBrightsandEffect() : Effect() {
    init {
        EffectTracker.registerEffect(this)
    }

    override fun getCooldown(): Int {
        return 1000
    }

    override fun getType(): EffectType? {
        return EffectType.BEAM
    }

    override fun run(worldObj: World, locations: Set<BlockPos>) {
        if (beam?.trace?.typeOfHit != RayTraceResult.Type.BLOCK) return
        SilimaticEvents.tracker.add(BlockPos(beam.trace.blockPos))
    }

    override fun getColor(): Color? {
        return Color(EnumSandType.BRIGHT.glassColor)
    }

}