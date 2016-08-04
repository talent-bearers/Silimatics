package wiresegal.silimatics.common.core;

import wiresegal.silimatics.common.lens.LensOculator;
import wiresegal.silimatics.common.lib.LibNames;

public class ModItems {
    public static LensOculator lensOculator;
    public static void init() {
        lensOculator = new LensOculator(LibNames.LENS_OCULATOR);
    }
}
