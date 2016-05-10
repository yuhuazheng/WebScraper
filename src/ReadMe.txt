This file contains following parts:
1. Overview
2. DataBase schema
3. Major parts descriptions, including trade-offs and next steps
4. How to run the program


1. Overview
    This assignment is to build a web page scraper to extract words and their counts. One possible usage is to return a list urls by a given words.
Therefore 2 tables can be created. One is to record raw information extracted from the page. The other is inverted table as of <word, List-of-url>.
The major parts include: database, webpage-scraping, text processing (word extraction and counting), hash utility (turn raw URL into hashed id).

2. Schema

2.1 Url table schema
<hashedId : original url, timestamp, list-of-words-and-counts>
hashedId is the unique key for each url and can be used crossing tables.
timestamp records the scraping time and can be used for updating or expiring a row.

2.2 inverted table schema
<word : list-of-hashedId>

3. Major Classes

3.1 HashUtils
The original url is hashed via SHA-1.

3.2 TextUtils
It extracts unique words and their counts from a given string.
It excludes words from "stop-words-list" and words with counts less than certain threshold.
further improvements include:
    import more proper stop words.
    import 3rd party tool to extract words more accurately. (Currently just simple regex.)
    returned map can be ordered by counts.

3.3 JavaDBUtils
Choose JavaDB In-Memory for demonstration. There are other options like H2DB etc.

3.4 SingleScraper
Use Jsoup to extract text from web page.
Further improvements:
    More accurate Jsoup path to extract more specific areas of texts.
    Use timestamp to control whether to scrape an url.
    Not only update in-memory database, also write to disk.
    Add more controls on word extraction. For example, how many words can be extracted from a single page.

3.5 testScraper
Wrap around above classes and show the progress of scraping and query the database.
Further improvements:
    Handle invalid inputs.
    Add concurrency test.

4. How to run program
Exteneral libraries are jsoup and derby.
A jar has been made to run the program from command line.
Double quoted line are typed by users.

yuhua@yuhua-Latitude-E6420:~/temp$ "java -jar WebScraper.jar"
InvertedTable created
URLTable created
type in a valid url, like http://www.yahoo.com.
type in 'finish' to end scraper
"http://www.yahoo.com"
page has unique words of 168

type in a valid url, like http://www.yahoo.com.
type in 'finish' to end scraper
"http://sports.yahoo.com"
page has unique words of 353

type in a valid url, like http://www.yahoo.com.
type in 'finish' to end scraper
"finish"

type in word to requst database
type in 'finish' to quit program
"will"
11897a5a38e50c6b72c7588c8ed6a153d866d5c0,f39ffe57c7fd8b34c3772dc895af59669580b45e
http://www.yahoo.com
http://sports.yahoo.com

type in word to requst database
type in 'finish' to quit program
"finish"

type in url to requst database
type in 'exit' to quit program
"http://www.yahoo.com"
hashId : 11897a5a38e50c6b72c7588c8ed6a153d866d5c0
timestamp : 2016-05-08 20:28:41.443
wordcount :
your:3,begins:2,Bill:2,after:6,headline:2,photo:2,with:6,career:2,told:2,years:2,Howard:2,went:4,women:3,city:2,have:2,Style:3,Lattimore:6,border:2,new:3,Trump:4,Skip:3,Sunday’s:2,running:3,and:17,Reblog:10,declared:3,High:4,probe:2,Mail:3,Sign:2,foods:3,Entertainment:2,coaching:5,RB:3,U.S.:4,found:3,are:3,Obama:2,Hillary:2,Learn:6,TV:3,Once:4,Photo:2,ate:2,Search:2,This:2,has:4,those:4,allegedly:2,These:3,Seen:2,Beauty:2,shot:2,Photos:3,not:3,Shopping:2,Movies:3,was:9,way:2,delivers:2,when:3,officials:2,Low:4,more:3,Day:3,habits:2,him:2,his:8,few:2,day:2,Who:3,job:3,who:4,their:2,Dislike:2,Derby:2,said.:2,also:4,Times:2,Politics:3,"Hillary:2,You:2,you:2,Bryant:5,an:3,former:3,as:9,at:8,ABC:4,be:5,dead:3,by:4,abused:2,Sign-in:10,Weight:2,Marcus:2,University:2,some:2,for:8,back:5,53°f:2,content:2,School:3,load:2,over:2,football:2,2016:2,2015:2,he:9,big:2,Celebrity:3,revealed:2,fired:2,in:21,Mother's:3,is:4,it:5,Yahoo:8,May:2,South:3,that:9,than:2,Carolina:2,Clinton:4,student:2,of:23,on:26,10.:2,Upon:5,Tumblr:12,episode:2,him,:2,1.:2,glycemic:2,2.:2,3.:2,But:2,one:2,4.:2,to:39,story:2,Tech:3,5.:2,Another:2,this:2,6.:2,Time:2,Greg:4,we:2,News:8,7.:2,8.:2,commencement:2,9.:2,work:4,UAB:4,our:2,More:10,brain:3,first:3,from:4,Sports:3,like:10,Donald:2,can:2,said:2,Sponsored:2,will:5

type in url to requst database
type in 'exit' to quit program
"exit"
GoodBye



