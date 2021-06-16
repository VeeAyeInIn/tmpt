package me.vee.forge.tmpt;

import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UltraBeastCaptureHandler {

    public final UltraBeastCaptureTrigger ULTRABEAST_CAPTURE_TRIGGER = CriteriaTriggers.register(new UltraBeastCaptureTrigger());

    @SubscribeEvent
    public void onCapture(PixelmonReceivedEvent e) {
        if (e.receiveType == ReceiveType.PokeBall && e.pokemon.getSpecies().isUltraBeast()) {
            ULTRABEAST_CAPTURE_TRIGGER.trigger(e.player, e.pokemon.getSpecies());
        }
    }
}
