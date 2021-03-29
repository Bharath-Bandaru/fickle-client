package info.fickle.fickleclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bharath on 05/11/16.
 */

public class HomeFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    public static RecyclerView rv;
    public static List<MainModel> offers;

    public static HomeFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        rv=(RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        initializeData();
        initializeAdapter();
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), rv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();



            }
        }));
        return view;
    }
    private void initializeAdapter(){
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
    }
    private void initializeData(){
        offers = new ArrayList<>();
        offers.add(new MainModel("Amazing Recharge Offers on MyAirtel App\n" +
                "+ Upto 3.5% Cashback From CouponDunia "));
        offers.add(new MainModel("Get amazing offers on International Roaming Smartpicks. Offer is valid only for postpaid sims."));
        offers.add(new MainModel("Get amazing offers and promo codes on various transactions made via Airtel Money. Offer valid for prepaid recharges only."));
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }
    public static void setAdap(){
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
    }

}
