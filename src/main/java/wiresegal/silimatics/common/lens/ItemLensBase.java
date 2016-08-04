package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wiresegal.silimatics.api.lens.ILens;
import wiresegal.silimatics.common.core.ModItems;
import wiresegal.zenmodelloader.common.items.base.ItemMod;

public abstract class ItemLensBase extends ItemMod implements ILens {

    public static final String OCULATOR = "oculator";

    public ItemLensBase(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //todo if player is wearing the frame
        playerIn.setGlowing(true);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }


    public static class EventHandler {
        @SubscribeEvent
        public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
            if (!(event.getEntity() instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != ModItems.lensOculator) //for now
                player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(OCULATOR, true);
        }
    }


}
