package wiresegal.silimatics.common.core;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wiresegal.silimatics.common.block.BlockGlass;
import wiresegal.silimatics.common.block.BlockSand;
import wiresegal.silimatics.common.block.BlockSifter;
import wiresegal.silimatics.common.lib.LibNames;

public class ModBlocks {
    public static BlockSifter sifter;
    public static BlockSand sand;
    public static BlockGlass glass;
    public static void init() {
        sifter = new BlockSifter();
        sand = new BlockSand("sand");
        glass = new BlockGlass("glass");
        GameRegistry.registerTileEntity(BlockGlass.TileSmedryGlass.class, "smedryglass");
    }
}
