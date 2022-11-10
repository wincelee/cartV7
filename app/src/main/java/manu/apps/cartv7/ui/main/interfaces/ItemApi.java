package manu.apps.cartv7.ui.main.interfaces;

import java.util.List;

import manu.apps.cartv7.ui.main.classes.Item;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ItemApi {

    String BASE_URL = "http://192.168.100.6:80/kuzasaccov3/";

    @GET("masterfetch?type=items")
    Call<List<Item>> getItems();
}
