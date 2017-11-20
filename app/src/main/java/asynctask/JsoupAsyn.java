package asynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import model.ItemGridView;

/**
 * Created by Admin on 11/15/2017.
 */

public class JsoupAsyn extends AsyncTask<String, Void, ArrayList<ItemGridView>> {

    public static final int WHAT_DATA = 1;
    private Handler handler;

    public JsoupAsyn(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    protected ArrayList<ItemGridView> doInBackground(String... strings) {
        String link = strings[0];
        ArrayList<ItemGridView> arrItem = new ArrayList<>();
        try {
            Document document = Jsoup.connect(link).get();

            boolean check = document.select("div").hasClass("visible-xs");
            if (check == false)
            {
                Elements elements = document.select("div.thumb-container-big");
                for (Element e: elements) {
                    Elements element = e.select("a");
                    String urlChild = element.get(0).attr("href");
                    String smallImg = element.get(0).select("img").attr("src");

                    Document document1 = Jsoup.connect("https://wall.alphacoders.com/"+urlChild).get();
                    Elements elements1 = document1.select("img.img-responsive");
                    String imageUrl = elements1.get(0).attr("src");

                    ItemGridView itemGridView = new ItemGridView(imageUrl,smallImg);
                    arrItem.add(itemGridView);

                }
            }
            else
            {
                Elements div = document.select("div[class=hidden-xs hidden-sm]");
                Elements li = div.get(0).select("ul.pagination").select("li");
                int q = li.size();
                String countPage = li.get(li.size() - 2).select("a").first().text();
                String href = li.get(li.size() - 2).select("a").first().attr("href");
                String[] str1 = href.split(countPage.trim());
                String hrefPage = str1[0];
                int c = Integer.parseInt(countPage);

                for(int i=1; i<=c; i++) {
                    Document document2 = Jsoup.connect("https://wall.alphacoders.com/" + hrefPage + i).get();
                    Elements elements = document2.select("div.thumb-container-big");
                    for (Element e : elements) {
                        Elements element = e.select("a");
                        String urlChild = element.get(0).attr("href");
                        String smallImg = element.get(0).select("img").attr("src");

                        Document document1 = Jsoup.connect("https://wall.alphacoders.com/" + urlChild).get();
                        Elements elements1 = document1.select("img.img-responsive");
                        String imageUrl = elements1.get(0).attr("src");

                        ItemGridView itemGridView = new ItemGridView(imageUrl,smallImg);
                        arrItem.add(itemGridView);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    @Override
    protected void onPostExecute(ArrayList<ItemGridView> itemGridViews) {
        super.onPostExecute(itemGridViews);
        Message message = new Message();
        message.what = WHAT_DATA;
        message.obj = itemGridViews;
        handler.sendMessage(message);
    }
}
