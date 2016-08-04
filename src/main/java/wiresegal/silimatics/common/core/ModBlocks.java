package wiresegal.silimatics.common.core;

import wiresegal.silimatics.common.block.BlockSifter;
import wiresegal.silimatics.common.lib.LibNames;

public class ModBlocks {
    public static BlockSifter sifter;
    public static void init() {
        sifter = new BlockSifter();
    }
}
