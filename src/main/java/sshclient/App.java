package sshclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sshclient.linux.LinuxApp;
import sshclient.windows.WinApp;

/**
 * This is the main entry point.
 */
public class App {

    protected App() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * main method.
     * @param args Takes a single file name
     */
    public static void main(final String[] args) {
        final Logger logger = LogManager.getLogger(App.class);

        if (System.getProperty("os.name").equals("Linux")) {
            logger.info("OS detected is Linux");
            LinuxApp app = new LinuxApp(args);
            app.run();
        } else {
            logger.info("OS detected is Windows");
            WinApp app = new WinApp(args);
            app.run();
        }
    }
}
