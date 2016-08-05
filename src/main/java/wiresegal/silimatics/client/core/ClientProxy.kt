package wiresegal.silimatics.client.core

import net.minecraft.client.renderer.block.statemap.IStateMapper
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockGlassPane
import wiresegal.silimatics.common.core.CommonProxy
import wiresegal.silimatics.common.core.ModBlocks

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        ModelLoader.setCustomStateMapper(ModBlocks.glassPane,
                StateMap.Builder()
                        .withName(BlockGlass.SAND_TYPE)
                        .withSuffix("GlassPane").build())
    }
}

