package wiresegal.silimatics.api.lens;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import wiresegal.silimatics.common.core.ItemNBTHelper;
import wiresegal.silimatics.common.item.ItemLens;

import javax.annotation.Nonnull;
import java.util.List;

public interface ILens {
    default void onUsingTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        //NO-OP
    }

    default boolean shouldMarkAsOculator(@Nonnull ItemStack stack) {
        return true;
    }

    default void addTooltip(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull List<String> tooltip, boolean advanced) {
        //NO-OP
    }

    default void addAttributes(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack, @Nonnull Multimap<String, AttributeModifier> modifiers) {
        //NO-OP
    }
}
