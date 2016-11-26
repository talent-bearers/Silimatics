package wiresegal.silimatics.common.item

import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.client.renderer.color.IItemColor
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModCreativeTab

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
class ItemSand(name: String) : ItemMod(name, *EnumSandType.getSandTypeNamesFor(name)), IItemColorProvider {

    init {
        ModCreativeTab.set(this)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].color }
    }
}
