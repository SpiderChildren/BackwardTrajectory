import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigestSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
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
	public String  test(String lat , String lon , String year , String month , String day , String hour ) throws ClientProtocolException, IOException{
		List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
		// POST传递参数
		formparams.add(new BasicNameValuePair("direction","Backward"));
		formparams.add(new BasicNameValuePair("vertical","0"));
		formparams.add(new BasicNameValuePair("Start year",year));
		formparams.add(new BasicNameValuePair("Start month",month));
		formparams.add(new BasicNameValuePair("Start day",day));
		formparams.add(new BasicNameValuePair("Start hour",hour));
		formparams.add(new BasicNameValuePair("duration","72"));
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
	
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(3000)//一、连接超时： 
				.setSocketTimeout(30000)// 二、读取数据超时
				.setConnectionRequestTimeout(30000)
				.build();  
		CloseableHttpClient client = HttpClientBuilder.create().build();//创建CloseableHttpClient
	
		//采用post方式请求url
		HttpPost post = new HttpPost("https://ready.arl.noaa.gov/hypub-bin/traj2.pl"); 
		post.setHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		post.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
		post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6");
		post.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
		post.setHeader(HttpHeaders.CONNECTION, "keep-alive");
		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		//_4c_=jVHLjuMgEPyVUZ9DAtjQOB8wt72NtMcRNjixYgULk3ge8r9vEzsZ7YxWWg6I6kd1dfEJ09GfYS%2BUNogaUaHUGzj59xH2n9AM%2Bb7m6xJ72MMxpWHc73bTNG3PwdrtIVx3sIEmOE9pUW1xWxBOH4RKw%2Bk5xOAuTXpN70MumXz9NLoTJeoYptFHij130bfh7Unn1kCT4Xd3dpQlSBkf462M0NilTHIfvUZI7hpkS3AgySDp0YfG9rmDltzAS%2BwOBx9%2F%2BXQMjoIv0boudeFse8o68gGcb%2B2lTxkmgk1vx7FrnB9PKQwwb%2BBtMcvoQqCsioJ2TeSM0SXPhypi51bXwFteWMUdqxBLVjovmBWOM11Y3lqlUEkFKyfqEg1HVYpqXja4cchvI3VV%2FRy5WPnPHuTiZ8%2B1u6sUhZdaNpbJtlasLJxhVe0dq1vjdNto67mDByWiUUiseqUU5s449Cuj%2BCquxF%2BaRfmYT06v5fx%2F5ObfWeR%2BfdHqm1ISb39BZY%2Blhm%2F5rHie5z8%3D; _ga=GA1.2.1382095915.1568702078; CGISESSID=57abd8222ee2ea8c70b3b38f99b53404
		post.setHeader("Cookie", "CGISESSID=9fcb7c98dace8132e7c40f006fbf19d2"); //session可能会时效,目前没发现具体超时时间,暂时先用一个浏览器的session吧

		post.setHeader(HttpHeaders.HOST, "ready.arl.noaa.gov");
		post.setHeader("Origin", "https://ready.arl.noaa.gov");
		post.setHeader(HttpHeaders.REFERER, "https://ready.arl.noaa.gov/hypub-bin/traj1.pl");
		post.setHeader("Upgrade-Insecure-Requests", "1");
		post.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36");
		post.setEntity(reqEntity);
		post.setConfig(requestConfig);  
	
		HttpClientContext httpClientContext = HttpClientContext.create();
		HttpResponse response = client.execute(post,httpClientContext);  
		HttpEntity resEntity=response.getEntity();  
		//此message为访问后的结果
		String message = EntityUtils.toString(resEntity, "utf-8");
	    System.out.println(message);
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
//		cookieStore = httpClientContext.getCookieStore();
//		SetCookie(cookieStore);
		//System.out.println(cookieStore.toString());
		//		if(response.getStatusLine().getStatusCode()==200){
		//			
		//		}else{  
		//			HttpEntity resEntity=response.getEntity();  
		//			System.out.println("log in error"+EntityUtils.toString(resEntity, "utf-8"));  
		//		} 
	
		//saveCookieStore(cookieStore, "cookie");
		client.close();
		return result;
	}
//	public static void main(String[] args) throws ClientProtocolException, IOException{
//		NaooTest.getInstance().test();
//	}



}
