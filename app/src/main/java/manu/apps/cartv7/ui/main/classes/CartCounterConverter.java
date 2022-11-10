package manu.apps.cartv7.ui.main.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import manu.apps.cartv7.R;

public class CartCounterConverter {

    public static Drawable convertLayoutToImage (Context context, int count, int drawableId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_cart_counter, null);
        ((ImageView)view.findViewById(R.id.menu_cart_icon)).setImageResource(drawableId);

        if (count == 0) {
            View counterValuePanel = view.findViewById(R.id.counter_value_panel);
            counterValuePanel.setVisibility(View.GONE);
        }else {
            TextView textView = (TextView) view.findViewById(R.id.count_text_view);
            textView.setText(""+ count);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
