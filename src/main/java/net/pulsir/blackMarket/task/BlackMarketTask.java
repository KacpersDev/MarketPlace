package net.pulsir.blackMarket.task;

import net.pulsir.blackMarket.BlackMarket;

public class BlackMarketTask implements Runnable {

    @Override
    public void run() {
        if (BlackMarket.getInstance().getBlackMarketManager().getCurrentTime() <= 0) {
            BlackMarket.getInstance().getBlackMarketManager().set();
        } else {
            BlackMarket.getInstance().getBlackMarketManager().setCurrentTime(BlackMarket
                    .getInstance().getBlackMarketManager().getCurrentTime() - 1);
        }
    }
}
