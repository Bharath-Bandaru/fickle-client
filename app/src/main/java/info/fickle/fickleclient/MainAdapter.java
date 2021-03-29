package info.fickle.fickleclient;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bharath on 04/11/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.OfferViewHolder> {
        List<MainModel> mainModelList;

public static class OfferViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView offer,live;
    ImageView stat;
    LinearLayout ll;
    OfferViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view);
        offer = (TextView) itemView.findViewById(R.id.offer_text);

    }
}
    MainAdapter(List<MainModel> mainModelList){
        this.mainModelList = mainModelList;
    }
    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
        OfferViewHolder pvh = new OfferViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(OfferViewHolder personViewHolder, int i) {
        personViewHolder.offer.setText(mainModelList.get(i).offerf);

    }
    @Override
    public int getItemCount() {
        return mainModelList.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
