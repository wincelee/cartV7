package manu.apps.cartv7.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import manu.apps.cartv7.MainActivity;
import manu.apps.cartv7.R;
import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.InputFilterMinMax;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.classes.ThousandTextWatcher;
import manu.apps.cartv7.ui.main.interfaces.AddRemoveCallbacks;

public class ItemBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private String code,name, description, photoBlob, getDiscountInput;
    private double discount, price;
    private int available,total_quantity,quantity;

    private TextView tvItemsAvailable, tvItemName, tvItemPrice;
    private EditText etQuantity, etDiscount;

    private Button btnAddItemToCart, btnDecrement, btnIncrement;

    private ImageView imvItemPhotoBlob;

    // Parsing Items
    private Item item = new Item();

    String quantityString;
    int maxValue, minValue;

    public ItemBottomSheet() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        code = getArguments().getString("code");
        name = getArguments().getString("name");
        description = getArguments().getString("description");
        photoBlob = getArguments().getString("photoBlob");
        discount = getArguments().getDouble("discount");
        price = getArguments().getDouble("price");
        available = getArguments().getInt("available");

        maxValue = available;
        minValue = 1;

        tvItemName = view.findViewById(R.id.tv_item_name);
        tvItemPrice = view.findViewById(R.id.tv_item_price);
        tvItemsAvailable = view.findViewById(R.id.tv_items_available);
        etQuantity = view.findViewById(R.id.et_item_quantity);
        etDiscount = view.findViewById(R.id.item_discount_edit_text);
        btnAddItemToCart = view.findViewById(R.id.btn_add_to_cart);
        btnDecrement = view.findViewById(R.id.btn_decrement);
        btnIncrement = view.findViewById(R.id.btn_increment);
        imvItemPhotoBlob = view.findViewById(R.id.item_photo_blob);

        // Setting on click listeners to buttons
        btnAddItemToCart.setOnClickListener(this);
        btnDecrement.setOnClickListener(this);
        btnIncrement.setOnClickListener(this);


        // Setting text to Text views
        tvItemName.setText(name);
        tvItemPrice.setText("Ksh" + " " + Config.numberFormatter(price));
        tvItemsAvailable.setText(available + " " + "Available");

        // Setting Text Watcher to bottom sheet fragment discount edit text
        etDiscount.addTextChangedListener(new ThousandTextWatcher(etDiscount));


        // Checking if available is negative and setting to available tv
        if (available < 0 | available == 0){
            tvItemsAvailable.setText(1 + " " + "Available");
        }else {
            tvItemsAvailable.setText(available + " " + "Available");
        }

        // Decoding and setting image
        byte[] decodedBlob = Base64.decode(photoBlob, Base64.DEFAULT);
        Glide.with(this)
                .asBitmap()
                .load (decodedBlob)
                .into(imvItemPhotoBlob);

        etQuantity.setFilters(new InputFilter[]{ new InputFilterMinMax(String.valueOf(minValue), String.valueOf(maxValue), getActivity())});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_to_cart:

                // Getting Values
                quantity = Integer.parseInt(etQuantity.getText().toString());

                //Toast.makeText(getActivity(), "Your quantity is: " + quantity, Toast.LENGTH_SHORT).show();

                getDiscountInput = ThousandTextWatcher.trimCommaOfString(etDiscount.getText().toString().trim());

                if (TextUtils.isEmpty(getDiscountInput)){

                    discount = 0;

                }else {

                    discount = Double.parseDouble(getDiscountInput);

                }

                if (Config.cartList.contains(code)) {
                    Toast.makeText(getActivity(), "Product already added to cart", Toast.LENGTH_SHORT).show();
                }else {

                    available = available - quantity;

                    item.setCode(code);
                    item.setName(name);
                    item.setDescription(description);
                    item.setPhotoBlob(photoBlob);
                    item.setDiscount(discount);
                    item.setPrice(price);
                    item.setQuantity(quantity);
                    item.setAvailable(available);

                    Config.cartList.add(item);

                    if (getActivity() instanceof MainActivity) {
                        ((AddRemoveCallbacks) getActivity()).onAddProduct();
                    }

                    // Getting total quantity for counter
                    total_quantity = 0;

                    for (Item item : Config.cartList) {

                        int quantity = item.getQuantity();
                        total_quantity = total_quantity + quantity;
                    }

                    MainActivity.cart_count = total_quantity;
                }


                // Dismissing the bottom sheet
                dismiss();

                // Adding to Cart Snack bar
                Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),"Successfully added to cart",Snackbar.LENGTH_SHORT)
                        .setActionTextColor(getResources().getColor(R.color.White100));
                View snackView = snackbar.getView();
                snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.White100));
                snackbar.show();

                break;
            case R.id.btn_decrement:

                btnIncrement.setEnabled(true);

                quantityString = etQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(quantityString)){
                    etQuantity.setError("Quantity is required");
                }else {
                    if (Integer.parseInt(etQuantity.getText().toString()) - 1 > minValue |
                    Integer.parseInt(etQuantity.getText().toString()) - 1 == minValue){
                        decreaseQuantity();
                    } else {
                        btnDecrement.setEnabled(false);
                        btnIncrement.setEnabled(true);
                        Toast.makeText(getActivity(), "Quantity cant be less than 1", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.btn_increment:

                btnDecrement.setEnabled(true);

                quantityString = etQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(quantityString)){
                    etQuantity.setError("Quantity is required");
                }else {
                    if (Integer.parseInt(etQuantity.getText().toString()) + 1 < maxValue |
                    Integer.parseInt(etQuantity.getText().toString()) + 1 == maxValue) {
                        increaseQuantity();
                    } else {
                        btnIncrement.setEnabled(false);
                        btnDecrement.setEnabled(true);
                        Toast.makeText(getActivity(), "Quantity cant be more than available", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

    }

    public void increaseQuantity() {

        display(Integer.parseInt(etQuantity.getText().toString()) + 1);

    }

    public void decreaseQuantity() {

        display(Integer.parseInt(etQuantity.getText().toString()) - 1);
    }

    private void display(int number) {
        etQuantity.setText(String.valueOf(number));
    }
}