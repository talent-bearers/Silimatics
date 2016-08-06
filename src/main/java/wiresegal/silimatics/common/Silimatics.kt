package wiresegal.silimatics.common

import net.minecraft.launchwrapper.Launch
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
        FMLLog.info("[Leavenworth] Shattering Glass, boy! We're late for The Modding Trials!")
        FMLLog.info("[Alcatraz] So we're right on schedule, what with your Talent.")
        FMLLog.info("[Leavenworth] Limping Lowrys, you're right! Now lad, make sure not to Break the mod! We wouldn't want any bugs, now would we?")
        FMLLog.info("[Alcatraz] So long as they're not Librarian bugs or Alivened, I think we'll be fine.")

        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
    }

    companion object {

        @Mod.Instance(LibMisc.MODID)
        lateinit var instance: Silimatics

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        lateinit var proxy: CommonProxy

        var isDevEnv: Boolean = Launch.blackboard.get("fml.deobfuscatedEnvironment") as Boolean
    }
}
