package stock.parser.yahoo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import stock.core.constants.Period;
import stock.core.constants.TickType;
import stock.core.pojo.Tick;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class YahooTickParser extends AbstractYahooParser<Tick> {

    private final static String URL_TEMPLATE = "https://tw.quote.finance.yahoo.net/quote/q?type=%s&perd=1m&mkt=10&sym=%s";

    public YahooTickParser(String symbol, TickType type, Period period) {
        super(symbol, type, period);
    }

    @Override
    public String getUrl() {
        //TODO period
        return String.format(URL_TEMPLATE, super.getType().getYahooCode(), super.getSymbol());
    }

    @Override
    public List<Tick> getData(String response) throws JsonProcessingException {
        List<Tick> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String str = response.split("\\(")[1].split("\\)")[0];
        JsonNode node = mapper.readTree(str);
        JsonNode ticks = node.get("tick");
        JsonNode detail = node.get("mem");
        String symbol = detail.get("id").textValue();
        String name = detail.get("name").textValue();
        Iterator<JsonNode> iterator = ticks.elements();
        while (iterator.hasNext()) {
            JsonNode tickNode = iterator.next();
            try {
                Tick tick = new Tick();
                tick.setSymbol(symbol);
                tick.setName(name);
                tick.setPeriod(Period.ONE_MIN);
                tick.setPrice(tickNode.get("p").intValue());
                tick.setVolume(tickNode.get("v").intValue());
                Date dateTime = sdFormat.parse(tickNode.get("t").toString());
                tick.setTime(dateTime);
                result.add(tick);
            } catch (ParseException e) {
                log.error(e.toString(), e);
            }
        }
        return result;
    }
}
