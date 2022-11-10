package manu.apps.cartv7.ui.main.adapters;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import manu.apps.cartv7.MainActivity;
import manu.apps.cartv7.R;
import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.fragments.CartFragment;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{

    private Context context;
    private List<Item> itemList;
    private RequestOptions requestOptions;
    OnClick onClick;


    public interface OnClick {
        void onEvent(Item item, int pos);
    }

    public CartAdapter(Context context, OnClick onClick) {
        this.context = context;
        this.itemList = Config.cartList;

        // OnClick Interface
        this.onClick = onClick;

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_load);

    }

    public void removeItem(int position) {

        itemList.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    public void restoreItem(Item item, int position) {

        itemList.add(position, item);

        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder (@NonNull final MyViewHolder myViewHolder, final int position) {

        final Item itemPosition = itemList.get(position);

        byte[] decodedBlob = Base64.decode(itemPosition.getPhotoBlob(), Base64.DEFAULT);

        Glide.with(context)
                .load(decodedBlob)
                .apply(requestOptions)
                .fitCenter()
                .into(myViewHolder.itemImageView);

        myViewHolder.tvItemName.setText(itemPosition.getName());
        myViewHolder.tvItemPrice.setText("ksh" + " " + Config.numberFormatter(itemPosition.getPrice()));
        myViewHolder.tvItemQuantity.setText(String.valueOf(itemPosition.getQuantity()));

        myViewHolder.tvItemDiscount.setText("Ksh"+" "+ Config.numberFormatter(itemPosition.getDiscount()));

        //Calculate Total
        myViewHolder.tvItemTotal.setText("Ksh"+" "+ Config.numberFormatter(itemPosition.getPrice()*
                itemPosition.getQuantity()-itemPosition.getDiscount()));

        // Setting on click options for on click on cart card view
        myViewHolder.cartCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onEvent(itemPosition, position);
                //CartFragment.cartUpdateBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        myViewHolder.btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Item deleteItem = itemList.get(position);

                MainActivity.cart_count = deleteItem.getQuantity();

                removeItem(position);

                Snackbar snackbar = Snackbar.make(v,"Successfully removed from cart",Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                restoreItem(deleteItem, position);
                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(context,R.color.colorAccent));
                View snackView = snackbar.getView();
                snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(context,R.color.White100));
                snackbar.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemName,tvItemPrice,tvItemQuantity,tvItemDiscount,tvItemTotal;
        ImageView itemImageView;
        Button btnRemoveFromCart;
        CardView cartCardView;

        public MyViewHolder(View itemView){
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvItemQuantity = itemView.findViewById(R.id.tv_cart_item_quantity);
            tvItemDiscount = itemView.findViewById(R.id.tv_cart_item_discount);
            tvItemTotal = itemView.findViewById(R.id.tv_cart_item_total);
            itemImageView = itemView.findViewById(R.id.imv_cart_item);
            cartCardView = itemView.findViewById(R.id.cart_card_view);

            btnRemoveFromCart = itemView.findViewById(R.id.btn_remove_from_cart);

        }
    }
}
