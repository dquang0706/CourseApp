package com.example.coursemanager.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.coursemanager.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {
    public static WebView webView;
    String link;

    public WebViewFragment() {
        // Required empty public constructor

    }

    public WebViewFragment(String link) {
        this.link = link;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = (WebView) view.findViewById(R.id.wv);
        Log.i("link", "onCreateView: " + link);
        try {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.loadUrl(link);
            webView.setWebViewClient(new WebViewClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}