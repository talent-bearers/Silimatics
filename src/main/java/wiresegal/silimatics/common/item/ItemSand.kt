package wiresegal.silimatics.common.item

import net.minecraft.client.renderer.color.IItemColor
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.common.core.IItemColorProvider
import wiresegal.zenmodelloader.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
class ItemSand(name: String) : ItemMod(name, *EnumSandType.getSandTypeNamesFor(name)), IItemColorProvider {
    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].color }
    }
}
