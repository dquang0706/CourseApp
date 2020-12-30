package com.example.coursemanager.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursemanager.R;
import com.example.coursemanager.adapter.NewsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsRecyclerViewAdapter.onCallBack {
    private RecyclerView rcvNews;
    private NewsRecyclerViewAdapter adapter;
    List<String> newsNames = new ArrayList<>();
    public NewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        rcvNews = view.findViewById(R.id.rcvNews);
        newsNames.add("Báo 24H");
        newsNames.add("Báo Giáo Dục Thời Đại");
        newsNames.add("Báo Người Lao Động");
        adapter = new NewsRecyclerViewAdapter(newsNames,this);
        rcvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvNews.setAdapter(adapter);
        rcvNews.hasFixedSize();
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                Bao24hFragment bao24hFragment = new Bao24hFragment();
                fragmentTransaction.replace(R.id.frame, bao24hFragment);
                fragmentTransaction.commit();
//                fragmentManager.popBackStackImmediate();
                //fragmentTransaction.addToBackStack(null);
                break;
            case 1:
                BaoGDThoiDaiFragment baoGDThoiDaiFragment = new BaoGDThoiDaiFragment();
                fragmentTransaction.replace(R.id.frame, baoGDThoiDaiFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                BaoNLDFragment baoNLDFragment = new BaoNLDFragment();
                fragmentTransaction.replace(R.id.frame, baoNLDFragment);
                fragmentTransaction.commit();
                fragmentManager.popBackStackImmediate();
                break;
            default:
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

}