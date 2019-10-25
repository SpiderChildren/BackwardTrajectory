import com.csvreader.CsvWriter;
import org.apache.http.client.ClientProtocolException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    //https://ready.arl.noaa.gov/hypubout/157786_trj001.gif
    //create table BT ( url varchar(500) not null , name  varchar(200)  not null unique , place varchar(50), time DATETIME);
    // alter table BT CONVERT TO CHARACTER SET utf8;
    public static ArrayList< ArrayList<String> > RetryList = new ArrayList<ArrayList<String>>();
    public static int retryTime  = 3;
    public static  String errorFile = "ErrorCity.csv";
    public static  String storeUrl = "BT/";
    public static ConnentToMySQL connenter = new ConnentToMySQL();
    private static String tableName = "BT";

    public static void main (String argus[]) throws ClientProtocolException, IOException, InterruptedException {

        String basic1 = "https://ready.arl.noaa.gov/hypubout/";
        String basic2 = "_trj001.gif";
        String year = "19" , month = "10" , day = "12" ,hour  = "02",  duration = "168";

        if(argus.length < 5 || argus.length > 5)
        {
            System.out.println("The number of imput arguments is wrong.");
        }
        else
        {

            year = argus[0];
            month = argus[1];
            day = argus[2];
            hour = argus[3];
            duration = argus[4];
            List< List<String>>  data = GetCityInfor.getCityInfo();
            ArrayList<String> errorCity = new ArrayList<String>();
            int length = data.size();
            for( int i = 0; i< data.size() ; i++)
            {
                String city = data.get(i).get(0);
                String lon =  data.get(i).get(1);
                String lat = data.get(i).get(2);
                String number  =  NaooTest.getInstance().test(lat , lon, year, month, day, hour , duration);
                System.out.println("Begin to get picture.");
                if (number != "-1") {
                    System.out.println("Job number:" + number);
                } else {
                    System.out.println("下载失败.Error. Cann't get job number.");
                    ArrayList<String> relist = new ArrayList<String>();
                    relist.add(city);
                    relist.add(lon);
                    relist.add(lat);
                    RetryList.add(relist);
                    continue;

                }
                String url = basic1 + number + basic2;
                String name = city + year + month + day +".gif";
                Thread.sleep(1000 * 20);   // 休眠10秒
                System.out.println("Picture url:" + url);
                if(DownloadPicture.download(url, storeUrl, name) == false)
                {
                    ArrayList<String> relist = new ArrayList<String>();
                    relist.add(city);
                    relist.add(lon);
                    relist.add(lat);
                    RetryList.add(relist);
                }
                //下载成功
                connenter.insert( tableName , storeUrl+name , name ,city  , "20"+year + month + day+ hour + "0000"  );
                System.out.println("Now/Total:" + (i+1)+"/"+length);
            }
            for( int i = 0 ; i< retryTime; i++)
            {
                for (int j = 0; j < RetryList.size(); j++) {
                    ArrayList<String> relist = RetryList.get(j);
                    String city = relist.get(0);
                    String lon = relist.get(1);
                    String lat = relist.get(2);
                    String number = NaooTest.getInstance().test(lat, lon, year, month, day, hour, duration);
                    if (number != "-1") {
                        System.out.println("Job number:" + number);
                    } else {
                        System.out.println("下载失败.Error. Cann't get job number.");
                        continue;
                    }
                    String url = basic1 + number + basic2;
                    String name = city + year + month + day + ".gif";
                    Thread.sleep(1000 * 20);   // 休眠10秒
                    System.out.println("Picture url:" + url);
                    if (DownloadPicture.download(url, storeUrl, name) == true) {
                        RetryList.remove(relist);
                        connenter.insert( tableName , storeUrl+name , name ,city  , "20"+year + month + day+ hour + "0000"  );

                    }
                }
            }
            System.out.println("These cities don't download ok.");
            System.out.println( errorCity.size());
            for( ArrayList<String> al : RetryList)
            {
                errorCity.add(al.get(0));
            }
            writeCsv( errorFile , errorCity);

        }

    }


    public static void writeCsv(String filePath , ArrayList<String> clist  )
    {
        try {
            CsvWriter csvWriter = new CsvWriter( filePath ,',' , Charset.forName("GBK") );
            for( String city : clist) {
                String[] writeLine = {city};
                System.out.println(writeLine);
                csvWriter.writeRecord(writeLine);
            }
            csvWriter.close();

        }
        catch ( Exception e)
        {
            e.printStackTrace();
        }
    }




}
