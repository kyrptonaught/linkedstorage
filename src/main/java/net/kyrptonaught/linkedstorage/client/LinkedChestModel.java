package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class LinkedChestModel extends Model {
    private final ModelPart lid;
    protected ModelPart base;
    public final ModelPart latch;
    public ModelPart button1, button2, button3;

    public LinkedChestModel(BlockEntityRendererFactory.Context ctx) {
        super(RenderLayer::getEntityCutout);
        ModelPart modelPart = ctx.getLayerModelPart(LinkedStorageModClient.LINKEDCHESTMODELLAYER);
        this.base = modelPart.getChild("bottom");
        this.lid = modelPart.getChild("lid");
        this.latch = modelPart.getChild("lock");
        this.button1 = modelPart.getChild("color1");
        this.button2 = modelPart.getChild("color2");
        this.button3 = modelPart.getChild("color3");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), ModelTransform.NONE);
        modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
        modelPartData.addChild("lock", ModelPartBuilder.create().uv(0, 0).cuboid(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 8.0F, 0.0F));
        modelPartData.addChild("color1", ModelPartBuilder.create().uv(0, 19).cuboid(4, 5, 5, 2, 1, 4), ModelTransform.pivot(0, 9f, 1f));
        modelPartData.addChild("color2", ModelPartBuilder.create().uv(0, 19).cuboid(7, 5, 5, 2, 1, 4), ModelTransform.pivot(0, 9f, 1f));
        modelPartData.addChild("color3", ModelPartBuilder.create().uv(0, 19).cuboid(10, 5, 5, 2, 1, 4), ModelTransform.pivot(0, 9f, 1f));

        return TexturedModelData.of(modelData, 64, 64);
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