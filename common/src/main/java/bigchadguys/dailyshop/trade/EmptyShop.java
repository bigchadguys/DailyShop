package bigchadguys.dailyshop.trade;

import java.util.stream.Stream;

public class EmptyShop extends Shop {

    public static final EmptyShop INSTANCE = new EmptyShop();

    @Override
    public Stream<Trade> getTrades() {
        return Stream.empty();
    }

}
