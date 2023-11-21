package com.ririthenerd.bike;

import com.ririthenerd.Bikes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BikeEntityRenderer<T extends PassiveEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(Bikes.MODID, "textures/entity/bike/bike.png");
    protected final EntityModel<T> model;
    public BikeEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer) {
        super(ctx);
        this.shadowRadius = 0.7F;
        this.model = (EntityModel<T>) new BikeEntityModel(ctx.getPart(layer));
    }

    public void render(T baseBikeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(baseBikeEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        float o = MathHelper.lerp(g, baseBikeEntity.prevPitch, baseBikeEntity.getPitch());
        matrixStack.translate(0.0F, 1.05F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-o));
        matrixStack.scale(-0.7F, -0.7F, 0.7F);
        this.model.setAngles(baseBikeEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(baseBikeEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    public Identifier getTexture(T baseBikeEntity) {
        return TEXTURE;
    }
}
