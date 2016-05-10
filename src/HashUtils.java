import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HashUtils {
    private static MessageDigest md = null;
    private static boolean IfMDSet = false;

    public static boolean InitialMD(){
        try{
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        IfMDSet = true;
        return true;
    }

    private static String byteToHex(byte[] encrypt)
    {
        Formatter formatter = new Formatter();
        for (byte b : encrypt)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String GetUrlHashValue(String inUrl){
        if(inUrl==null || inUrl.isEmpty()){
            return null;
        }

        if(!IfMDSet)
            InitialMD();

        byte[] encrypt = null;
        try{
            md.reset();
            encrypt = md.digest(inUrl.getBytes("UTF-8"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return byteToHex(encrypt);

    }

}
