
import java.util.HashMap;
import java.util.HashSet;

public class TextUtils {

    private static HashSet<String> stopWords;
    private static int freqThreshold=2;

    public static void InitialTextUtils(){
        stopWords = new HashSet<String>();

        String[] stops = {"the"};
        for(String s: stops){
            if(!stopWords.contains(s))
                stopWords.add(s);
        }
    }

    public static HashMap<String,Integer> BuildTFMap(String rawText){
        HashMap<String,Integer> map = new HashMap<String,Integer>();

        String[] words = rawText.split("\\s+");
        for(String w: words){
            if(w.length()<2)
                continue; //filter out empty and single letter
            if(stopWords.contains(w.toLowerCase()))
                continue;
            w=w.trim();
            Integer count = map.get(w);
            map.put(w,(count==null? 1: map.get(w)+1));
        }

        map.values().removeIf(c -> c < freqThreshold);
        return map;

    }
}
