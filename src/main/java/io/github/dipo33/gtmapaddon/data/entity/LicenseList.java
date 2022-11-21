package io.github.dipo33.gtmapaddon.data.entity;

import io.github.dipo33.gtmapaddon.data.entity.License.Category;
import io.github.dipo33.gtmapaddon.storage.serializable.Serializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LicenseList implements Serializable<LicenseList> {

    private final Map<Category, Map<String, License>> licenses;
    private final Map<UUID, List<License>> playerLicenses;

    public LicenseList() {
        licenses = new ConcurrentHashMap<>();
        playerLicenses = new ConcurrentHashMap<>();
    }

    private Map<String, License> getLicenses(Category category) {
        return licenses.computeIfAbsent(category, x -> new ConcurrentHashMap<>());
    }

    public List<License> getPlayerLicenses(UUID owner) {
        return playerLicenses.computeIfAbsent(owner, x -> new CopyOnWriteArrayList<>());
    }

    public License getLicense(Category category, String name) {
        return getLicenses(category).get(name);
    }

    public Collection<License> getAllPlayerLicenses(Category category, UUID owner) {
        return getPlayerLicenses(owner).stream()
                .filter(license -> category == null || license.getCategory() == category)
                .sorted()
                .collect(Collectors.toList());
    }

    public Collection<License> getAllPlayerLicenses(UUID owner) {
        return getAllPlayerLicenses(null, owner);
    }

    public Collection<License> getAllLicenses(Category category) {
        return licenses.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(license -> category == null || license.getCategory() == category)
                .sorted()
                .collect(Collectors.toList());
    }

    public Collection<License> getAllLicenses() {
        return getAllLicenses(null);
    }

    private Collection<UUID> getAllOwners() {
        return playerLicenses.keySet();
    }

    public License addLicense(License license) {
        return getLicenses(license.getCategory()).putIfAbsent(license.getName(), license);
    }

    public License removeLicense(Category category, String name) {
        return getLicenses(category).remove(name);
    }

    public void assignLicense(License license, UUID owner) {
        getPlayerLicenses(owner).add(license);
    }

    public void revokeLicense(License license, UUID owner) {
        getPlayerLicenses(owner).remove(license);
    }

    public boolean hasLicense(License license, UUID owner) {
        return getPlayerLicenses(owner).contains(license);
    }

    public boolean hasLicense(License license) {
        return playerLicenses.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(license::equals);
    }

    public static class Keys {
        public static final String LICENSES = "Licenses";
        public static final String OWNED_LICENSES = "OwnedLicenses";
        public static final String OWNER = "Owner";
    }

    @Override
    public NBTTagCompound serialize(LicenseList item) {
        NBTTagCompound tag = new NBTTagCompound();

        NBTTagList tagLicences = new NBTTagList();
        for (License license : item.getAllLicenses()) {
            tagLicences.appendTag(License.INSTANCE.serialize(license));
        }

        NBTTagList tagOwnedLicenses = new NBTTagList();
        for (UUID owner : item.getAllOwners()) {
            NBTTagCompound tagOwner = new NBTTagCompound();

            NBTTagList tagOwnerLicenses = new NBTTagList();
            for (License license : item.getPlayerLicenses(owner)) {
                tagOwnerLicenses.appendTag(License.INSTANCE.serialize(license));
            }

            tagOwner.setTag(Keys.LICENSES, tagOwnerLicenses);
            tagOwner.setString(Keys.OWNER, owner.toString());
            tagOwnedLicenses.appendTag(tagOwner);
        }

        tag.setTag(Keys.LICENSES, tagLicences);
        tag.setTag(Keys.OWNED_LICENSES, tagOwnedLicenses);

        return tag;
    }

    @Override
    public LicenseList deserialize(NBTTagCompound tag) {
        LicenseList licenseList = new LicenseList();

        NBTTagList tagLicenses = tag.getTagList(Keys.LICENSES, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagLicenses.tagCount(); ++i) {
            NBTTagCompound tagLicense = tagLicenses.getCompoundTagAt(i);
            licenseList.addLicense(License.INSTANCE.deserialize(tagLicense));
        }

        NBTTagList tagOwnedLicenses = tag.getTagList(Keys.OWNED_LICENSES, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagOwnedLicenses.tagCount(); ++i) {
            NBTTagCompound tagOwner = tagOwnedLicenses.getCompoundTagAt(i);
            UUID owner = UUID.fromString(tagOwner.getString(Keys.OWNER));

            NBTTagList tagOwnerLicenses = tagOwner.getTagList(Keys.LICENSES, Constants.NBT.TAG_COMPOUND);
            for (int j = 0; j < tagOwnerLicenses.tagCount(); ++j) {
                NBTTagCompound tagLicense = tagOwnerLicenses.getCompoundTagAt(j);
                licenseList.assignLicense(License.INSTANCE.deserialize(tagLicense), owner);
            }
        }

        return licenseList;
    }
}
