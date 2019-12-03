package stock.parser.fubon;

import org.jsoup.nodes.Document;
import stock.core.pojo.Quote;

import java.io.IOException;
import java.util.List;

/**
 * 富邦網頁
 */
public interface FubonAnalysisParser {

    /**
     * @return url
     */
    String getUrl();

    /**
     * jsoup 回傳結果
     *
     * @return Document
     */
    Document sendRequest() throws IOException;

    /**
     * @param document jsoup response
     * @return rank quotes
     */
    List<Quote> getRankQuotes(Document document);
}
