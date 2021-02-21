package Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Storage {
    HashMap<String, ItemStack> data = new HashMap<>();

    public boolean add(String[] filters, Item item) {
        return add(filters, item, 1);
    }

    public boolean add(String[] filters, Item item, int count) {
        for (String s: filters)
            if (!item.getName().matches(s)) return false;

        ItemStack existedStack = data.get(item.getName());
        if (existedStack == null) {
            data.put(item.getName(), new ItemStack(item, count));
            return true;
        }
        if (!existedStack.getItem().equals(item)) return false;
        existedStack.increase(count);
        return true;
    }

    public boolean remove(String name) {
        ItemStack existedStack = data.get(name);
        if (existedStack == null) return false;
        existedStack.decrease();
        if (existedStack.isEmpty()) data.remove(name);
        return true;
    }

    public List<ItemStack> getData() {
        return new ArrayList<>(data.values());
    }

    public ItemStack find(String name) {
        return data.get(name);
    }

    public List<ItemStack> filter(String name) {
        return data.values().stream()
                .filter(x -> x.getItem().getName().matches(name + ".*"))
                .collect(Collectors.toList());
    }
}
