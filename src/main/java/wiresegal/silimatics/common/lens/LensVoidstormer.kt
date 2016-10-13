package wiresegal.silimatics.common.lens

import net.minecraft.util.math.Vec3d

object LensVoidstormer : LensWindstormer() {

    override val mainDiv: Vec3d by lazy { Vec3d(-16.0, -20.0, -16.0) }
    override val secDiv: Vec3d by lazy { Vec3d(-16.0, -16.0, -16.0) }
    override val pitch: Float by lazy { 0.75F }
    override val name: String by lazy { "voidstormer" }
}
