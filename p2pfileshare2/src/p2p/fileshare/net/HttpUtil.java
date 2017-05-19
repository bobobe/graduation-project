package p2p.fileshare.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


//http请求工具类
public class HttpUtil {
	
    //post请求
	public static JSONObject postHttp(String url,JSONObject jsonParam) {
		//post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    String str = EntityUtils.toString(result.getEntity());
                    System.out.print(str);
                    /**把json字符串转换成json对象**/
                    jsonResult = new JSONObject(str);
                } catch (Exception e) {
                    System.out.print("请求失败了"+e.toString());
                }
            }
        } catch (IOException e) {
        	System.out.print("请求失败"+e.toString());
        }
        return jsonResult;
	}
}