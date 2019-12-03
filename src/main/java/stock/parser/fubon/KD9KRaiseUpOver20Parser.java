package stock.parser.fubon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import stock.core.constants.Period;
import stock.core.domain.KDObject;
import stock.core.pojo.Quote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class KD9KRaiseUpOver20Parser extends AbstractFubonAnalysisParser {

    private static final String URL = "https://fubon-ebrokerdj.fbs.com.tw/z/zk/zk3/zkparse_710_NA.djhtm";

    @Override
    public String getUrl() {
        return URL;
    }

    @Override
    public List<Quote> getRankQuotes(Document document) {
        List<Quote> result = new ArrayList<>();
        Date tradeDate = this.getTradeDate(document);

        Elements elements = document.select("td.zkt01 > table > tbody > tr");
        for (int i = 2; i < elements.size() - 1; i++) {
            try {
                Elements vals = elements.get(i).select("td");
                KDObject kdObject = new KDObject();
                String id = vals.get(0).text().substring(0, 4);
                String stock = vals.get(0).text().substring(4);
                double close = Double.parseDouble(vals.get(1).text());
                double diff = Double.parseDouble(vals.get(2).text());
                double kValue = Double.parseDouble(vals.get(4).text());
                double dValue = Double.parseDouble(vals.get(5).text());
                kdObject.setSymbol(id);
                kdObject.setName(stock);
                kdObject.setClose(close);
                kdObject.setDiff(diff);
                kdObject.setKValue(kValue);
                kdObject.setDValue(dValue);
                kdObject.setTradeDate(tradeDate);
                kdObject.setPeriod(Period.ONE_DAY);
                log.debug(String.format("%s %s  %s  %s  %s  %s",
                        vals.get(0).text(), vals.get(1).text(), vals.get(2).text(), vals.get(3).text(),
                        vals.get(4).text(), vals.get(5).text()));
                result.add(kdObject);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }

        return result;
    }

    private Date getTradeDate(Document document) {
        Elements date = document.select("div.zkf0");
        String[] dtStr = date.text().substring(3).split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Integer.parseInt(dtStr[0]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dtStr[1]));
        Date tradeDate = DateUtils.truncate(cal.getTime(), Calendar.DATE);
        log.info(tradeDate + "");
        return tradeDate;
    }


}
