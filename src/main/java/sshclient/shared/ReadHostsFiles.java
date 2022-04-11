package sshclient.shared;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * ReadHostsFiles contains methods to get a list of hosts
 * from a file specified in HostsFiles.java and returns
 * a list of hostnames
 */
public final class ReadHostsFiles {

    /**
     * Constructor for ReadHostsFiles.java
     */
    protected ReadHostsFiles() {

    }

    /**
     * Calls getHosts() with a filename for both values from
     * HostsFiles enum then adds the lists returned to a
     * HashMap
     * @return HashMap<String, List<String>>
     */
    public static HashMap<String, List<String>> readHostList() {
        HashMap<String, List<String>> hosts = new HashMap<>();
        hosts.put(
                "linux",
                getHosts(System.getenv(HostsFiles.LINUX.getVar()))
        );
        hosts.put(
                "windows",
                getHosts(System.getenv(HostsFiles.WINDOWS.getVar()))
        );
        return hosts;
    }

    /**
     * Builds a list of Strings from a file name
     * @param fileName String name of a file
     * @return List<String>
     */
    private static List<String> getHosts(final String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> hosts = new ArrayList<>();
        try {
            File f = new File(fileName);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    hosts.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hosts;
    }
}
