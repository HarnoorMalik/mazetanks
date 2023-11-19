package WizardTD.util;

public class Utils {
    public static float progressToMultiplier(int current, int max) {
        if (current <= 0) return 0f;
        if (current >= max) return 1f;
        return (float) current / max;
    }
}
