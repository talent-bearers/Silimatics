package wiresegal.silimatics.common.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.color.IItemColor;
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
import org.jetbrains.annotations.Nullable;
import wiresegal.silimatics.api.lens.ILens;
import wiresegal.silimatics.common.lens.*;
import wiresegal.silimatics.common.lib.LibMisc;
import wiresegal.zenmodelloader.client.core.TooltipHelper;
import wiresegal.zenmodelloader.common.core.IItemColorProvider;
import wiresegal.zenmodelloader.common.items.base.ItemMod;

import java.util.List;

public class ItemLens extends ItemMod implements ILens, IItemColorProvider {

    public static final String OCULATOR = LibMisc.MODID + ":oculator";

    public static ILens[] lenses = new ILens[] {
            new LensDull(),
            new LensRashid(),
            new LensBright(),
            new LensHeat(),
            new LensWindstormer(),
            new LensVoidstorm(),
            new LensTrails(),
            new LensBlood(),
            new LensPain(),
            new LensSun(),
            new LensView()
    };

    public ItemLens(String name) {
        super(name, EnumSandType.Companion.getSandTypeNamesFor(name));
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static ILens getLensFromStack(ItemStack stack) {
        if(stack != null)
            return lenses[stack.getMetadata() % lenses.length];
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        List<String> tempTip = Lists.newArrayList();
        lenses[stack.getMetadata() % lenses.length].addTooltip(stack, playerIn, tempTip, advanced);
        if (tempTip.size() > 0)
            TooltipHelper.INSTANCE.tooltipIfShift(tooltip, () -> tooltip.addAll(tempTip));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //todo if player is wearing the frame
        playerIn.setGlowing(false);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public void onUsingTick(World world, EntityPlayer player, ItemStack stack) {
        lenses[stack.getMetadata() % lenses.length].onUsingTick(world, player, stack);
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

    @Nullable
    @SideOnly(Side.CLIENT)
    @Override
    public IItemColor getItemColor() {
        return (stack, tintIndex) -> EnumSandType.values()[stack.getItemDamage() % EnumSandType.values().length].getGlassColor();
    }
}
