package sshclient.jobs;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Collections;
import java.util.Map;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * This class is supposed to be a mapper for all the possible jobs we can run
 */
public final class JobMapper {

    private static final int SECONDS = 5;

    private JobMapper() {
        // prevents calls from subclass
    }

    private static Map<String, String> settingsMap() {
        return Collections.singletonMap("hostname", "hostname1");
    }

    public static final JobDetail SIMPLE_JOB = JobBuilder
            .newJob(SimpleJob.class)
            .withIdentity("job1", "group1")
            .setJobData(new JobDataMap(settingsMap()))
            .build();

    public static final Trigger SIMPLE_TRIGGER = TriggerBuilder.newTrigger()
            .withIdentity("trigger1", "group1")
            .startNow()
            .withSchedule(
                    simpleSchedule()
                            .withIntervalInSeconds(SECONDS)
                            .repeatForever()
            )
            .forJob(SIMPLE_JOB)
            .build();
}
