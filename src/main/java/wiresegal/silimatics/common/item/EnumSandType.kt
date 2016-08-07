package wiresegal.silimatics.common.item

import net.minecraft.util.IStringSerializable

/**
 * @author WireSegal
 * Created at 9:11 AM on 8/4/16.
 */
enum class EnumSandType(val color: Int, val glassColor: Int, val chance: Double? = null) : IStringSerializable {
    DULL(0xFEFEFE, 0xFEFEFE, 0.6), RASHID(0xFEFEFE, 0xFEFEFE, 0.01), BRIGHT(0x70C6E0, 0xE086D1, 0.15),
    HEAT(0xE0AC70, 0xE0AC70), STORM(0xA0E070, 0xA0E070), VOID(0x363D32, 0x363D32), TRAIL(0xD3CD58, 0xEA605B),
    BLOOD(0xBF8382, 0x000000), PAIN(0x628E66, 0x314433), SUN(0xC1A57A, 0xF2C146)/*, VIEW(0xC88AD1, 0x42C9DD)*/;

    override fun toString(): String {
        return this.name.toLowerCase().split("_").joinToString("", transform = { it.capitalizeFirst() }).lowercaseFirst()
    }

    override fun getName(): String {
        return this.name.toLowerCase()
    }

    companion object {
        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }

        fun String.lowercaseFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).toLowerCase() + this.slice(1..this.length - 1)
        }

        val sandTypeNames: Array<String> by lazy {
            EnumSandType.values().map { it.toString() }.toTypedArray()
        }

        fun getSandTypeNamesFor(prefix: String): Array<String> = EnumSandType.values().map {
            prefix + it.toString().capitalizeFirst()
        }.toTypedArray()
    }
}
