package manu.apps.cartv7.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.interfaces.ItemApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewItemsViewModel extends ViewModel {

    private MutableLiveData<List<Item>> itemLiveData;
    private List<Item> itemList;

    public LiveData<List<Item>> getItems(){
//        if (itemLiveData == null){
//            itemLiveData = new MutableLiveData<>();
//            loadItems();
//        }
        itemLiveData = new MutableLiveData<>();
        loadItems();

        return itemLiveData;
    }

    private void loadItems() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.VIEW_ITEMS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemApi productApi = retrofit.create(ItemApi.class);
        Call<List<Item>> call = productApi.getItems();

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                itemLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

}