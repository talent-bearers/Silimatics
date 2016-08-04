package wiresegal.silimatics.common.core;

import net.minecraft.item.ItemArmor;
import wiresegal.silimatics.common.item.ItemLensFrames;
import wiresegal.silimatics.common.lens.ItemLens;
import wiresegal.silimatics.common.lib.LibNames;

public class ModItems {
    public static ItemLens lens;
    public static ItemLensFrames frame;
    public static void init() {
        lens = new ItemLens(LibNames.LENS);
        frame = new ItemLensFrames(LibNames.LENS_FRAMES, ItemArmor.ArmorMaterial.LEATHER);
    }
}
