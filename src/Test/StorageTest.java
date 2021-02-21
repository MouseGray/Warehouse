package Test;

import Storage.*;
import org.junit.*;

import java.util.List;

public class StorageTest {
    @Test
    public void FullStorageTest () {
        Storage storage = new Storage();

        // Filters
        String first_filter = "ab.*";
        String second_filter = "[a-z]*";

        String[] filters = { first_filter, second_filter };

        List<ItemStack> data = storage.getData();
        Assert.assertEquals("New Storage is not empty.", data.size(), 0);

        // Add incorrect item by tow filters
        storage.add(filters, new Item("546", "home"));

        data = storage.getData();
        Assert.assertEquals("Incorrect data is added.", data.size(), 0);

        // Add incorrect item by one filters
        storage.add(filters, new Item("zzz", "home"));

        data = storage.getData();
        Assert.assertEquals("Incorrect data is added.", data.size(), 0);

        // Add correct item
        storage.add(filters, new Item("abcd", "home"));

        data = storage.getData();
        Assert.assertEquals("Correct data is not added.", data.size(), 1);

        // Add new item
        storage.add(filters, new Item("abcdc", "home"));

        data = storage.getData();
        Assert.assertEquals("Correct data is not added.", data.size(), 2);

        // Add exist item
        storage.add(filters, new Item("abcd", "home"), 3);

        data = storage.getData();
        Assert.assertEquals("Incorrect add exist item.", data.size(), 2);

        data = storage.getData();
        Assert.assertEquals("Incorrect count add exist item.", data.get(1).getCount(), 4);

        // Remove not exist item
        storage.remove("jjj");

        data = storage.getData();
        Assert.assertEquals("Incorrect remove not exist item.", data.size(), 2);

        // Remove stacked item
        storage.remove("abcd");

        data = storage.getData();
        Assert.assertEquals("Incorrect remove stacked item.", data.size(), 2);

        data = storage.getData();
        Assert.assertEquals("Incorrect count add exist item.", data.get(1).getCount(), 3);

        // Remove last item
        storage.remove("abcdc");

        data = storage.getData();
        Assert.assertEquals("Incorrect remove last item.", data.size(), 1);

        // Find not exist item
        ItemStack jjj = storage.find("jjj");

        Assert.assertNull("Incorrect find not exist item.", jjj);

        // Find exist item
        ItemStack abcd = storage.find("abcd");

        Assert.assertEquals("Incorrect find not exist item.", abcd.getItem().getName(), "abcd");
    }
}
