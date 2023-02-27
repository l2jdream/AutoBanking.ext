package l2.gameserver;

import l2.commons.configuration.ExProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class AutoBankingConfig {
    private static final String AUTO_GOLD_BAR_FILE = "../Game/config/custom/AutoGoldBar.properties";
    private static final Logger LOG = LoggerFactory.getLogger(AutoBankingConfig.class);
    /**
     * AutoGoldBar
     */
    public static boolean AUTO_GOLD_BAR_ENABLE;
    public static boolean AUTO_GOLD_BAR_AVAILABLE_ONLY_PREMIUM;
    public static Long AUTO_GOLD_BAR_ADENA_COUNT;
    public static int AUTO_GOLD_BAR_ITEM_ID;

    public static void load() {
        // load configuration file
        ExProperties auto_gold_bar = initProperties(AUTO_GOLD_BAR_FILE);

        AUTO_GOLD_BAR_ENABLE = auto_gold_bar.getProperty("AutoGoldBarEnable", true);
        AUTO_GOLD_BAR_AVAILABLE_ONLY_PREMIUM = auto_gold_bar.getProperty("AutoGoldBarOnlyForPremium", false);
        AUTO_GOLD_BAR_ADENA_COUNT = auto_gold_bar.getProperty("AutoGoldBarAdenaCount", 250_000_000L);
        AUTO_GOLD_BAR_ITEM_ID = auto_gold_bar.getProperty("AutoGoldBarItemId", 3470);
    }
    private static final ExProperties initProperties(String filename) {
        ExProperties result = new ExProperties();

        try {
            result.load(new File(filename));
        } catch (final IOException e) {
            LOG.error(AutoBankingConfig.class.getSimpleName() + ": Error loading " + filename + " config.");
        }
        return result;
    }

}
