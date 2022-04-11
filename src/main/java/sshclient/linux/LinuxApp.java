package sshclient.linux;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import sshclient.jobs.SimpleJob;
import sshclient.shared.ReadHostsFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * This is the Linux entrypoint, it gets called when os.name = Linux
 */
public final class LinuxApp {
    private final Logger logger = LogManager.getLogger(LinuxApp.class);
    private final HashMap<String, List<String>> hosts;
    public static final int SSH_INTERVAL = 10;
    private String[] args;

    /**
     * Constructor for sshclient.linux.LinuxApp
     * @param mainArgs The String[] args of the main() call
     */
    public LinuxApp(final String[] mainArgs) {
        this.hosts = ReadHostsFiles.readHostList();
        this.args = mainArgs;
    }

    /**
     * Call LinuxApp.run() to start.
     */
    public void run() {
        logger.info("Starting LinuxApp");
        logHosts(hosts);
        launchQuartz();
    }

    /**
     * Handles creating a scheduler factory, registering jobs
     *  and starting the scheduler
     */
    private void launchQuartz() {
        Scheduler sched = null;
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            sched = sf.getScheduler();
            buildDynamicJobs(sched);
            sched.start();
            // sched.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
            if (sched != null) {
                try {
                    sched.shutdown();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Used in the run() method to print the lists of hosts
     * from ReadHostsFiles.readHostList()
     * @param map Takes a HashMap from ReadHostsFiles.readHostList()
     */
    private void logHosts(final HashMap<String, List<String>> map) {
        for (String s : hosts.keySet()) {
            logger.debug("Reading " + s + " hosts:");
            for (String x : hosts.get(s)) {
                logger.debug("\thost: " + x);
            }
        }
    }

    /**
     * Attempts to build dynamic quartz jobs from a list of hosts
     * @param quartz Quartz Scheduler
     * @throws SchedulerException
     */
    private void buildDynamicJobs(final Scheduler quartz)
            throws SchedulerException {
        List<String> linHosts = hosts.get("linux");
        for (String s : linHosts) {
            logger.debug("Building job and trigger for " + s);
            quartz.scheduleJob(
                    JobBuilder.newJob(SimpleJob.class)
                            .withIdentity(s)
                            .setJobData(new JobDataMap(settingsMap(s)))
                            .build(),
                    TriggerBuilder.newTrigger()
                            .withIdentity(s + "_trgr")
                            .startNow()
                            .withSchedule(
                                    simpleSchedule()
                                            .withIntervalInSeconds(SSH_INTERVAL)
                                            .repeatForever()
                            )
                            .build()
            );
        }
    }

    /**
     * settingsMap attempts to create a Map with a single
     * key-value pair to be used in the constructor of a
     * JobDataMap
     * @param name hostname
     * @return Map
     */
    private static Map<String, String> settingsMap(final String name) {
        HashMap<String, String> settings = new HashMap<>();
        settings.put("hostname", name);
        settings.put("id", UUID.randomUUID().toString());
        return settings;
    }
}
