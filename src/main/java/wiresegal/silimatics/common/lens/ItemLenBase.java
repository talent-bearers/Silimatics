package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import wiresegal.silimatics.api.lens.ILens;
import wiresegal.zenmodelloader.common.items.base.ItemMod;

public abstract class ItemLenBase extends ItemMod implements ILens {

    public ItemLenBase(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //todo if player is wearing the frame
        playerIn.setGlowing(true);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
