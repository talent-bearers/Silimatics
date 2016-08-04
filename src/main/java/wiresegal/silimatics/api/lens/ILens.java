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

import java.util.List;

public interface ILens {
    void onUsingTick(World world, EntityPlayer player, ItemStack stack);

    default boolean shouldMarkAsOculator(ItemStack stack) {
        return true;
    }

    default void addTooltip(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        //NO-OP
    }

    default void addAttributes(EntityEquipmentSlot slot, ItemStack stack, Multimap<String, AttributeModifier> modifiers) {
        //NO-OP
    }
}
