package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntitySmallFireball
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.SilimaticMethodHandles
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

object LensFirebringer : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking && !world.isRemote) {
            val fireball = EntitySmallFireball(world, player.posX + player.lookVec.xCoord, player.posY + player.eyeHeight + player.lookVec.yCoord, player.posZ + player.lookVec.zCoord, player.lookVec.xCoord / 8, player.lookVec.yCoord / 8, player.lookVec.zCoord / 8)
            SilimaticMethodHandles.setTicksAlive(fireball, 599)
            world.spawnEntityInWorld(fireball)
        } else if(world.isRemote) ;
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.firebringer.desc")
    }
}
