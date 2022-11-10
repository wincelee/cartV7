package manu.apps.cartv7.ui.main.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manu.apps.cartv7.MainActivity;
import manu.apps.cartv7.R;
import manu.apps.cartv7.ui.main.adapters.ItemAdapter;
import manu.apps.cartv7.ui.main.classes.CartCounterConverter;
import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.interfaces.AddRemoveCallbacks;
import manu.apps.cartv7.ui.main.viewmodel.ViewItemsViewModel;


public class ViewItemsFragment extends Fragment /*implements AddRemoveCallbacks */{


    private ViewItemsViewModel viewItemsViewModel;
    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private static List<Item> itemList;

    public static ViewItemsFragment newInstance() {
        return new ViewItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.view_items_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewItemsViewModel = new ViewModelProvider(this).get(ViewItemsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        itemsRecyclerView = view.findViewById(R.id.items_recycler_view);

        itemList = new ArrayList<>();

        itemsJsonRequest();

    }

    private void itemsJsonRequest() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        Log.wtf("VIEW_ITEMS_URL", Config.VIEW_ITEMS_URL);

        JsonArrayRequest itemJsonArrayRequest = new JsonArrayRequest(Config.VIEW_ITEMS_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);

                        Item item = new Item();

                        item.setCode(jsonObject.getString("code"));
                        item.setName(jsonObject.getString("name"));
                        item.setDescription(jsonObject.getString("description"));
                        item.setPrice(jsonObject.getDouble("price"));
                        item.setDiscount(jsonObject.getDouble("discount"));
                        item.setAvailable(jsonObject.getInt("available"));
                        item.setPhotoBlob(jsonObject.getString("photoBlob"));

                        itemList.add(item);

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //progressDialog.setMessage(e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                setUpItemRecyclerView(itemList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display volley in snack bar
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Volley Error" +
                        error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        RequestQueue itemRequestQueue = Volley.newRequestQueue(getActivity());
        itemRequestQueue.add(itemJsonArrayRequest);
    }

    private void setUpItemRecyclerView(List<Item> itemList) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Don't assign the recycler view adapter any other way it will cause a null
        itemAdapter = new ItemAdapter(getActivity(),itemList,fragmentManager);

        itemsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        itemsRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.view_items_fragment_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item_action);
//        MenuItem cartItem = menu.findItem(R.id.cart_action);
//
//        cartItem.setIcon(CartCounterConverter.convertLayoutToImage(getActivity(),MainActivity.cart_count,R.drawable.ic_view_cart));

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getItemFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.refresh_items_action:
                itemList.clear();
                itemsJsonRequest();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onAddProduct() {
//        cart_count++;
//        getActivity().invalidateOptionsMenu();
//    }
//
//    @Override
//    public void onRemoveProduct() {
//        cart_count--;
//        getActivity().invalidateOptionsMenu();
//    }
}