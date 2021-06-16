package me.vee.forge.tmpt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used in conjunction with {@link UltraBeastCaptureHandler}, to activate once a Pixelmon has been caught, and is
 * confirmed to be an Ultra Beast.
 *
 * @see com.pixelmonmod.pixelmon.api.advancements.triggers.LegendaryCaptureTrigger
 */
@SuppressWarnings("NullableProblems")
public class UltraBeastCaptureTrigger implements ICriterionTrigger<UltraBeastCaptureTrigger.Instance> {
    
    private static final ResourceLocation ID = new ResourceLocation("tmpt:ultrabeast_capture_trigger");
    private final Map<PlayerAdvancements, UltraBeastCaptureTrigger.Listeners> listeners = Maps.newHashMap();

    public UltraBeastCaptureTrigger() {}

    public ResourceLocation getId() {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<UltraBeastCaptureTrigger.Instance> listener) {
        UltraBeastCaptureTrigger.Listeners ultrabeastcapturetrigger$Listeners = this.listeners.get(playerAdvancementsIn);
        if (ultrabeastcapturetrigger$Listeners == null) {
            this.listeners.put(playerAdvancementsIn,
                    ultrabeastcapturetrigger$Listeners = new UltraBeastCaptureTrigger.Listeners(playerAdvancementsIn));
        }
        ultrabeastcapturetrigger$Listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<UltraBeastCaptureTrigger.Instance> listener) {
        UltraBeastCaptureTrigger.Listeners ultrabeastcapturetrigger$Listeners = this.listeners.get(playerAdvancementsIn);
        if (ultrabeastcapturetrigger$Listeners != null) {
            ultrabeastcapturetrigger$Listeners.remove(listener);
            if (ultrabeastcapturetrigger$Listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public UltraBeastCaptureTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        String pokemon = json.has("pokemon") ? JsonUtils.getString(json, "pokemon") : "";
        if (EnumSpecies.getFromNameAnyCase(pokemon) == null && !pokemon.equalsIgnoreCase("ultrabeast")) {
            Pixelmon.LOGGER.error("Invalid Pokémon name for " + this.getId().toString());
        }
        EnumSpecies pixelmon = EnumSpecies.getFromNameAnyCase(pokemon);
        return new UltraBeastCaptureTrigger.Instance(this.getId(), pixelmon, pokemon);
    }

    public void trigger(EntityPlayerMP player, EnumSpecies pokemon) {
        UltraBeastCaptureTrigger.Listeners ultrabeastcapturetrigger$Listeners = this.listeners.get(player.getAdvancements());
        if (ultrabeastcapturetrigger$Listeners != null) {
            ultrabeastcapturetrigger$Listeners.trigger(pokemon);
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<UltraBeastCaptureTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<UltraBeastCaptureTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<UltraBeastCaptureTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(EnumSpecies pokemon) {
            List<Listener<UltraBeastCaptureTrigger.Instance>> list = null;
            Iterator<Listener<Instance>> var3;
            Listener<Instance> listener;
            for(var3 = this.listeners.iterator(); var3.hasNext(); list.add(listener)) {
                listener = var3.next();
                if (list == null) {
                    list = Lists.newArrayList();
                }
            }
            if (list != null) {
                var3 = list.iterator();
                while(var3.hasNext()) {
                    listener = var3.next();
                    if ((listener.getCriterionInstance()).test(pokemon)) {
                        listener.grantCriterion(this.playerAdvancements);
                    }
                }
            }
        }
    }

    public static class Instance extends AbstractCriterionInstance {

        EnumSpecies pokemon;
        String name;

        public Instance(ResourceLocation criterionIn, EnumSpecies pokemon, String name) {
            super(criterionIn);
            this.pokemon = pokemon;
            this.name = name;
        }

        public boolean test(EnumSpecies pokemon) {
            if (this.pokemon == null && this.name.equalsIgnoreCase("ultrabeast") && EnumSpecies.ultrabeasts.contains(pokemon.name)) {
                return true;
            } else {
                return this.pokemon == pokemon;
            }
        }
    }
}