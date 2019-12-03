package stock.core.domain;

import lombok.Getter;
import lombok.Setter;
import stock.core.pojo.Quote;

@Getter
@Setter
public class ForeignVentureNetBuyObject extends Quote {

    /**
     * 交易量
     */
    private int volume;
}
