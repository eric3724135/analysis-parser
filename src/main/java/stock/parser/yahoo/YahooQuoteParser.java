package stock.parser.yahoo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import stock.core.constants.Period;
import stock.core.constants.TickType;
import stock.core.pojo.Quote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class YahooQuoteParser extends AbstractYahooParser<Quote> {

    //https://tw.quote.finance.yahoo.net/quote/q?type=ta&perd=d&mkt=10&sym=2330
    private final static String URL = "https://tw.quote.finance.yahoo.net/quote/q?type=%s&perd=%s&mkt=10&sym=%s";

    public YahooQuoteParser(String symbol, TickType type, Period period) {
        super(symbol, type, period);
    }

    @Override
    public String getUrl() {
        return String.format(URL, super.getType().getYahooCode(), super.getPeriod().getCode(), super.getSymbol());
    }

    @Override
    public List<Quote> getData(String response) throws JsonProcessingException {
        List<Quote> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        //TODO period 不同有不一樣
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        String str = response.split("\\(")[1].split("\\)")[0];
        JsonNode node = mapper.readTree(str);
        //TODO
        JsonNode quotes = node.get("ta");
        JsonNode detail = node.get("mem");
        String symbol = detail.get("id").textValue();
        String name = detail.get("name").textValue();
        Iterator<JsonNode> iterator = quotes.elements();
        while (iterator.hasNext()) {

            JsonNode tickNode = iterator.next();
            try {
                Quote quote = new Quote();
                quote.setSymbol(symbol);
                quote.setName(name);
                quote.setPeriod(Period.ONE_MIN);
                quote.setHigh(tickNode.get("h").doubleValue());
                quote.setLow(tickNode.get("l").doubleValue());
                quote.setOpen(tickNode.get("o").doubleValue());
                quote.setClose(tickNode.get("c").doubleValue());
                Date dateTime = sdFormat.parse(tickNode.get("t").toString());
                quote.setTradeDate(dateTime);
                result.add(quote);
            } catch (ParseException e) {
                log.error(e.toString(), e);
            }
        }
        return result;
    }

    @Override
    public boolean isPeriodValid(Period period) {
        if (period.equals(Period.FIFTEEN_MIN))
            return false;
        return true;
    }
}
