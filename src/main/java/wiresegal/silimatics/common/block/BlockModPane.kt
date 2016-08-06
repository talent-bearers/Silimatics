package wiresegal.silimatics.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockPane
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.Loader
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.zenmodelloader.common.block.base.ItemModBlock
import wiresegal.zenmodelloader.common.core.IModBlock
import wiresegal.zenmodelloader.common.core.VariantHelper

/**
 * @author WireSegal
 * Created at 10:49 PM on 8/4/16.
 */
open class BlockModPane(name: String, material: Material, canDrop: Boolean, vararg variants: String) : BlockPane(material, canDrop), IModBlock {
    override val variants: Array<out String>

    override val bareName: String = name
    val modId: String

    val itemForm: ItemBlock? by lazy { createItemForm() }

    init {
        modId = Loader.instance().activeModContainer().modId
        this.variants = VariantHelper.beginSetupBlock(name, variants)
        VariantHelper.finishSetupBlock(this, name, itemForm)
        ModCreativeTab.set(this)
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        VariantHelper.setUnlocalizedNameForBlock(this, modId, name, itemForm)
        return super.setUnlocalizedName(name)
    }

    open fun createItemForm(): ItemBlock? {
        return ItemModBlock(this)
    }
}
