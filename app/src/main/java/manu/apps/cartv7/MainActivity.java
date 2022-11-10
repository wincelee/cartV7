package manu.apps.cartv7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import manu.apps.cartv7.ui.main.classes.CartCounterConverter;
import manu.apps.cartv7.ui.main.classes.DisableSSL;
import manu.apps.cartv7.ui.main.interfaces.AddRemoveCallbacks;

public class MainActivity extends AppCompatActivity implements AddRemoveCallbacks {


    /** Variables*/
    // Counter for Cart
    public static int cart_count = 0;

    private NavController navController;
    private Toolbar toolbar;

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        //toolbar = findViewById(R.id.main_toolbar);

        // Setting up nav controller with toolbar
        //NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        //To add navigation support to the default action bar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        DisableSSL.disableSSLCertificate(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem cartItem = menu.findItem(R.id.cart_action);

        cartItem.setIcon(CartCounterConverter.convertLayoutToImage(this,cart_count,R.drawable.ic_view_cart));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart_action) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_view_products_to_cart);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();
    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (getCurrentFocus() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

        }

        return super.dispatchTouchEvent(ev);
    }
}