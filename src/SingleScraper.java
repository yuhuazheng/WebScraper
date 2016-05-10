
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SingleScraper {
    private HashMap<String,Integer> bodyWordMap;
    private String url;
    private String hashedUrlId;

    public SingleScraper(String url){

        this.url=url;
        hashedUrlId = HashUtils.GetUrlHashValue(this.url);
        bodyWordMap = new HashMap<String,Integer>();
    }

    public void start() throws SQLException {
        //if not already scraped within certain period
        Timestamp lastTS = JavaDBUtils.GetTSFromURLTable(hashedUrlId);
        Timestamp curTS = new Timestamp(new java.util.Date().getTime());
        if(lastTS!=null && curTS.before(lastTS)){
            System.out.println("the url has been scraped at later time, quit");
            return;
        }
        scrape();

        //write to in-memory database
        writeDB();
    }

    private void scrape(){
        String title = null;
        String text = null;
        try{
            //fetch the url
            Document doc = Jsoup.connect(url).get();
            //parse the page
            text = doc.text();
        } catch (IOException e){
            e.printStackTrace();
        }

        //build hashmaps
        if(text!=null && !text.isEmpty()) {
            bodyWordMap = TextUtils.BuildTFMap(text);
            System.out.println("page has unique words of " + bodyWordMap.size());
        }
        return;
    }

    public void writeDB() throws SQLException {
        StringBuilder buff = new StringBuilder();
        for(String w : bodyWordMap.keySet()){
            //write to iverted table
            JavaDBUtils.InsertInvertedTable(w,hashedUrlId);

            //update buff
            buff.append(w);
            buff.append(":");
            buff.append(bodyWordMap.get(w));
            buff.append(",");

        }
        buff.deleteCharAt(buff.length()-1);
        //write to url table
        JavaDBUtils.InsertURLTable(hashedUrlId,url,buff.toString());
    }

}
