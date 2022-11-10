package manu.apps.cartv7.ui.main.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import manu.apps.cartv7.R;
import manu.apps.cartv7.ui.main.classes.Config;
import manu.apps.cartv7.ui.main.classes.Item;
import manu.apps.cartv7.ui.main.fragments.ItemBottomSheet;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{

    private Context context;
    private List<Item> itemList;
    private List<Item> itemListFull;
    private FragmentManager fragmentManager;
    private RequestOptions requestOptions;
    private Item item;

    private boolean isBottomSheetShowing = false;

    public ItemAdapter(Context context, List<Item> itemList, FragmentManager fragmentManager){
        this.context = context;
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;

        // Item Search

        itemListFull = new ArrayList<>(itemList);

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_load);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Item itemPosition = itemList.get(position) ;

        // Here is for setting items to be displayed in the Main layout using the product adapter
        // For setting items to the bottom sheet use the Bottom Sheet Fragment

        holder.itemName.setText(itemPosition.getName());

        //Setting using String Formatter and method 2
        /*myViewHolder.price.setText ("Ksh"+" "+ MainActivity.numberTextFormatter(String.format("%.0f",
                productList.get(position).getPrice())));*/

        holder.itemPrice.setText ("Ksh"+" "+ Config.numberFormatter(itemPosition.getPrice()));

        byte[] decodedBlob = Base64.decode(itemPosition.getPhotoBlob(), Base64.DEFAULT);

        Glide.with(context)
                .load(decodedBlob)
                .apply(requestOptions)
                .fitCenter()
                .into(holder.photoBlob);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ItemBottomSheet itemBottomSheet = new ItemBottomSheet();
                Bundle bundle = new Bundle();

                bundle.putString("code",itemPosition.getCode());
                bundle.putString("name",itemPosition.getName());
                bundle.putString("description",itemPosition.getDescription());
                bundle.putString("upcCode",itemPosition.getUpcCode());
                bundle.putString("photoBlob",itemPosition.getPhotoBlob());
                bundle.putDouble("discount",itemPosition.getDiscount());
                bundle.putDouble("price",itemPosition.getPrice());
                bundle.putInt("available",itemPosition.getAvailable());

                itemBottomSheet.setArguments(bundle);
                itemBottomSheet.show(fragmentManager, itemBottomSheet.getTag());

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice;
        ImageView photoBlob;
        CardView cardView;


        public MyViewHolder(View itemView){
            super(itemView);

            itemName = itemView.findViewById(R.id.tv_item_name);
            itemPrice = itemView.findViewById(R.id.tv_item_price);
            photoBlob = itemView.findViewById(R.id.imv_item);
            cardView = itemView.findViewById(R.id.item_card_view);

        }
    }

    public Filter getItemFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(itemListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Item item: itemListFull) {

                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }

                    // Search others below
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
