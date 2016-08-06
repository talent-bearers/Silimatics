package wiresegal.silimatics.client.core

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.resources.IResourceManager
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextFormatting
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack

/**
 * @author WireSegal
 * Created at 11:53 PM on 8/5/16.
 */
class FontHijacker(val sup: FontRenderer) : FontRenderer(Minecraft.getMinecraft().gameSettings, ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false) {
    override fun drawStringWithShadow(text: String?, x: Float, y: Float, color: Int): Int {
        return sup.drawStringWithShadow(text.doReplace(), x, y, color)
    }

    override fun setUnicodeFlag(unicodeFlagIn: Boolean) {
        sup.unicodeFlag = unicodeFlagIn
    }

    override fun drawSplitString(str: String?, x: Int, y: Int, wrapWidth: Int, textColor: Int) {
        sup.drawSplitString(str.doReplace(), x, y, wrapWidth, textColor)
    }

    override fun getColorCode(character: Char): Int {
        return sup.getColorCode(character)
    }

    override fun getBidiFlag(): Boolean {
        return sup.bidiFlag
    }

    override fun getStringWidth(text: String?): Int {
        return sup.getStringWidth(text.doReplace())
    }

    override fun drawString(text: String?, x: Int, y: Int, color: Int): Int {
        return sup.drawString(text.doReplace(), x, y, color)
    }

    override fun drawString(text: String?, x: Float, y: Float, color: Int, dropShadow: Boolean): Int {
        return sup.drawString(text.doReplace(), x, y, color, dropShadow)
    }

    override fun getUnicodeFlag(): Boolean {
        return sup.unicodeFlag
    }

    override fun setBidiFlag(bidiFlagIn: Boolean) {
        sup.bidiFlag = bidiFlagIn
    }

    override fun getCharWidth(character: Char): Int {
        return sup.getCharWidth(character)
    }

    override fun trimStringToWidth(text: String?, width: Int): String {
        return sup.trimStringToWidth(text.doReplace(), width)
    }

    override fun trimStringToWidth(text: String?, width: Int, reverse: Boolean): String {
        return sup.trimStringToWidth(text.doReplace(), width, reverse)
    }

    override fun splitStringWidth(str: String?, maxLength: Int): Int {
        return sup.splitStringWidth(str.doReplace(), maxLength)
    }

    override fun onResourceManagerReload(resourceManager: IResourceManager?) {
        sup.onResourceManagerReload(resourceManager)
    }

    override fun listFormattedStringToWidth(str: String?, wrapWidth: Int): MutableList<String>? {
        return sup.listFormattedStringToWidth(str.doReplace(), wrapWidth)
    }

    private fun String?.doReplace(): String {
        if (this == null) return "null"
        if (!shouldReplace()) return this
        return this.replace(TextFormatting.OBFUSCATED.toString(), TextFormatting.DARK_GRAY.toString())
    }

    private fun shouldReplace(): Boolean {
        val player = Minecraft.getMinecraft().thePlayer ?: return false
        val headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
        if (headStack != null && headStack.item is ItemLensFrames) {
            val lensStack = headStack.getLensStack()
            if (lensStack.item == ModItems.lens && lensStack.itemDamage == EnumSandType.RASHID.ordinal) {
                return true
            }
        }
        return false
    }
}
