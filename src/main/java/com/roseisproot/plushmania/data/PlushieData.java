package com.roseisproot.plushmania.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PlushieData{

    boolean isPlushie;
    float sogPercentage;


    public PlushieData(boolean isPlushie, float sogPercentage) {
        this.isPlushie = isPlushie;
        this.sogPercentage = sogPercentage;
    }

    public static Codec<PlushieData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("isPlushie").forGetter(PlushieData::isPlushie),
                    Codec.FLOAT.fieldOf("sogPercentage").forGetter(PlushieData::sogPercentage)
            ).apply(instance, PlushieData::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, PlushieData> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, PlushieData>() {
        @Override
        public PlushieData decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {

            boolean isPlushie = registryFriendlyByteBuf.readBoolean();
            float sogPercentage = registryFriendlyByteBuf.readFloat();

            return new PlushieData(isPlushie, sogPercentage);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf o, PlushieData plushieData) {
            o.writeBoolean(plushieData.isPlushie());
            o.writeFloat(plushieData.sogPercentage());
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
}
