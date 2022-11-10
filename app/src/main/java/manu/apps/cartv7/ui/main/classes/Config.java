package manu.apps.cartv7.ui.main.classes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final String ITEMS_URL = "http://192.168.42.41:80/kuzasaccov3/";

    public static final String VIEW_ITEMS_URL = ITEMS_URL+"masterfetch?type=items";

    /** Cart Lists */
    public static List<Item> cartList = new ArrayList<>();

    public static String numberFormatter(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(d);
    }

    private static final String JSON_URL = "https://www.kuzasystems.com/cartapp/apis/";

    public static final String POST_PAYMENT = JSON_URL+"post_payment.php";
}
