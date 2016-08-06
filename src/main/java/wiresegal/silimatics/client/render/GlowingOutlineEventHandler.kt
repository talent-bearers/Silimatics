package wiresegal.silimatics.client.render

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack
import wiresegal.silimatics.common.lens.LensOculator

/**
 * @author WireSegal
 * Created at 1:02 PM on 8/6/16.
 */
object GlowingOutlineEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun EntityPlayer.isWearingOculatorLens(): Boolean {
        val headStack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) ?: return false
        if (headStack.item is ItemLensFrames) {
            val lensStack = headStack.getLensStack()
            return ItemLens.getLensFromStack(lensStack) is LensOculator
        }
        return false
    }

    val entityRegens = mutableListOf<Pair<EntityPlayer, Boolean>>()

    @SubscribeEvent
    fun onRenderTick(e: TickEvent.RenderTickEvent) {
        val thePlayer = Minecraft.getMinecraft().thePlayer
        if (!(thePlayer?.isWearingOculatorLens() ?: false)) return
        if (e.phase == TickEvent.Phase.START) {
            entityRegens.clear()
            for (entity in Minecraft.getMinecraft().theWorld.playerEntities) {
                if (entity == thePlayer) continue
                if ((entity.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG) ?: continue).getBoolean(ItemLens.Companion.EventHandler.OCULATOR)) {
                    entityRegens.add(entity to entity.isGlowing)
                    entity.isGlowing = true
                }
            }
        } else {
            for ((entity, flag) in entityRegens) {
                entity.isGlowing = flag
            }
        }
    }
}
