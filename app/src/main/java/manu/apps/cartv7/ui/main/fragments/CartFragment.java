package manu.apps.cartv7.ui.main.fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manu.apps.cartv7.MainActivity;
import manu.apps.cartv7.R;
import manu.apps.cartv7.ui.main.adapters.CartAdapter;
import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.viewmodel.CartViewModel;
import manu.apps.cartv7.ui.main.viewmodel.ViewItemsViewModel;

public class CartFragment extends Fragment implements View.OnClickListener {

    /**
     * Bottom Sheet Behavior
     */
    // Cart Bottom Sheet Behavior
    public static BottomSheetBehavior cartUpdateBottomSheetBehavior;

    // Post Payment Options Bottom Sheet Behavior
    private BottomSheetBehavior paymentOptionsBottomSheetBehavior;

    private BottomSheetBehavior paymentProcessingSheetBehavior;

    /**
     * Lists
     */
    // Cart List
    public static List<Item> cartItemsList;

    /**
     * Variables
     */
    // Total for all the items in the cart
    double total;

    // Products in cart position
    int position;

    /**
     * Views
     */
    TextView tvItemsTotal;
    EditText etPaymentTotal;
    Spinner spnPaymentOptions;
    ProgressBar pbPostPayment;

    TextInputLayout tilPaymentTotal;

    RecyclerView cartRecyclerView;

    Button btnPayment, btnPostPayment;

    /**
     * Adapter
     */
    CartAdapter cartAdapter;

    /**
     * Layout
     */
    RelativeLayout rlCashPayment;

    /**
     * View Model
     */
    private CartViewModel cartViewModel;

    /**
     * Dialogs
     */
    private Dialog cashPaymentDialog;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartItemsList = Config.cartList;

        tvItemsTotal = view.findViewById(R.id.tv_items_total);

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        rlCashPayment = view.findViewById(R.id.rl_cash_payment);
        btnPayment = view.findViewById(R.id.btn_process_payment);
        btnPostPayment = view.findViewById(R.id.btn_post_payment);
        tilPaymentTotal = view.findViewById(R.id.til_payment_total);

        etPaymentTotal = view.findViewById(R.id.et_payment_total);
        spnPaymentOptions = view.findViewById(R.id.spn_payment_options);
        pbPostPayment = view.findViewById(R.id.pb_post_payment);

        View postPaymentView = view.findViewById(R.id.layout_payment_options);
        paymentOptionsBottomSheetBehavior = BottomSheetBehavior.from(postPaymentView);
        paymentOptionsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        View processingPaymentView = view.findViewById(R.id.layout_post_payment);
        paymentProcessingSheetBehavior = BottomSheetBehavior.from(processingPaymentView);
        paymentProcessingSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Initializing total for each item to 0
        total = 0;

        for (Item item : cartItemsList) {

            double total_items = item.getQuantity() * item.getPrice();
            total = total + total_items;

            /*// Setting total to price discount checker
            discountPriceChecker = total + total_item;*/
        }

        tvItemsTotal.setText("Ksh" + " " + Config.numberFormatter(total));

        rlCashPayment.setOnClickListener(this);
        btnPayment.setOnClickListener(this);

        // Set up Edit Text Total
        etPaymentTotal.setText("Your total payment is Ksh" + " " + Config.numberFormatter(total));

        setUpCartRecyclerView();
        setUpPaymentOptionsSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_process_payment:
                processPayment();
            case R.id.rl_cash_payment:
                //cashPayment();
            case R.id.btn_post_payment:
                uploadPaymentDetails();
                break;
        }
    }

    private void setUpPaymentOptionsSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.payment_options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnPaymentOptions.setAdapter(adapter);
    }

    private void cashPayment() {
        // view cart view products dialog
        cashPaymentDialog = new Dialog(getActivity());
        cashPaymentDialog.setContentView(R.layout.layout_payment);
        //viewCartViewProductsDialog.setTitle("Add Category");
        cashPaymentDialog.show();


        // Setting dialog background to transparent
        cashPaymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Setting size of the dialog
        cashPaymentDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showPaymentOptions() {
        paymentOptionsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void processPayment(){
        paymentProcessingSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void uploadPaymentDetails(){

        //pbPostPayment.setVisibility(View.VISIBLE);
        //btnPostPayment.setVisibility(View.GONE);

        if (etPaymentTotal.getText().toString().trim().isEmpty()) {
            tilPaymentTotal.setError("Payment total is empty");
            etPaymentTotal.requestFocus();
        }else {

            final String paymentTotal = String.valueOf(total);

            pbPostPayment.setVisibility(View.VISIBLE);
            btnPostPayment.setVisibility(View.GONE);

            tilPaymentTotal.setErrorEnabled(false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.POST_PAYMENT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(getActivity(), "Payment Successful", Toast.LENGTH_SHORT).show();
                                    pbPostPayment.setVisibility(View.GONE);
                                    btnPostPayment.setVisibility(View.VISIBLE);

                                    paymentProcessingSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Adding category error", Toast.LENGTH_SHORT).show();

                                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),e.toString(),Snackbar.LENGTH_LONG).show();
                                pbPostPayment.setVisibility(View.GONE);
                                btnPostPayment.setVisibility(View.VISIBLE);

                                paymentProcessingSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Adding product error" + error.toString(), Toast.LENGTH_SHORT).show();

                            pbPostPayment.setVisibility(View.GONE);
                            btnPostPayment.setVisibility(View.VISIBLE);

                            paymentProcessingSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("paymentTotal",paymentTotal);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

    }

    private void setUpCartRecyclerView() {

        // Don't set this adapter directly to  recycler view it will produce a null
        cartAdapter = new CartAdapter(getActivity(), new CartAdapter.OnClick() {
            @Override
            public void onEvent(Item item, int pos) {
                position = pos;

                /** Update Cart Here*/

//                byte[] decodedBlob = Base64.decode(item.getPhotoBlob(), Base64.DEFAULT);
//
//                // Setting item image
//                Glide.with(getActivity()).asBitmap().load (decodedBlob).into(imvCartProduct);
//
//                // Setting text to product name text view
//                tvCartProductName.setText(product.getName());
//
//                // Setting text to product price text view
//                tvCartPrice.setText("ksh"+" "+ MainActivity.numberFormatter(product.getPrice()));
//
//                // Setting text to product show stock text view
//                tvCartShowStock.setText(product.getStock() + " " + "Available");
//
//                // Setting quantity to quantity elegant number button
//                cartQuantityNumberButton.setNumber(String.valueOf(product.getQuantity()));
//
//                // Setting text to discount edit text
//                //etCartDiscount.setText(MainActivity.numberFormatter(product.getDiscount()));
//                etCartDiscount.setText(String.valueOf(product.getDiscount()));
//
//
//                // Setting range to quantity elegant number button to be less than the available stock
//                cartQuantityNumberButton.setRange(1,product.getStock());

            }
        });
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(cartAdapter);
    }

}