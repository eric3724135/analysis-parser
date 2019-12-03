package stock.parser.fubon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import stock.core.pojo.Quote;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

public class AbstractFubonAnalysisParser implements FubonAnalysisParser {
    @Override
    public String getUrl() {
        throw new NullPointerException();
    }

    @Override
    public Document sendRequest() throws IOException {
        return Jsoup.connect(getUrl()).sslSocketFactory(socketFactory()).get();
    }

    @Override
    public List<Quote> getRankQuotes(Document document) {
        return null;
    }

    private SSLSocketFactory socketFactory() {
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
