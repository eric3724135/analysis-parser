package stock.core.domain;

import lombok.Getter;
import lombok.Setter;
import stock.core.pojo.Quote;

@Getter
@Setter
public class KDObject extends Quote {

    /**
     * K值
     */
    private double kValue;
    /**
     * D值
     */
    private double dValue;

    /**
     * Quote 價差
     */
    private double diff;
}
