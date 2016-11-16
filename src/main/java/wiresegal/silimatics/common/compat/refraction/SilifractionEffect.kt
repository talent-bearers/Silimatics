package wiresegal.silimatics.common.compat.refraction

import com.teamwizardry.refraction.api.Effect
import com.teamwizardry.refraction.common.light.EffectTracker
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import java.awt.Color

/**
 * Created by Elad on 11/16/2016.
 */
object SilifractionEffect : Effect() {
    init {
        EffectTracker.registerEffect(this)
    }

    override fun getCooldown(): Int {
        return if (potency == 0) 0 else 1000 / potency
    }

    override fun run(world: World, locations: Set<BlockPos>) {
        if (beam.trace.typeOfHit != RayTraceResult.Type.BLOCK) return
        world.setBlockState(beam.trace.blockPos, Blocks.AIR.defaultState)
    }

    override fun getColor(): Color {
        return Color.YELLOW
    }
}