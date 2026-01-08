package com.roseisproot.plushmania.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PlushieData{

    boolean isPlushie;
    float sogPercentage;
    int color;


    public PlushieData(boolean isPlushie, float sogPercentage, int color) {
        this.isPlushie = isPlushie;
        this.sogPercentage = sogPercentage;
        this.color = color;
    }

    public static Codec<PlushieData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("isPlushie").forGetter(PlushieData::isPlushie),
                    Codec.FLOAT.fieldOf("sogPercentage").forGetter(PlushieData::sogPercentage),
                    Codec.INT.fieldOf("color").forGetter(PlushieData::getColor)
            ).apply(instance, PlushieData::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, PlushieData> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, PlushieData>() {
        @Override
        public PlushieData decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {

            boolean isPlushie = registryFriendlyByteBuf.readBoolean();
            float sogPercentage = registryFriendlyByteBuf.readFloat();
            int color = registryFriendlyByteBuf.readInt();

            return new PlushieData(isPlushie, sogPercentage, color);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf o, PlushieData plushieData) {
            o.writeBoolean(plushieData.isPlushie());
            o.writeFloat(plushieData.sogPercentage());
            o.writeInt(plushieData.getColor());
        }
    };

    public boolean isPlushie() {
        return isPlushie;
    }

    public PlushieData setPlushie(boolean plushie) {
        isPlushie = plushie;
        return this;
    }

    public float sogPercentage() {
        return sogPercentage;
    }

    public PlushieData setSogPercentage(float sogPercentage) {
        this.sogPercentage = sogPercentage;
        return this;
    }

    public int getColor() {
        return color;
    }

    public PlushieData setColor(int color) {
        this.color = color;
        return this;
    }
}
