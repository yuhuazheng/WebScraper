import java.sql.*;

public class JavaDBUtils {
    private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String dbName = "TFDB";
    private static Connection conn;


    public static void CreateDB() throws SQLException, ClassNotFoundException {
        String connectionURL = "jdbc:derby:memory:testDB;create=true";
        Class.forName(driver);

        try{
            conn = DriverManager.getConnection(connectionURL);
        } catch(SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static void BuildInvertedTable() throws SQLException {
        String query = "CREATE TABLE InvertedTable (" +
                "Word VARCHAR(256) NOT NULL, " +
                "HashedUrls VARCHAR(512), " +
                "PRIMARY KEY (word))";
        Statement stmnt = conn.createStatement();
        stmnt.executeUpdate(query);
        System.out.println("InvertedTable created");
        stmnt.close();
    }

    public static String ReadInvertedTable(String inWord) throws SQLException {
        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM InvertedTable WHERE Word = ?");
        stmnt.setString(1, inWord);
        ResultSet rs = stmnt.executeQuery();
        //System.out.println("InvertedTable requested on "+inWord);
        StringBuilder sb = new StringBuilder();
        while(rs.next()){
            sb.append(rs.getString("HashedUrls"));
        }
        stmnt.close();
        return sb.toString();
    }

    public static void InsertInvertedTable(String word, String hashedUrl) throws SQLException {
        PreparedStatement stmnt = conn.prepareStatement("INSERT INTO InvertedTable VALUES(?,?)");
        StringBuilder buff = new StringBuilder(ReadInvertedTable(word));
        if(buff==null||buff.length()==0){
            stmnt.setString(1, word);
            stmnt.setString(2, hashedUrl);
        }
        else{
            buff.append(",");
            buff.append(hashedUrl);
            stmnt = conn.prepareStatement("UPDATE InvertedTable SET HashedUrls =? WHERE Word = ?");
            stmnt.setString(1, buff.toString());
            stmnt.setString(2, word);
        }
        stmnt.executeUpdate();
        //System.out.println("Inverted_Table inserted "+word+" : "+hashedUrl);
        stmnt.close();
    }

    public static void BuildURLTable() throws SQLException {
        String query = "CREATE TABLE URLTable (" +
                "HashedId CHAR(40) NOT NULL, " +
                "URL VARCHAR(256) NOT NULL, " +
                "TS TIMESTAMP, " +
                "WordCounts VARCHAR(32672), " +
                "PRIMARY KEY (HashedId))";
        Statement stmnt = conn.createStatement();
        stmnt.executeUpdate(query);
        System.out.println("URLTable created");
        stmnt.close();
    }

    public static void InsertURLTable(String hashId, String url, String wordCounts) throws SQLException {
        Timestamp  sqlDate = new Timestamp(new java.util.Date().getTime());
        PreparedStatement stmnt = conn.prepareStatement("INSERT INTO URLTable VALUES(?,?,?,?)");
        stmnt.setString(1, hashId);
        stmnt.setString(2, url);
        stmnt.setTimestamp(3, sqlDate);
        stmnt.setString(4, wordCounts);

        stmnt.executeUpdate();
        //System.out.println("URLTable inserted : "+url);
        stmnt.close();
    }

    public static Timestamp GetTSFromURLTable(String hashId) throws SQLException {
        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM URLTable WHERE HashedId = ?");
        stmnt.setString(1, hashId);
        ResultSet rs = stmnt.executeQuery();
        //System.out.println("URLTable requested on "+hashId);
        Timestamp ts = null;
        while(rs.next()){
            ts = rs.getTimestamp("TS");
            break;
        }
        stmnt.close();
        return ts;
    }

    public static String GetUrlFromURLTable(String hashId) throws SQLException {
        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM URLTable WHERE HashedId = ?");
        stmnt.setString(1, hashId);
        ResultSet rs = stmnt.executeQuery();
        //System.out.println("URLTable requested on "+hashId);
        String url = null;
        while(rs.next()){
            url = rs.getString("URL");
            break;
        }
        stmnt.close();
        return url;
    }

    public static String GetWordcountsFromURLTable(String hashId) throws SQLException {
        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM URLTable WHERE HashedId = ?");
        stmnt.setString(1, hashId);
        ResultSet rs = stmnt.executeQuery();
        //System.out.println("URLTable requested on "+hashId);
        String wordCounts = null;
        while(rs.next()){
            wordCounts = rs.getString("WordCounts");
            break;
        }
        stmnt.close();
        return wordCounts;
    }

}
