package asynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import model.Figure;

/**
 * Created by Admin on 11/17/2017.
 */

public class JsoupAsynFigures extends AsyncTask<String, Void, ArrayList<Figure>> {

    public static final int WHAT_DATA_ONE = 1;
    private Handler handler;

    public JsoupAsynFigures(Handler handler)
    {
        this.handler = handler;
    }


    @Override
    protected ArrayList<Figure> doInBackground(String... strings) {
        ArrayList<Figure> arrFi = new ArrayList<>();
        String link = strings[0];

        try {
            Document document = Jsoup.connect(link).get();
            // Elements elements0 = document.select("div").attr("style")
            Elements elements = document.select("div.panel-body");
            Elements elements1 = elements.get(0).select("div.center");

            Elements elements2 = elements1.get(0).select("div");
            Elements elements3 = elements2.get(0).select("h4");


            for (int i=1; i<elements3.size();i++)
            {
                Elements ele = elements3.get(i).select("a");
                String name = ele.get(0).text();
                String url = ele.get(0).attr("href");
                Figure figure = new Figure(name,url);
                arrFi.add(figure);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return arrFi;
    }



    @Override
    protected void onPostExecute(ArrayList<Figure> itemLv) {
        super.onPostExecute(itemLv);
        Message message = new Message();
        message.what = WHAT_DATA_ONE;
        message.obj = itemLv;
        handler.sendMessage(message);
    }
}