package wiresegal.silimatics.common.lens

import com.google.common.collect.Multimap
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper
import java.util.*

object LensWarrior : ILens {
    val TAG_ACTIVE = "active"

    override fun shouldMarkAsOculator(stack: ItemStack): Boolean {
        return false
    }

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, true)
    }

    override fun onDeadTick(world: World, player: EntityPlayer, stack: ItemStack) {
        ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, false)
    }

    override fun onCleanupTick(world: World, entity: Entity, stack: ItemStack) {
        ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, false)
    }

    override fun addAttributes(slot: EntityEquipmentSlot, stack: ItemStack, modifiers: Multimap<String, AttributeModifier>) {
        if (slot == EntityEquipmentSlot.HEAD && ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, true)) {
            modifiers.removeAll(SharedMonsterAttributes.ARMOR.attributeUnlocalizedName)
            modifiers.put(SharedMonsterAttributes.ARMOR.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior armor modifier", 4.0, 0))
            modifiers.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior toughness modifier", 1.0, 0))
            modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior attack modifier", 2.0, 0))
            modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior attack speed modifier", -1.0, 0))
            modifiers.put(SharedMonsterAttributes.MOVEMENT_SPEED.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior speed modifier", 0.25, 1))
            modifiers.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior resistance modifier", 0.25, 0))
        }
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.warrior.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.warrior.desc2")
    }

    private val WARRIOR_MODIFIER = UUID.fromString("35f6b4f8-03d3-41c9-b31f-fb9c893ca6da")

}
