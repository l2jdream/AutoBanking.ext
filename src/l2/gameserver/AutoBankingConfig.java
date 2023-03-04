package l2.gameserver;

import l2.commons.configuration.ExProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class AutoBankingConfig {
    private static final String AUTO_GOLD_BAR_FILE = "../Game/config/custom/AutoBanking.properties";
    private static final Logger LOG = LoggerFactory.getLogger(AutoBankingConfig.class);
    /**
     * AutoGoldBar
     */
    public static boolean AUTO_BANKING_ENABLE;
    public static boolean AUTO_BANKING_AVAILABLE_ONLY_PREMIUM;
    public static Long AUTO_BANKING_ADENA_COUNT;
    public static int AUTO_BANKING_ITEM_ID;

    public static void load() {
        // load configuration file
        ExProperties auto_gold_bar = initProperties();

        AUTO_BANKING_ENABLE = auto_gold_bar.getProperty("AutoBankingEnable", true);
        AUTO_BANKING_AVAILABLE_ONLY_PREMIUM = auto_gold_bar.getProperty("AutoBankingOnlyForPremium", false);
        AUTO_BANKING_ADENA_COUNT = auto_gold_bar.getProperty("AutoBankingAdenaCount", 250_000_000L);
        AUTO_BANKING_ITEM_ID = auto_gold_bar.getProperty("AutoBankingItemId", 3470);
    }
    private static ExProperties initProperties() {
        ExProperties result = new ExProperties();

        try {
            result.load(new File(AutoBankingConfig.AUTO_GOLD_BAR_FILE));
        } catch (final IOException e) {
            LOG.error(AutoBankingConfig.class.getSimpleName() + ": Error loading " + AutoBankingConfig.AUTO_GOLD_BAR_FILE + " config.");
        }
        return result;
    }

}
