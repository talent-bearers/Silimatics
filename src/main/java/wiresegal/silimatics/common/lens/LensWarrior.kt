package wiresegal.silimatics.common.lens

import com.google.common.collect.Multimap
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens

import java.util.UUID

class LensWarrior : ILens {
    override fun shouldMarkAsOculator(stack: ItemStack): Boolean {
        return false
    }

    override fun addAttributes(slot: EntityEquipmentSlot, stack: ItemStack, modifiers: Multimap<String, AttributeModifier>) {
        if (slot == EntityEquipmentSlot.HEAD) {
            modifiers.removeAll(SharedMonsterAttributes.ARMOR.attributeUnlocalizedName)
            modifiers.put(SharedMonsterAttributes.ARMOR.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior armor modifier", 4.0, 0))
            modifiers.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior toughness modifier", 1.0, 0))
            modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior attack modifier", 2.0, 0))
            modifiers.put(SharedMonsterAttributes.MOVEMENT_SPEED.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior speed modifier", 0.25, 1))
            modifiers.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.attributeUnlocalizedName, AttributeModifier(WARRIOR_MODIFIER, "Warrior resistance modifier", 0.25, 0))
        }
    }

    companion object {

        private val WARRIOR_MODIFIER = UUID.fromString("35f6b4f8-03d3-41c9-b31f-fb9c893ca6da")
    }
}
