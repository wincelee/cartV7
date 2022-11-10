package manu.apps.cartv7.ui.main.classes;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class InputFilterMinMax implements InputFilter {

    private int min, max;
    private Context context;

    public InputFilterMinMax(int min, int max, Context context) {
        this.min = min;
        this.max = max;
        this.context = context;
    }

    public InputFilterMinMax(String min, String max, Context context) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
        this.context = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }

        Toast.makeText(context, "Input is greater than Maximum Quantity", Toast.LENGTH_SHORT).show();

        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
