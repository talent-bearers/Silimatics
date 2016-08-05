package wiresegal.silimatics.api.lens;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
    
    default Vec3d addPlayerLook(@Nonnull EntityPlayer player, BlockPos pos) {
        if (player.getLookVec().xCoord < 0.5 && player.getLookVec().xCoord > -0.5 && player.getLookVec().zCoord < -0.5) {//north, -z
            return new Vec3d(pos.getX(), pos.getY(), pos.getZ() - 1.5);

        } else if (player.getLookVec().zCoord < 0.5 && player.getLookVec().zCoord > -0.5 && player.getLookVec().zCoord > 0.5) {//east, +x
            return new Vec3d(pos.getX() + 1.5, pos.getY(), pos.getZ());

        } else if (player.getLookVec().xCoord < 0.5 && player.getLookVec().xCoord > -0.5 && player.getLookVec().zCoord > 0.5) {//south +z
            return new Vec3d(pos.getX(), pos.getY(), pos.getZ() + 1.5);

        } else {
            return new Vec3d(pos.getX() - 1.5, pos.getY(), pos.getZ());

        }
    }
}
