package wiresegal.silimatics.common.item

import com.teamwizardry.librarianlib.common.base.item.ItemModArmor
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import wiresegal.silimatics.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:43 PM on 8/6/16.
 */
class ItemGrapplerBoots(name: String) : ItemModArmor(name, ArmorMaterial.CHAIN, EntityEquipmentSlot.FEET) {
    override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        return "${LibMisc.MODID}:textures/models/grapplerBoots.png"
    }
}
