package com.gempukku.libgdx.common;

public class SimpleNumberFormatter {
    public static String format(float value, int decimalsAfterSeparator, String decimalSeparator) {
        StringBuilder result = new StringBuilder();
        if (value < 0f) {
            result.append('-');
            value = -value;
        }
        result.append((int) value);
        value -= ((int) value);
        if (value != 0f && decimalsAfterSeparator > 0) {
            result.append(decimalSeparator);
            for (int i = 0; i < decimalsAfterSeparator; i++) {
                if (i > 0 && value == 0f)
                    break;
                value *= 10f;
                result.append((int) value);
                value -= ((int) value);
            }
        }
        return result.toString();
    }

    public static String format(float value, int decimalsAfterSeparator) {
        return format(value, decimalsAfterSeparator, ".");
    }

    public static String format(float value) {
        return format(value, 7);
    }
}
