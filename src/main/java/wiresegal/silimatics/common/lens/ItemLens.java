package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wiresegal.silimatics.api.lens.ILens;
import wiresegal.silimatics.common.item.EnumSandType;
import wiresegal.silimatics.common.item.ItemLensFrames;
import wiresegal.silimatics.common.lib.LibMisc;
import wiresegal.zenmodelloader.client.core.TooltipHelper;
import wiresegal.zenmodelloader.common.items.base.ItemMod;

import java.util.List;

public class ItemLens extends ItemMod implements ILens {

    public static final String OCULATOR = LibMisc.MODID + ":oculator";

    public static ILens[] lenses = new ILens[] {
            new LensWindstormer()
    };

    public ItemLens(String name) {
        super(name, EnumSandType.Companion.getSandTypeNames());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        TooltipHelper.INSTANCE.tooltipIfShift(tooltip, () -> {
            lenses[stack.getMetadata()].addTooltip(stack, playerIn, tooltip, advanced);
        });
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //todo if player is wearing the frame
        playerIn.setGlowing(true);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public void onUsingTick(World world, EntityPlayer player, ItemStack stack) {
        lenses[stack.getMetadata()].onUsingTick(world, player, stack);
    }


    public static class EventHandler {
        @SubscribeEvent
        public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
            if (!(event.getEntity() instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ItemStack headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (headStack != null && headStack.getItem() instanceof ItemLensFrames) {
                ItemStack lensStack = ItemLensFrames.Companion.getLensStack(headStack);
                if (lensStack != null && lensStack.getItem() instanceof ILens && ((ILens) lensStack.getItem()).shouldMarkAsOculator(lensStack))
                    player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(OCULATOR, true);
            }
        }
    }


}
