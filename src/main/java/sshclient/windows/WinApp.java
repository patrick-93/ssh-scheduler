package sshclient.windows;

/**
 * Windows OS entrypoint
 */
public final class WinApp {
    private String[] args;

    /**
     * Constructor for sshclient.windows.WinApp
     * @param mainArgs The String[] args of the main() call
     */
    public WinApp(final String[] mainArgs) {
        this.args = mainArgs;
    }

    /**
     * Call WinApp.run() to start
     */
    public void run() {
        System.out.println("WinApp started");
    }
}
