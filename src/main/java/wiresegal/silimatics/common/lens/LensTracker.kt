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
import java.awt.Color
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
                for (posI in eidos.indices) {
                    if (posI == eidos.size - 1) continue
                    val pos = eidos[posI]
                    val nextPos = eidos[posI + 1]

                    val lenVec = nextPos.subtract(pos)
                    val len = lenVec.lengthVector()

                    val ray = lenVec.normalize()
                    val step = 2.5
                    val steps = (len * step).toInt()

                    for (i in 0..steps - 1) {
                        val x = pos.xCoord + ray.xCoord * i / step
                        val y = pos.yCoord + ray.yCoord * i / step
                        val z = pos.zCoord + ray.zCoord * i / step
                        world.spawnParticle(EnumParticleTypes.REDSTONE, true, x, y, z, r, g, b)
                    }
                    world.spawnParticle(EnumParticleTypes.REDSTONE, true, nextPos.xCoord, nextPos.yCoord + 0.1, nextPos.zCoord, r, g, b)
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldTick(e: TickEvent.ClientTickEvent) {
        val world = Minecraft.getMinecraft().theWorld ?: return

        val players = mutableListOf<UUID>()
        if (e.phase == TickEvent.Phase.START && world.totalWorldTime % 10 == 0L) {
            for (i in world.playerEntities) {
                players.add(i.uniqueID)
                val poslog = positionChangelog.getOrPut(i.uniqueID) { mutableListOf() }
                poslog.add(i.positionVector)
                if (poslog.size > 30) for (ignored in 1..poslog.size - 30)
                    poslog.removeAt(0)

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
