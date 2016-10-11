package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.silimatics.common.potions.ModPotions
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensBestower : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        val entity = LensOculator.getEntityLookedAt(player)
        if (!player.isSneaking || entity == null || entity !is EntityPlayer || player.getActivePotionEffect(MobEffects.WEAKNESS) != null ||
                entity.getActivePotionEffect(MobEffects.SPEED) != null) return
        entity.addPotionEffect(PotionEffect(MobEffects.SPEED, 80, 1))
        entity.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 80, 1))
        entity.addPotionEffect(PotionEffect(MobEffects.JUMP_BOOST, 80, 1))
        entity.addPotionEffect(PotionEffect(MobEffects.HASTE, 80, 1))

        //player.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 80, 50, true, true))
        player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 80, 50, true, true))
        //player.addPotionEffect(PotionEffect(MobEffects.JUMP_BOOST, 80, 128, true, true))
        player.addPotionEffect(PotionEffect(ModPotions.rooted, 80))
        player.addPotionEffect(PotionEffect(MobEffects.MINING_FATIGUE, 80, 50, true, true))
        player.addPotionEffect(PotionEffect(MobEffects.WITHER, 80, 0, true, true))
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.lensHeart.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.lensHeart.desc2")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.lensHeart.desc3")
    }
}
