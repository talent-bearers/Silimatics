package wiresegal.silimatics;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wiresegal.silimatics.common.core.CommonProxy;
import wiresegal.silimatics.common.lib.LibMisc;

@Mod(modid = LibMisc.MODID, name = LibMisc.NAME, version = LibMisc.VERSION)
public class Silimatics {

    @Mod.Instance(LibMisc.MODID)
    public static Silimatics instance;

    @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLLog.info("Shattering Glass, boy! We're late for The Modding Trials!");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
