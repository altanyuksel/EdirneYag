package Utils;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalInputFilter implements InputFilter {
    private int decimalDigits;

    public DecimalInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String currentText = dest.toString();
        int indexOfDot = currentText.indexOf(".");

        if (indexOfDot != -1 && dstart > indexOfDot) {
            // The cursor is after the dot, which means we are editing the decimal part.
            int lengthOfDecimalPart = currentText.length() - (indexOfDot + 1);
            if (lengthOfDecimalPart >= decimalDigits) {
                // Reject input if the decimal part already has the desired number of digits.
                return "";
            }
        }

        return null; // Accept the input.
    }
}