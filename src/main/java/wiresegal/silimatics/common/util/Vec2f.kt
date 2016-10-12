package wiresegal.silimatics.common.util

import wiresegal.silimatics.common.block.BlockCymaticPlate

/**
 * Created by Elad on 10/12/2016.
 */
data class Vec2f(var x: Float, var y: Float) {
    override fun equals(other: Any?): Boolean
        = other is Vec2f && other.x == x && other.y == y


    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    fun round(): Vec2f {
        x = BlockCymaticPlate.round(x)
        y = BlockCymaticPlate.round(y)
        return this
    }
}