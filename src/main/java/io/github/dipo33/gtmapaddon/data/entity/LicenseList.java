package io.github.dipo33.gtmapaddon.data.entity;

import io.github.dipo33.gtmapaddon.data.entity.License.Category;
import io.github.dipo33.gtmapaddon.storage.serializable.Serializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LicenseList implements Serializable<LicenseList> {

    private final Map<Category, List<License>> licenses;
    private final Map<UUID, List<License>> ownedLicenses;

    public LicenseList() {
        licenses = new ConcurrentHashMap<>();
        ownedLicenses = new ConcurrentHashMap<>();
    }

    private List<License> getLicenses(Category category) {
        return licenses.computeIfAbsent(category, x -> new ArrayList<>());
    }

    private Collection<License> getAll() {
        return licenses.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Collection<UUID> getAllOwners() {
        return ownedLicenses.keySet();
    }

    public List<License> getLicenses(UUID owner) {
        return ownedLicenses.computeIfAbsent(owner, x -> new ArrayList<>());
    }

    public boolean addLicense(License license) {
        return getLicenses(license.getCategory()).add(license);
    }

    public boolean removeLicense(License license) {
        return getLicenses(license.getCategory()).remove(license);
    }

    public void assignLicense(License license, UUID owner) {
        getLicenses(owner).add(license);
    }

    public void revokeLicense(License license, UUID owner) {
        getLicenses(owner).remove(license);
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
        for (License license : item.getAll()) {
            tagLicences.appendTag(License.INSTANCE.serialize(license));
        }

        NBTTagList tagOwnedLicenses = new NBTTagList();
        for (UUID owner : item.getAllOwners()) {
            NBTTagCompound tagOwner = new NBTTagCompound();

            NBTTagList tagOwnerLicenses = new NBTTagList();
            for (License license : item.getLicenses(owner)) {
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
