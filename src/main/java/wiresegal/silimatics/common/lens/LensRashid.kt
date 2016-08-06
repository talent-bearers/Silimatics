package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensRashid : ILens {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onServerChat(e: ServerChatEvent) {
        val player = e.player
        val component = e.component
        val headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
        if (headStack != null && headStack.item is ItemLensFrames) {
            val lensStack = headStack.getLensStack()
            if (lensStack.item == ModItems.lens && lensStack.itemDamage == EnumSandType.RASHID.ordinal) {
                if (component is TextComponentTranslation) {
                    for (i in 1..component.formatArgs.size - 1) {
                        val iComp = component.formatArgs[i]
                        if (iComp is String)
                            component.formatArgs[i] = TextFormatting.OBFUSCATED.toString() + component.formatArgs[i]
                        else if (iComp is ITextComponent)
                            iComp.style.obfuscated = true
                    }
                } else component.style.obfuscated = true
            }
        }
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.translator.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.translator.desc2")
    }
}
