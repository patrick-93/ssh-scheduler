package sshclient.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import sshclient.shared.SimpleSSH;
import sshclient.shared.SshConfigValues;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


/**
 * This class describes a simple sample Quartz Job
 */
public class SimpleJob implements Job {
    private final Logger logger = LogManager.getLogger(SimpleJob.class);

    /**
     * execute() is a required method for quartz jobs, it holds the main
     * logic for each job
     * @param ctx I'm not exactly sure what this is
     * @throws JobExecutionException Quartz exception
     */
    public void execute(final JobExecutionContext ctx)
            throws JobExecutionException {

        JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();

        logger.info("Simple job executed - "
                + dataMap.get("hostname") + " - "
                + dataMap.get("id")
        );
        String command = "/bin/lsblk";
        HashMap<String, ByteArrayOutputStream> output = SimpleSSH.runCommand(
                (String) dataMap.get("hostname"),
                "user",
                "SuperSecretPassword",
                SshConfigValues.SSH_PORT,
                command
        );
        logger.info("Job Result for " + dataMap.get("id")
                + "\n---- STDOUT ----\n" + output.get("stdout")
                + "\n---- STDERR ----\n" + output.get("stderr")
                + "\n");
    }
}
