package com.example.coursemanager.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.coursemanager.R;
import com.example.coursemanager.rss.CustomAdapter;
import com.example.coursemanager.rss.Downloader;
import com.example.coursemanager.rss.RssItem;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BaoGDThoiDaiFragment extends Fragment {

    final static String urlAddress = "https://giaoducthoidai.vn/rss/giao-duc.rss";
    ListView lv_news;

    public BaoGDThoiDaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bao_thanh_nien, container, false);
        lv_news = view.findViewById(R.id.lv_news_thanhnien);
        new Downloader(getActivity(),urlAddress,lv_news).execute();
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final  RssItem rssItem = CustomAdapter.rssItemList.get(i);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(), R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_click_news,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                // Xem tin tức
                bottomSheetView.findViewById(R.id.txt_XemTinTuc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Log.i("RssList", "rssItemList: " + CustomAdapter.rssItemList.size());
                        Log.i("RssItem", "rssItem: " + rssItem);
                        String link = rssItem.getLink();
                        Log.i("RssLink", "rssLink: " + link);
                        WebViewFragment webViewFragment = new WebViewFragment(link);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, webViewFragment);
                        fragmentTransaction.commit();
                    }
                });

                // Share tin tức
                ShareButton shareButton = bottomSheetView.findViewById(R.id.btnShare);
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(rssItem.getLink()))
                        .build();
                shareButton.setShareContent(content);
                bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        return view;
    }
}