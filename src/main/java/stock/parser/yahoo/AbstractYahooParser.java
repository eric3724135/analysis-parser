package stock.parser.yahoo;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import stock.core.constants.Period;
import stock.core.constants.TickType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

@Slf4j
@Getter
public class AbstractYahooParser<T> implements YahooParser<T> {

    private String symbol;

    private TickType type;

    private Period period;

    public AbstractYahooParser(String symbol, TickType type, Period period) {
        this.symbol = symbol;
        this.type = type;
        this.period = period;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String sendRequest(String url) throws IOException {
        return Jsoup.connect(url).sslSocketFactory(socketFactory()).execute().body();
    }

    @Override
    public List<T> getData(String response) throws JsonProcessingException {
        return null;
    }

    @Override
    public boolean isPeriodValid(Period period) {
        return false;
    }

    SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
}
