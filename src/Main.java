import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class Main {

    //https://ready.arl.noaa.gov/hypubout/157786_trj001.gif

    public static void main (String argus[]) throws ClientProtocolException, IOException, InterruptedException {

        String basic1 = "https://ready.arl.noaa.gov/hypubout/";
        String basic2 = "_trj001.gif";
        if(argus.length < 6 || argus.length > 6)
        {
            System.out.println("The number of imput arguments is wrong.");
        }
        else
        {
            String number  =  NaooTest.getInstance().test(argus[0] ,argus[1], argus[2], argus[3], argus[4], argus[5] );
            System.out.println(number);
            String url = basic1 + number + basic2;
            String storeUrl = "D:\\weatherPicture";
            String name = "BC.gif";
            Thread.sleep(1000*30);   // 休眠3秒
            DownloadPicture.download( url ,storeUrl , name);
        }

    }



}
