package wiresegal.silimatics.common

import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.core.CommonProxy
import wiresegal.silimatics.common.lib.LibMisc

@Mod(modid = LibMisc.MODID, name = LibMisc.NAME, version = LibMisc.VERSION)
class Silimatics {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        FMLLog.info("Shattering Glass, boy! We're late for The Modding Trials!")
        proxy!!.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy!!.init(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy!!.postInit(event)
    }

    companion object {

        @Mod.Instance(LibMisc.MODID)
        var instance: Silimatics? = null

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        var proxy: CommonProxy? = null
    }
}
