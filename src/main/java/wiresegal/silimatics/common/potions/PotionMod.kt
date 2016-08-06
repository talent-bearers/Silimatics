package wiresegal.silimatics.common.potions

import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.lib.LibMisc

open class PotionMod(name: String, badEffect: Boolean, color: Int, iconIndex: Int) : Potion(badEffect, color) {

    val iconX = iconIndex % 8
    val iconY = iconIndex / 8

    init {
        GameRegistry.register(this, ResourceLocation(LibMisc.MODID, name))
        setPotionName("${LibMisc.MODID}.potion." + name)
        if (!badEffect)
            setBeneficial()
    }

    override fun hasStatusIcon() = false

    @SideOnly(Side.CLIENT)
    override fun renderInventoryEffect(x: Int, y: Int, effect: PotionEffect, mc: Minecraft) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource)
        mc.currentScreen!!.drawTexturedModalRect(x + 6, y + 7, 0 + iconX * 18, 198 + iconY * 18, 18, 18)
    }

    @SideOnly(Side.CLIENT)
    override fun renderHUDEffect(x: Int, y: Int, effect: PotionEffect?, mc: Minecraft, alpha: Float) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource)
        mc.ingameGUI.drawTexturedModalRect(x + 3, y + 3, 0 + iconX * 18, 198 + iconY * 18, 18, 18)
    }

    fun hasEffect(entity: EntityLivingBase): Boolean {
        return hasEffect(entity, this)
    }

    fun getEffect(entity: EntityLivingBase): PotionEffect? {
        return getEffect(entity, this)
    }

    companion object {
        val resource = ResourceLocation(LibMisc.MODID, "textures/gui/potions.png")

        fun hasEffect(entity: EntityLivingBase, potion: Potion): Boolean {
            return entity.getActivePotionEffect(potion) != null
        }

        fun getEffect(entity: EntityLivingBase, potion: Potion): PotionEffect? {
            return entity.getActivePotionEffect(potion)
        }
    }
}
