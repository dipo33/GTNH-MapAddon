package io.github.dipo33.gtmapaddon.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sk.dipo.money.MoneyMod;

public class MoneyModWrapper {


    public static String priceToString(int price) {
        String euro = Integer.toString(price / 100);
        String cent = Integer.toString(price % 100);

        if ((price % 100) < 10) {
            cent = "0" + cent;
        }

        return euro + "." + cent;
    }

    public static int stringToPrice(String priceString) {
        if (priceString.contains(".")) {
            String[] parts = priceString.split("\\.");
            if (dotCount(priceString) > 1) {
                throw new NumberFormatException("More than one dot is not allowed");
            } else if (parts[1].length() > 2) {
                throw new NumberFormatException("Only two decimal points are allowed");
            }

            int euro = Integer.parseInt(parts[0]);
            int cent = Integer.parseInt(parts[1]);
            cent = parts[1].length() == 2 ? cent : cent * 10;

            return euro * 100 + cent;
        } else {
            return Integer.parseInt(priceString) * 100;
        }
    }

    public static boolean chargeMoney(ItemStack creditCard, String pinCode, int value) {
        NBTTagCompound tag = creditCard.getTagCompound();
        String actualPinCode = tag.getString("PIN");
        if (!actualPinCode.equalsIgnoreCase(pinCode)) {
            return false;
        }

        int balance = MoneyMod.db.getInteger("Players", tag.getString("OwnerUUID") + ".Balance");
        if (balance < value) {
            return false;
        }

        MoneyMod.db.set("Players", tag.getString("OwnerUUID") + ".Balance", balance - value);
        return true;
    }

    private static int dotCount(String str) {
        int dotCount = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '.') {
                ++dotCount;
            }
        }

        return dotCount;
    }
}
