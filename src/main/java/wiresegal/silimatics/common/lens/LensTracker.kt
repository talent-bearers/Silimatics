package wiresegal.silimatics.common.lens

import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SPacketParticles
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
    val players = mutableListOf<UUID>()

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (!world.isRemote && world is WorldServer && player is EntityPlayerMP) {
            for ((uuid, eidos) in positionChangelog) {
                val color = uuid.hashCode()
                val r = ((color and 0xFF0000) shr 16) / 255.0 - 1
                val g = ((color and 0x00FF00) shr 8) / 255.0
                val b = (color and 0x0000FF) / 255.0
                for (pos in eidos)
                    world.spawnParticle(player, EnumParticleTypes.REDSTONE, true, pos.xCoord, pos.yCoord, pos.zCoord, 0, r, g, b, 1.0)
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
