package me.vee.forge.tmpt;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Tmpt.MOD_ID, name = Tmpt.MOD_NAME, version = Tmpt.VERSION, dependencies = "required-after:pixelmon")
public class Tmpt {

    public static final String MOD_ID = "tmpt";
    public static final String MOD_NAME = "Too Many Pixelmon Triggers";
    public static final String VERSION = "1.0.0";

    /**
     * Register the {@link UltraBeastCaptureHandler} to Pixelmon's event bus.
     *
     * @param event FML's post initialization event
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Pixelmon.EVENT_BUS.register(new UltraBeastCaptureHandler());
    }
}
