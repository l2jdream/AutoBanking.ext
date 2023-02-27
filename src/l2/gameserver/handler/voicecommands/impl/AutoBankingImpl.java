package l2.gameserver.handler.voicecommands.impl;

import l2.gameserver.AutoBankingConfig;
import l2.gameserver.data.htm.HtmCache;
import l2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2.gameserver.model.Player;
import l2.gameserver.network.l2.components.SystemMsg;
import l2.gameserver.scripts.Functions;

import java.text.DecimalFormat;

public class AutoBankingImpl extends Functions implements IVoicedCommandHandler {

    private static final String[] COMMANDS = {"autogoldbar", "goldbar", "deposit", "withdraw"};

    @Override
    public boolean useVoicedCommand(String command, Player player, String args) {

        if (!AutoBankingConfig.AUTO_GOLD_BAR_ENABLE || player == null) {
            return false;
        }

        if (command.equalsIgnoreCase(COMMANDS[0])) {
            if (player.getVarBoolean("isAutoGoldBar")) {
                player.unsetVar("isAutoGoldBar");
                player.sendMessage("AutoGoldBar: deactivated");
                htmlBuilder(player);
                return false;
            } else {
                player.setVar("isAutoGoldBar", "true", -1);
                player.sendMessage("AutoGoldBar: activated");
                htmlBuilder(player);
                return true;
            }
        } else if (command.equalsIgnoreCase(COMMANDS[1])) {
            htmlBuilder(player);
            return true;
        } else if (command.equalsIgnoreCase(COMMANDS[2])) {
            Boolean deposit = this.deposit(command, player, args);
            htmlBuilder(player);
            return deposit;
        } else if (command.equalsIgnoreCase(COMMANDS[3])) {
            Boolean withdraw = this.withdraw(command, player, args);
            htmlBuilder(player);
            return withdraw;
        }
        return false;
    }

    private void htmlBuilder(Player player) {
        if (player != null) {
            String html = HtmCache.getInstance().getNotNull("scripts/services/goldbar/autogoldbar.htm", player);
            html = html.replace("%onOff%", player.getVarBoolean("isAutoGoldBar") ? "OFF" : "ON");
            html = html.replace("%enabled%", player.getVarBoolean("isAutoGoldBar") ? "<font color=\"00FF00\">Active</font>" : "<font color=\"FF0000\">Disabled</font>");
            html = html.replace("%goldbar%", String.valueOf(player.getInventory().getCountOf(AutoBankingConfig.AUTO_GOLD_BAR_ITEM_ID)));
            html = html.replace("%adena%", formatNumber(player.getInventory().getAdena()));
            html = html.replace("%adena_golbar%", formatNumber(AutoBankingConfig.AUTO_GOLD_BAR_ADENA_COUNT));
            html = html.replace("%deposit_command%", "bypass -h user_deposit");
            html = html.replace("%withdraw_command%", "bypass -h user_withdraw");
            Functions.show(html, player, null);

        }
    }

    private String formatNumber(long number) {
        DecimalFormat df = new DecimalFormat("#,###,###,###,###,###,##0");
        return df.format(number).replace(",", ".");
    }

    public boolean deposit(String command, Player player, String args) {
        if (getItemCount(player, 57) < (long) AutoBankingConfig.AUTO_GOLD_BAR_ADENA_COUNT) {
            player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return false;
        } else {
            removeItem(player, 57, (long) AutoBankingConfig.AUTO_GOLD_BAR_ADENA_COUNT);
            player.sendMessage("Deposit successfully converted");
            addItem(player, AutoBankingConfig.AUTO_GOLD_BAR_ITEM_ID, 1L);
            return true;
        }
    }

    public boolean withdraw(String command, Player player, String args) {
        if (getItemCount(player, AutoBankingConfig.AUTO_GOLD_BAR_ITEM_ID) < 1L) {
            player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return false;
        } else if (getItemCount(player, 57) >= 2_147_483_647L) {
            player.sendMessage("You can't withdraw more than 2.147.483.647 adena");
            return false;
        } else {
            removeItem(player, AutoBankingConfig.AUTO_GOLD_BAR_ITEM_ID, 1L);
            player.sendMessage("Withdraw successfully converted");
            addItem(player, 57, (long) AutoBankingConfig.AUTO_GOLD_BAR_ADENA_COUNT);
            return true;
        }
    }

    @Override
    public String[] getVoicedCommandList() {
        return COMMANDS;
    }
}
