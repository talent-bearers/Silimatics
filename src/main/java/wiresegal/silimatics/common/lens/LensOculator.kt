package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SPacketParticles
import net.minecraft.util.EnumParticleTypes
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper
import java.util.*

class LensOculator : ILens {
    companion object {
        val OCULATORS = mutableListOf<UUID>()
    }

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if(player !is EntityPlayerMP || world !is WorldServer) return
        for(p in world.playerEntities) {
            if (OCULATORS.contains(p.uniqueID) || p.isSmedry()) {
                val r: Float = 50 / 255f
                val g: Float = 50 / 255f
                val b: Float = 50 / 255f
                val packet = SPacketParticles(EnumParticleTypes.REDSTONE, true, player.posX.toFloat(), player.posY.toFloat() + 2, player.posZ.toFloat(), r, g, b, 1.toFloat(), 0)
                val blockpos = player.position
                val d0 = blockpos.distanceSq(player.posX, player.posY, player.posZ)
                if (d0 <= 512 * 512)
                    player.connection.sendPacket(packet)
            }
        }


    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.oculator.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.oculator.desc2")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.oculator.desc3")
    }

    fun EntityPlayer.isSmedry(): Boolean = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean(ItemLens.Companion.EventHandler.OCULATOR)
}
