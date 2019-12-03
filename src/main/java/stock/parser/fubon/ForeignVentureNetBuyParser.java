package stock.parser.fubon;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import stock.core.constants.Period;
import stock.core.domain.ForeignVentureNetBuyObject;
import stock.core.pojo.Quote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 外資買超排行榜
 */
@Slf4j
@Getter
public abstract class ForeignVentureNetBuyParser extends AbstractFubonAnalysisParser {

    private static final String URL = "https://fubon-ebrokerdj.fbs.com.tw/z/zg/zg_D_0_2.djhtm";

    private int NetBuyDays = 1;

    @Override
    public String getUrl() {
        return URL;
    }

    @Override
    public List<Quote> getRankQuotes(Document document) {
        List<Quote> result = new ArrayList<>();
        Date tradeDate = this.getTradeDate(document);

        Elements elements = document.select("table#oMainTable > tbody > tr");
        for (int i = 2; i < elements.size() - 1; i++) {
            try {
                Elements values = elements.get(i).select("td");
                ForeignVentureNetBuyObject netBuyObject = new ForeignVentureNetBuyObject();
                String id = values.get(1).text().split(" ")[0];
                String stock = values.get(1).text().split(" ")[1];
                double close = Double.parseDouble(values.get(2).text());
                DecimalFormat formatter = new DecimalFormat("#,###");
                Number number = formatter.parse(values.get(5).text());
                int volume = number.intValue();
                netBuyObject.setSymbol(id);
                netBuyObject.setName(stock);
                netBuyObject.setClose(close);
                netBuyObject.setTradeDate(tradeDate);
                netBuyObject.setVolume(volume);
                netBuyObject.setPeriod(Period.ONE_DAY);
                log.debug(String.format("%s %s  %s  %s  %s  %s",
                        values.get(0).text(), values.get(1).text(), values.get(2).text(), values.get(3).text(),
                        values.get(4).text(), values.get(5).text()));
                result.add(netBuyObject);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }
        return result;
    }

    private Date getTradeDate(Document document) {
        Elements date = document.select("div.t11");
        String[] dtStr = date.text().substring(3).split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Integer.parseInt(dtStr[0]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dtStr[1]));
        Date tradeDate = DateUtils.truncate(cal.getTime(), Calendar.DATE);
        log.info(tradeDate + "");
        return tradeDate;
    }
}
