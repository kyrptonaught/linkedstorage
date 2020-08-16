package net.kyrptonaught.linkedstorage.client;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

import java.util.function.Consumer;

public class EnderStorageResourcePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        InputStreamResourcePack pack = new InputStreamResourcePack("/resourcepacks/enderstorage.zip") {
            public String getName() {
                return "EnderStorage for LinkedStorage";
            }
        };

        ResourcePackProfile resourcePackProfile2 = ResourcePackProfile.of(LinkedStorageMod.MOD_ID + ":enderstorage", false, () -> pack, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (resourcePackProfile2 != null) {
            consumer.accept(resourcePackProfile2);
        }
    }
}
