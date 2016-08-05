package wiresegal.silimatics.common.lens

import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SPacketParticles
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
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
    val players = mutableListOf<UUID>()

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (!world.isRemote && player is EntityPlayerMP) {
            for ((uuid, eidos) in positionChangelog) {
                val color = uuid.hashCode()
                val r = ((color and 0xFF0000) shr 16) / 255f - 1
                val g = ((color and 0x00FF00) shr 8) / 255f
                val b = (color and 0x0000FF) / 255f
                for (pos in eidos) {
                    val packet = SPacketParticles(EnumParticleTypes.REDSTONE, true, pos.xCoord.toFloat(), pos.yCoord.toFloat(), pos.zCoord.toFloat(), r, g, b, 1.toFloat(), 0)
                    val blockpos = player.position
                    val d0 = blockpos.distanceSq(pos.xCoord, pos.yCoord, pos.zCoord)
                    if (d0 <= 512 * 512)
                        player.connection.sendPacket(packet)
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldTick(e: TickEvent.WorldTickEvent) {
        if (e.world.isRemote) return

        if (e.phase == TickEvent.Phase.START)
            players.clear()
        else if (!players.isEmpty())
            for (uuid in positionChangelog.keys.toTypedArray())
                if (uuid !in players)
                    positionChangelog.remove(uuid)
    }

    @SubscribeEvent
    fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
        if (e.entityLiving.worldObj.totalWorldTime % 20 != 0L || e.entityLiving.worldObj.isRemote) return

        val player = e.entityLiving
        if (player is EntityArmorStand) return
        players.add(player.uniqueID)
        var changelog = positionChangelog[player.uniqueID]
        if (changelog == null) {
            changelog = mutableListOf()
            positionChangelog.put(player.uniqueID, changelog)
        }

        changelog.add(player.positionVector.addVector(0.0, 0.5, 0.0))
        changelog = changelog.takeLast(30).toMutableList()

        positionChangelog.put(player.uniqueID, changelog)
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.tracker.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.tracker.desc2")
    }
}
