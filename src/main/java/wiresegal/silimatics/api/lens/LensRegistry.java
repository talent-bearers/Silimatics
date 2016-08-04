package wiresegal.silimatics.api.lens;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LensRegistry {

    private HashBiMap<ResourceLocation, ILens> registry = HashBiMap.create(512);

    private static LensRegistry theRegistry = new LensRegistry();

    public static LensRegistry getRegistry() {
        return theRegistry;
    }

    
    public Class<ILens> getRegistrySuperType() {
        return ILens.class;
    }

    
    public void register(ResourceLocation rl, ILens value) {
        registry.put(rl, value);
    }

    
    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    
    public boolean containsValue(ILens value) {
        return registry.containsValue(value);
    }

    
    public ILens getValue(ResourceLocation key) {
        return registry.get(key);
    }

    
    public ResourceLocation getKey(ILens value) {
        return registry.inverse().get(value);
    }

    
    public Set<ResourceLocation> getKeys() {
        return registry.keySet();
    }

    
    public List<ILens> getValues() {
        return Lists.newArrayList(registry.values());
    }

    
    public Set<Map.Entry<ResourceLocation, ILens>> getEntries() {
        return registry.entrySet();
    }

    
    public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type) {
        return null; //no idea what that is, to be honest
    }

    
    public Iterator<ILens> iterator() {
        return registry.values().iterator();
    }
}
