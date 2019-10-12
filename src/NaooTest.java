import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigestSpi;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



/**
 * httpclient池配置类
 * @author yanan
 *
 */
public class NaooTest {
	private CookieStore cookieStore=null;
	/**
	 * 单例模式
	 */
	private static NaooTest httpClientTool=null;
	private NaooTest(){}
	public synchronized static NaooTest getInstance() {
		if (httpClientTool == null) {  
			httpClientTool = new NaooTest();
		}  
		return httpClientTool;
	}
 
	public CookieStore getCookie(){
		return cookieStore;
	}
	public void SetCookie(CookieStore cookieStore){
		this.cookieStore=cookieStore;
	}
	 
	/**
	 * 模拟发送请求，下载图片
	 * 
	 * 
	 */
	public String  test(String lat , String lon , String year , String month , String day , String hour , String duration ) throws ClientProtocolException, IOException{
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		BasicCookieStore cookieStore = new BasicCookieStore();
		// 创建一个HttpClient
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
				.setDefaultCookieStore( cookieStore).build();
		CloseableHttpResponse res = null;

		try {
			HttpGet get = new HttpGet("https://ready.arl.noaa.gov/hypub-bin/trajasrc.pl");
			res = httpClient.execute(get);
			String message = EntityUtils.toString( res.getEntity() , "utf-8");
//			System.out.println("1");
			res.close();

			HttpPost post = new HttpPost("https://ready.arl.noaa.gov/hypub-bin/trajsrcm.pl");
			List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("Lat" ,"35"));
			valuePairs.add(new BasicNameValuePair("Latns" ,"35"));
			valuePairs.add(new BasicNameValuePair("Lon" ,"115"));
			valuePairs.add(new BasicNameValuePair("Lonew" ,"35"));
			valuePairs.add(new BasicNameValuePair("metdata" ,"GDAS1"));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
			post.setEntity(entity);
			res = httpClient.execute(post);
			message = EntityUtils.toString( res.getEntity() , "utf-8");
//			System.out.println("2");
			res.close();

			post = new HttpPost("https://ready.arl.noaa.gov/hypub-bin/traj1.pl");
			valuePairs = new LinkedList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("mfile" ,"current7days"));
			entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
			post.setEntity(entity);
			res = httpClient.execute(post);
			message = EntityUtils.toString( res.getEntity() , "utf-8");
//			System.out.println("3");
			res.close();

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("direction","Backward"));
			formparams.add(new BasicNameValuePair("vertical","0"));
			formparams.add(new BasicNameValuePair("Start year",year));
			formparams.add(new BasicNameValuePair("Start month",month));
			formparams.add(new BasicNameValuePair("Start day",day));
			formparams.add(new BasicNameValuePair("Start hour",hour));
			formparams.add(new BasicNameValuePair("duration",duration));
			formparams.add(new BasicNameValuePair("repeatsrc","24"));
			formparams.add(new BasicNameValuePair("ntrajs","3"));
			formparams.add(new BasicNameValuePair("Source lat",lat));
			formparams.add(new BasicNameValuePair("Source lon",lon));
			formparams.add(new BasicNameValuePair("Source lat2",""));
			formparams.add(new BasicNameValuePair("Source lon2",""));
			formparams.add(new BasicNameValuePair("Source lat3",""));
			formparams.add(new BasicNameValuePair("Source lon3",""));
			formparams.add(new BasicNameValuePair("Midlayer height","No"));
			formparams.add(new BasicNameValuePair("Source hgt1","500"));
			formparams.add(new BasicNameValuePair("Source hunit","0"));
			formparams.add(new BasicNameValuePair("Source hgt2","0"));
			formparams.add(new BasicNameValuePair("Source hgt3","0"));
			formparams.add(new BasicNameValuePair("gis","0"));
			formparams.add(new BasicNameValuePair("gsize","96"));
			formparams.add(new BasicNameValuePair("Zoom Factor","70"));
			formparams.add(new BasicNameValuePair("projection","0"));
			formparams.add(new BasicNameValuePair("Vertical Unit","1"));
			formparams.add(new BasicNameValuePair("Label Interval","6"));
			formparams.add(new BasicNameValuePair("color","Yes"));
			formparams.add(new BasicNameValuePair("colortype","Yes"));
			formparams.add(new BasicNameValuePair("pltsrc","1"));
			formparams.add(new BasicNameValuePair("circle","-1"));
			formparams.add(new BasicNameValuePair("county","arlmap"));
			formparams.add(new BasicNameValuePair("psfile","No"));
			formparams.add(new BasicNameValuePair("pdffile","Yes"));
			formparams.add(new BasicNameValuePair("mplot","NO"));
			HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");
			post = new HttpPost("https://ready.arl.noaa.gov/hypub-bin/traj2.pl");
			post.setEntity(reqEntity);
			res = httpClient.execute(post);
			message = EntityUtils.toString( res.getEntity() , "utf-8");
//			System.out.println( message );
			res.close();
			String mu = "obidno=";
			int from = message.indexOf(mu);
			int to = from + 7;
			String result = "";
			for(int i = to ; i < message.length(); i++)
			{
				if( message.charAt(i) == '\"')
				{
					break;
				}
				else
					result += message.charAt(i);
			}
			httpClient.close();
			return  result;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return  "-1";
		}

	}
//	public static void main(String[] args) throws ClientProtocolException, IOException{
//		NaooTest.getInstance().test();
//	}



}
