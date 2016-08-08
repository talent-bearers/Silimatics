package wiresegal.silimatics.common.lens

import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper
import java.util.*

class LensTracker : ILens {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val positionChangelog = mutableMapOf<UUID, MutableList<Vec3d>>()

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (world.isRemote) {
            for ((uuid, eidos) in positionChangelog) {
                val color = uuid.hashCode()
                val r = ((color and 0xFF0000) shr 16) / 255.0 - 1
                val g = ((color and 0x00FF00) shr 8) / 255.0
                val b = (color and 0x0000FF) / 255.0
                for (pos in eidos)
                    world.spawnParticle(EnumParticleTypes.REDSTONE, true, pos.xCoord, pos.yCoord, pos.zCoord, r, g, b)
            }
        }
    }

    @SubscribeEvent
    fun onWorldTick(e: TickEvent.ClientTickEvent) {
        val world = Minecraft.getMinecraft().theWorld ?: return

        val players = mutableListOf<UUID>()
        if (e.phase == TickEvent.Phase.START && world.totalWorldTime % 20 == 0L) {
            for (i in world.playerEntities) {
                players.add(i.uniqueID)
                positionChangelog.getOrPut(i.uniqueID) { mutableListOf() }.add(i.positionVector)
            }

            for (i in positionChangelog.keys.toTypedArray())
                if (i !in players)
                    positionChangelog.remove(i)
        }
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.tracker.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.tracker.desc2")
    }
}
