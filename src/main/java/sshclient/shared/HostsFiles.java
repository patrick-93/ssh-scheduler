package sshclient.shared;

/**
 * Hosts file environment variable names are set here
 */
public enum HostsFiles {
    LINUX("LIN_HOSTS_FILE"),
    WINDOWS("WIN_HOSTS_FILE");

    private String var;


    /**
     * Required constructor?
     * @param file
     */
    HostsFiles(final String file) {
        this.var = file;
    }

    /**
     * Getter
     * @return returns the enum value
     */
    public String getVar() {
        return var;
    }
}
