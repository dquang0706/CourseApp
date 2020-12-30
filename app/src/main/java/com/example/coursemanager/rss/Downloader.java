package com.example.coursemanager.rss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Downloader extends AsyncTask<Void, Void, Object> {
    Context context;
    String urlAddress;
    ListView listView;

    public Downloader(Context context, String urlAddress, ListView listView) {
        this.context = context;
        this.urlAddress = urlAddress;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... voids) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o.toString().startsWith("Error")) {
            Toast.makeText(context, o.toString(), Toast.LENGTH_SHORT).show();
        } else {
            new RSSParser(context, (InputStream) o, listView).execute();
        }
    }

    private Object downloadData() {
        Object connection = Connector.connect(urlAddress);
        if (connection.toString().startsWith("Error")) {
            return connection.toString();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            int responseCode = con.getResponseCode();
            if (responseCode == con.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(con.getInputStream());
                return inputStream;
            }
            return ErrorTracker.RESPONSE_ERROR + con.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorTracker.IO_ERROR;
        }
    }
}
