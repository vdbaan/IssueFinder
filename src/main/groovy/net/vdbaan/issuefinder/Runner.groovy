package net.vdbaan.issuefinder

import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.util.IssueLogger

import java.util.logging.Logger

class Runner {
    private final static Logger logger = Logger.getLogger(Runner.class.getCanonicalName())

    static void main(String... args) {
        IssueLogger.setup(args)
        logger.info('starting')
        testJavaFX()
        Config.getInstance().attachShutDownHook()
        if (!args.contains('--reset-config')) {
            logger.info('Resetting the config')
            Config.getInstance().loadConfig()
        }
        Config.getInstance().checkDataDirectory()
        MainAppImpl.startup(args)
    }

    static void testJavaFX() {
        logger.info('testing availability of JavaFX')
        try {
            Class.forName('javafx.stage.Stage')
        } catch (ClassNotFoundException e) {
            logger.severe 'JavaFX8 Missing'
            logger.severe 'Please install JavaFX, for example:'
            logger.severe '- sudo apt-get install openjfx'
            System.exit(1)
        }
    }
}
