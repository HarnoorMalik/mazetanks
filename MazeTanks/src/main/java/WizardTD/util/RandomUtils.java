package WizardTD.util;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    public static Random r = new Random();

    public static <T> T choice(List<T> items) {
        assert !items.isEmpty();

        int index = r.nextInt(items.size());
        return items.get(index);
    }

    public static <T> T choiceOrNull(List<T> items) {
        if (items.isEmpty()) return null;

        int index = r.nextInt(items.size());
        return items.get(index);
    }
}
