package com.example.coursemanager.rss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RSSParser extends AsyncTask<Void, Void, Boolean> {
    Context context;
    InputStream inputStream;
    ListView listView;

    ProgressDialog progressDialog;
    List<RssItem> rssItems = new ArrayList<>();

    public RSSParser(Context context, InputStream inputStream, ListView listView) {
        this.context = context;
        this.inputStream = inputStream;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            listView.setAdapter(new CustomAdapter(context, rssItems));
        } else {
            Toast.makeText(context, "Unable to Parse", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseRSS() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);
            int event = parser.getEventType();

            String tagValue = null;
            Boolean isSiteMeta = true;

            rssItems.clear();
            RssItem rssItem = new RssItem();

            do {

                String tagName = parser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            rssItem = new RssItem();
                            isSiteMeta = false;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        tagValue = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (!isSiteMeta) {
                            if (tagName.equalsIgnoreCase("title")) {
                                rssItem.setTitle(tagValue);

                            } else if (tagName.equalsIgnoreCase("description")) {
                                String desc = tagValue;
                                rssItem.setDescription(desc);
                                Log.i("abc", "parseRSS: " + desc);
                                //EXTRACT IMAGE FROM DESCRIPTION
                                String imageUrl = desc.substring(desc.indexOf("src=") + 5);
                                int indexOf = imageUrl.indexOf("'");
                                if (indexOf==-1){
                                    indexOf = imageUrl.indexOf("\"");
                                }
                                imageUrl = imageUrl.substring(0,indexOf);
                                Log.i("hinh", "parseRSS: "+imageUrl);
                                rssItem.setImageUrl(imageUrl);

                            } else if (tagName.equalsIgnoreCase("pubDate")) {
                                rssItem.setDate(tagValue);
                            } else if (tagName.equalsIgnoreCase("link")){
                                rssItem.setLink(tagValue);
                            }
                        }

                        if (tagName.equalsIgnoreCase("item")) {
                            rssItems.add(rssItem);
                            isSiteMeta = true;
                        }

                        break;
                }

                event = parser.next();

            } while (event != XmlPullParser.END_DOCUMENT);

            return true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
