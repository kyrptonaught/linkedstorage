package net.kyrptonaught.linkedstorage.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.kyrptonaught.linkedstorage.client.EnderStorageResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

    @Shadow
    @Final
    private ResourcePackProfile.Factory<ResourcePackProfile> profileFactory;

    @Inject(method = "providePackProfiles", at = @At(value = "RETURN"), cancellable = true)
    public void ResourcePackManager(CallbackInfoReturnable<Map<String, ResourcePackProfile>> cir) {
        Map<String, ResourcePackProfile> packs = Maps.newTreeMap();
        packs.putAll(cir.getReturnValue());
        new EnderStorageResourcePackProvider().register((resourcePackProfile) -> packs.put(resourcePackProfile.getName(), resourcePackProfile), this.profileFactory);
        cir.setReturnValue(ImmutableMap.copyOf(packs));
    }
}
