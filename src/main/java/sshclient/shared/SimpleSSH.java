package sshclient.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Contains methods to abstract away SSH related stuff
 */
public final class SimpleSSH {
    private final Logger logger = LogManager.getLogger(SimpleSSH.class);

    /**
     * Takes arguments needed to open an SSH session and returns
     * a list of ByteArrayOutputStreams for stdout and stdin
     * @param hostname hostname of machine to connect
     * @param user username to use
     * @param pw password
     * @param port port number
     * @param command command to execute
     * @return List of ByteArrayOutputStream for stdout/stdin
     */
    public static HashMap<String, ByteArrayOutputStream> runCommand(
            final String hostname,
            final String user,
            final String pw,
            final int port,
            final String command
    ) {
        SshClient client = SshClient.setUpDefaultClient();

//        DefaultKnownHostsServerKeyVerifier customSkv =
//                new DefaultKnownHostsServerKeyVerifier(
//                        AcceptAllServerKeyVerifier.INSTANCE,
//                        false
//                );
//        client.setServerKeyVerifier(customSkv);
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        client.start();
        try (ClientSession session = client
                .connect(
                        user, hostname, SshConfigValues.SSH_PORT
                )
                .verify()
                .getSession()
        ) {
            session.addPasswordIdentity(pw);
            session.auth().verify(SshConfigValues.SSH_TIMEOUT);
            session.executeRemoteCommand(
                    command, stdout, stderr, StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }

        }
        HashMap<String, ByteArrayOutputStream> output = new HashMap<>();
        output.put("stdout", stdout);
        output.put("stderr", stderr);
        return output;
    }
}
