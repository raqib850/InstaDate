package com.example.raqib.instadate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TabScience extends Fragment {

    RecyclerView myRecyclerView;
    public List<NewsItems> ScienceDaily;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_recycler_view,container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        myRecyclerView.hasFixedSize();

        ScienceDaily = SitesXmlPullParserTheFinancialExpressTech.getStackSitesFromFile(getActivity().getBaseContext());
        //FOR DIFFERENT CHANNELS
//        List<NewsItems> newsItemsList = new ArrayList<NewsItems>(){
//            {
//
//                addAll(ScienceDaily);
//            }
//        };

        myRecyclerView.setAdapter(new MyNewsRecyclerViewAdapter(ScienceDaily));
        return view;
    }

}