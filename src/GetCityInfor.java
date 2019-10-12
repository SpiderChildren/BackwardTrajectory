import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetCityInfor {

    public static List<String> readCSV( File file)
    {
        List<String> dataList = new ArrayList<String>();
        BufferedReader br = null;
        try
        {
            DataInputStream in = new DataInputStream( new FileInputStream(file));
            br = new BufferedReader(new InputStreamReader(in , "GBK"));

            String line = "";
            while (( line = br.readLine()) != null)
            {
                dataList.add(line);
            }
        }
        catch( Exception e)
        {
            e.printStackTrace();
            System.out.println("读取文件失败");
        }
        return dataList;

    }

    public static  List< List<String>> getCityInfo()
    {
        File file = new File("Information.csv");
        List<String> dataList = readCSV(file);
        List< List<String>> result = new ArrayList< List<String>>();
        if(dataList!=null && !dataList.isEmpty()){
            for( int i =0 ; i<dataList.size(); i++)
            {
                String s = dataList.get(i);
                String []dataInfo = s.split(",");
                ArrayList<String> temp = new ArrayList<String>();
                for( String ts : dataInfo)
                {
                    temp.add(ts);
                }
                result.add(temp);
            }
        }

        return result;
    }


//    public static void main( String argus[])
//    {
//        List< List<String>> result = getCityInfo();
//        for (int i = 0; i < result.size(); i++)
//        {
//            System.out.print(result.get(i).size());
//            for (int j = 0; j < result.get(i).size(); j++)
//            {
//                System.out.print(result.get(i).get(j) + " ");
//            }
//            System.out.println();
//        }
//
//    }
}
