package parsingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import httpUtil.HttpPoolUtil;

public class parsingServiceImpl {
	private BufferedReader bufferedReader = null;
	private StringBuilder conTent = null;

	/**
	 * get all the content for parsing
	 * 
	 * @param urlin
	 *        incoming url
	 * @return all response content
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public String getContent(String urlin) throws URISyntaxException, IOException {
		// get url
		URI url = new URIBuilder().setScheme("https").setHost(urlin).build();
		conTent = new StringBuilder();

		HttpGet httpGet = new HttpGet(url);
		/*
		 * httpGet.setHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"
		 * );
		 */
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = HttpPoolUtil.httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			// get encoding info
			InputStream is = entity.getContent();
			String line = "";
			// convert to bufferstream
			bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while ((line = bufferedReader.readLine()) != null) {
				conTent.append(line);
			}
			is.close();
			return conTent.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
		}
	}

	/**
	 * get all page num
	 * 
	 * @param text
	 *            source code 
	 * @return all page num
	 */
	public String PageAll(String text) {
		String regex = "/共(\\d+?)页";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

}