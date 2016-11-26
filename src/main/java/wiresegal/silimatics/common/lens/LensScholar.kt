package wiresegal.silimatics.common.lens

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc

/**
 * Created by Elad on 10/12/2016.
 */
object LensScholar : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if(!player.isSneaking) return
        player.motionX /= 1.25
        player.motionY /= 1.25
        player.motionZ /= 1.25
        player.fallDistance = 0f
        val entities = world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(player.position.add(-3, -3, -3), player.position.add(4, 4, 4)))
        entities.filter { it != player }.forEach { it.motionX /= 1.75; it.motionY /= 1.75; it.motionZ /= 1.75 }
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.lensSchool.desc")
    }
}
