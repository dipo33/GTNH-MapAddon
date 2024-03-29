package io.github.dipo33.gtmapaddon.data.entity;

import io.github.dipo33.gtmapaddon.storage.serializable.Serializable;
import net.minecraft.nbt.NBTTagCompound;

public class License implements Serializable<License>, Comparable<License> {

    public static final License INSTANCE = new License(null, null, 0);

    private final String name;
    private final Category category;
    private final int price;

    public License(String name, Category category, int price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public enum Category {
        ANIMALS, BEES, PLANTS, TREES;

        public String getName() {
            return name().toLowerCase();
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return toDisplayName(this.name);
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public static class Keys {
        public static final String NAME = "Name";
        public static final String CATEGORY = "Category";
        public static final String PRICE = "Price";
    }

    @Override
    public NBTTagCompound serialize(License item) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(Keys.NAME, item.getName());
        tag.setInteger(Keys.CATEGORY, item.getCategory().ordinal());
        tag.setInteger(Keys.PRICE, item.getPrice());

        return tag;
    }

    @Override
    public License deserialize(NBTTagCompound tag) {
        String name = tag.getString(Keys.NAME);
        Category category = Category.values()[tag.getInteger(Keys.CATEGORY)];
        int price = tag.getInteger(Keys.PRICE);

        return new License(name, category, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        License license = (License) o;

        if (!name.equalsIgnoreCase(license.name)) return false;
        return category == license.category;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public int compareTo(License other) {
        int result = Integer.compare(price, other.price);
        return result == 0 ? name.compareTo(other.name) : result;
    }
    
    public static String toDisplayName(String name) {
        String displayName = name.substring(0, 1).toUpperCase() + name.substring(1);
        return displayName.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
