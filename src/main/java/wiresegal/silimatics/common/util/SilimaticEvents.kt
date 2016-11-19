package wiresegal.silimatics.common.util

import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * Created by Elad on 11/19/2016.
 */
object SilimaticEvents {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }
    val tracker: MutableList<BlockPos> = mutableListOf()
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onWorldTick(event: TickEvent.WorldTickEvent) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote)
            tracker.clear()
    }
}