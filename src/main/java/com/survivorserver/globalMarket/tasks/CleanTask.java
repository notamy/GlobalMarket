package com.survivorserver.globalMarket.tasks;

import java.util.ArrayList;
import java.util.List;

import com.survivorserver.globalMarket.InterfaceListener;
import org.bukkit.entity.Player;

import com.survivorserver.globalMarket.InterfaceHandler;
import com.survivorserver.globalMarket.InterfaceViewer;
import com.survivorserver.globalMarket.Market;

public class CleanTask implements Runnable {
	
    Market market;
    InterfaceHandler handler;

    public CleanTask(Market market, InterfaceHandler handler) {
        this.market = market;
        this.handler = handler;
    }

    @Override
    public void run() {
        List<InterfaceViewer> toRemove = new ArrayList<InterfaceViewer>();
        for (InterfaceViewer viewer : handler.getAllViewers()) {
            Player player = market.getServer().getPlayer(viewer.getViewer());
            if (player == null) {
                toRemove.add(viewer);
                continue;
            }
            if (player.getOpenInventory() == null) {
                toRemove.add(viewer);
                if (market.useProtocolLib()) {
                    market.getPacket().getMessage().clearPlayer(player);
                }
                InterfaceListener.cleanInventory(player.getInventory());
            }
        }
        for (InterfaceViewer viewer : toRemove) {
            handler.removeViewer(viewer);
        }
    }
}
