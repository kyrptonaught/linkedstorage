package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.MathHelper;

@EnvironmentInterfaces({@EnvironmentInterface(value = EnvType.CLIENT, itf = ChestAnimationProgress.class), @EnvironmentInterface(value = EnvType.CLIENT, itf = Tickable.class)})
public class OpenableBlockEntity extends BlockEntity implements ChestAnimationProgress, Tickable {
    OpenableBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Environment(EnvType.CLIENT)
    protected int countViewers() {
        return 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, lastAnimationAngle, animationAngle);
    }

    private float animationAngle;
    private float lastAnimationAngle;

    @Override
    @Environment(EnvType.CLIENT)
    public void tick() {
        System.out.println(world.isClient);
        if (world != null && world.isClient) {
            int viewerCount = countViewers();
            lastAnimationAngle = animationAngle;
            if (viewerCount > 0 && animationAngle == 0.0F) playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN);
            if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F) {
                float float_2 = animationAngle;
                if (viewerCount > 0) animationAngle += 0.1F;
                else animationAngle -= 0.1F;
                animationAngle = MathHelper.clamp(animationAngle, 0, 1);
                if (animationAngle < 0.5F && float_2 >= 0.5F) playSound(SoundEvents.BLOCK_ENDER_CHEST_CLOSE);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void playSound(SoundEvent soundEvent) {
        double d = (double) this.pos.getX() + 0.5D;
        double e = (double) this.pos.getY() + 0.5D;
        double f = (double) this.pos.getZ() + 0.5D;
        this.world.playSound(d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F, false);
    }
}
