
import java.sql.SQLException;
import java.util.Scanner;

public class testScraper {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        JavaDBUtils.CreateDB();
        JavaDBUtils.BuildInvertedTable();
        JavaDBUtils.BuildURLTable();

        TextUtils.InitialTextUtils();

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("type in a valid url, like http://www.yahoo.com.");
            System.out.println("type in 'finish' to end scraper");
            String u = scanner.next();
            if(u.equals("finish"))
                break;
            SingleScraper inst = new SingleScraper(u);
            inst.start();
        }

        while(true){
            System.out.println("type in word to requst database");
            System.out.println("type in 'finish' to quit program");
            String w = scanner.next();
            if(w.equals("finish"))
                break;
            String hashedIds = JavaDBUtils.ReadInvertedTable(w);
            System.out.println(hashedIds);
            for(String id: hashedIds.split(",")){
                String url = JavaDBUtils.GetUrlFromURLTable(id);
                System.out.println(url);
            }
        }

        while(true){
            System.out.println("type in url to requst database");
            System.out.println("type in 'exit' to quit program");
            String u = scanner.next();
            if(u.equals("exit"))
                break;
            String hashedId = HashUtils.GetUrlHashValue(u);
            System.out.println("hashId : "+hashedId);
            System.out.println("timestamp : "+JavaDBUtils.GetTSFromURLTable(hashedId));
            System.out.println("wordcount : ");
            System.out.println(JavaDBUtils.GetWordcountsFromURLTable(hashedId));
        }
        System.out.println("GoodBye");
    }
}
