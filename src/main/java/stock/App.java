package stock;

import lombok.extern.slf4j.Slf4j;
import stock.core.constants.Period;
import stock.core.constants.TickType;
import stock.core.pojo.Quote;
import stock.core.pojo.Tick;
import stock.parser.yahoo.YahooQuoteParser;
import stock.parser.yahoo.YahooTickParser;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 */
@Slf4j
public class App {
    public static void main(String[] args) throws IOException {

//        FubonAnalysisParser parser = new ForeignVentureNetBuyParser(){};
//        List<Quote> quotes = parser.getRankQuotes(parser.sendRequest());
//        log.info(quotes + "");

//        //YAHOO Tick
//        YahooTickParser parser = new YahooTickParser("2330", TickType.TICK, Period.ONE_MIN);
//        String body = parser.sendRequest(parser.getUrl());
//        List<Tick> list = parser.getData(body);
//        log.info("done");

        //Yahoo Quote
        YahooQuoteParser parser = new YahooQuoteParser("2330", TickType.QUOTE, Period.ONE_DAY);
        String body = parser.sendRequest(parser.getUrl());
        List<Quote> list = parser.getData(body);
        log.info("done");
    }
}
