package wiresegal.silimatics.common.item

/**
 * @author WireSegal
 * Created at 9:11 AM on 8/4/16.
 */
enum class EnumSandType(val color: Int, val chance: Double?) {
    DULL(0xFFFFFF, 0.75), RASHID(0xFFFFFF, 0.001), BRIGHT(0x70C6E0, 0.15),
    HEAT(0XE0AC70), STORM(0xA0E070), VOID(0x363D32), TRAIL(0xD3CD58),
    BLOOD(0xBF8382), PAIN(0x628E66), SUN(0xC1A57A), VIEW(0xC88AD1);

    constructor(color: Int) : this(color, null)
}
