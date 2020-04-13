package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class LinkedChestModel extends Model {
    private final ModelPart lid;
    protected ModelPart base;
    public final ModelPart latch;
    public ModelPart button1, button2, button3;

    public LinkedChestModel() {
        super(RenderLayer::getEntityCutout);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.base = new ModelPart(64, 64, 0, 19); // 818
        this.base.addCuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.lid = new ModelPart(64, 64, 0, 0);
        this.lid.addCuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);//817
        this.lid.pivotY = 9.0F;
        this.lid.pivotZ = 1.0F;
        this.latch = new ModelPart(64, 64, 0, 0);
        this.latch.addCuboid(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F); //819
        this.latch.pivotY = 8.0F;
        button1 = new ModelPart(64, 64, 0, 19);
        button2 = new ModelPart(64, 64, 0, 19);
        button3 = new ModelPart(64, 64, 0, 19);
        button1.addCuboid(4, 5, 5, 2, 1, 4, 0);
        button2.addCuboid(7, 5, 5, 2, 1, 4, 0);
        button3.addCuboid(10, 5, 5, 2, 1, 4, 0);
        button1.pivotY = 9f;
        button1.pivotZ = 1f;
        button2.pivotY = 9f;
        button2.pivotZ = 1f;
        button3.pivotY = 9f;
        button3.pivotZ = 1f;
    }

    public void setLidPitch(float pitch) {
        pitch = 1.0f - pitch;
        button1.pitch = button2.pitch = button3.pitch = latch.pitch = lid.pitch = -((1.0F - pitch * pitch * pitch) * 1.5707964F);
    }

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int j) {
        render(matrixStack, vertexConsumer, i, j, 1, 1, 1, 1);
    }

    @Override
    public void render(MatrixStack stack, VertexConsumer consumer, int i, int j, float r, float g, float b, float f) {
        base.render(stack, consumer, i, j);
        lid.render(stack, consumer, i, j);
    }
}