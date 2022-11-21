package io.github.dipo33.gtmapaddon.data.entity;

import io.github.dipo33.gtmapaddon.storage.serializable.Serializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrizeList implements Serializable<PrizeList> {

    private final Map<Integer, Map<OwnedChunk.Status, Integer>> prices;

    public PrizeList() {
        this.prices = new ConcurrentHashMap<>();
    }

    public int getPrice(int dimensionId, OwnedChunk.Status status) {
        return prices.computeIfAbsent(dimensionId, (x) -> new ConcurrentHashMap<>()).getOrDefault(status, -1);
    }

    public void setPrice(int dimensionId, OwnedChunk.Status status, int price) {
        prices.computeIfAbsent(dimensionId, (x) -> new ConcurrentHashMap<>()).put(status, price);
    }

    private static class Keys {
        public static final String DIMENSIONS = "Dimensions";
        public static final String DIMENSION_ID = "DimensionID";
        public static final String PRICES = "Prices";
    }

    @Override
    public NBTTagCompound serialize(PrizeList item) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList tagDimensions = new NBTTagList();

        for (Map.Entry<Integer, Map<OwnedChunk.Status, Integer>> dimEntry : prices.entrySet()) {
            NBTTagCompound tagDimension = new NBTTagCompound();
            NBTTagCompound tagPrices = new NBTTagCompound();

            for (Map.Entry<OwnedChunk.Status, Integer> priceEntry : dimEntry.getValue().entrySet()) {
                tagPrices.setInteger(priceEntry.getKey().name(), priceEntry.getValue());
            }

            tagDimension.setInteger(Keys.DIMENSION_ID, dimEntry.getKey());
            tagDimension.setTag(Keys.PRICES, tagPrices);
            tagDimensions.appendTag(tagDimension);
        }

        tag.setTag(Keys.DIMENSIONS, tagDimensions);
        return tag;
    }

    @Override
    public PrizeList deserialize(NBTTagCompound tag) {
        NBTTagList tagDimensions = tag.getTagList(Keys.DIMENSIONS, Constants.NBT.TAG_COMPOUND);
        PrizeList prizeList = new PrizeList();

        for (int i = 0; i < tagDimensions.tagCount(); ++i) {
            NBTTagCompound tagDimension = tagDimensions.getCompoundTagAt(i);
            NBTTagCompound tagPrices = tagDimension.getCompoundTag(Keys.PRICES);
            int dimensionId = tagDimension.getInteger(Keys.DIMENSION_ID);

            for (OwnedChunk.Status status : OwnedChunk.Status.values()) {
                if (!tagPrices.hasKey(status.name())) continue;

                int price = tagPrices.getInteger(status.name());
                prizeList.setPrice(dimensionId, status, price);
            }
        }

        return prizeList;
    }
}
