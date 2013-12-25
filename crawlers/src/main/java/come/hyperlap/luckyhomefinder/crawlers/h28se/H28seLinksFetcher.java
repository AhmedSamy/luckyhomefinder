package come.hyperlap.luckyhomefinder.crawlers.h28se;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.hyperlap.luckhomefinder.crawlers.Exceptions.LinksFetcherException;
import com.hyperlap.luckhomefinder.crawlers.domain.ConnectionConfigs;
import com.hyperlap.luckyhomefinder.crawler.webconstants.H28seConstants;
import com.hyperlap.luckyhomefinder.crawlers.service.LinksFetcher;

/**
 * Implementations of links fetcher service to handle fetching links from H28Se
 * site
 * */
public class H28seLinksFetcher implements LinksFetcher {

	
	public List<String> fetchLinks(String link) throws LinksFetcherException {
		Connection connection = InitConnection();
		Response response = null;
		try {
			response = connection.execute();
			parseLinks(response);
		} catch (final IOException e) {
			throw new LinksFetcherException(e);
		}

		return null;
	}

	/**
	 * Initialize connection object used to connect to the website.
	 * 
	 * @return Connection object used to perform connection to the website.
	 * */
	protected final Connection InitConnection() {
		Connection connection = Jsoup
				.connect(H28seConstants.MAINSEARCH.value());
		connection.data(H28seConstants.ACTIONKEY.value(),
				H28seConstants.ACTIONVALUE.value());
		connection.data(H28seConstants.ALLDATAKEY.value(),
				H28seConstants.ALLDATAVALUE.value());
		connection.maxBodySize(0);
		connection.timeout(0);
		connection.method(Method.POST);
		connection.referrer(H28seConstants.MAINSEARCH.value());
		connection.userAgent(ConnectionConfigs.USERAGENT.value());
		return connection;

	}

	/**
	 * @throws IOException
	 * 
	 * */
	protected void parseLinks(Response response) throws IOException {
		Document document = response.parse();
		Elements elements = document.getElementsByTag("a");
		String regx = "\\bproperty\\d+\\Whtml\\b";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher;
		for (Element element : elements) {
			String linkHtml = element.attr("href");
			matcher = pattern.matcher(linkHtml);
			while (matcher.find()) {
				System.out.println(matcher.group());

			}
		}
	}
}
