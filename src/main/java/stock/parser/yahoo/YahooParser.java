package stock.parser.yahoo;

import com.fasterxml.jackson.core.JsonProcessingException;
import stock.core.constants.Period;
import stock.core.constants.TickType;

import java.io.IOException;
import java.util.List;

public interface YahooParser<T> {
    /**
     * @return url
     */
    String getUrl();

    /**
     * jsoup 回傳結果
     *
     * @return json body
     */
    String sendRequest(String url) throws IOException;

    /**
     * @param response jsoup response
     * @return ticks
     */
    List<T> getData(String response) throws JsonProcessingException;

    /**
     * 傳入之period是否合法
     *
     * @param period period
     * @return boolean
     */
    boolean isPeriodValid(Period period);
}
