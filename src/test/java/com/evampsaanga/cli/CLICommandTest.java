package com.evampsaanga.cli;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import com.aqanetics.AQAInfo;
import com.aqanetics.common.base.cli.LocalCommandExecutor;

public class CLICommandTest {

  @Test
  @AQAInfo(name = "Test CLI", metadata = {"CLI", "Bash"})
  public void testBashCommand() throws IOException, InterruptedException {
    LocalCommandExecutor executor = new LocalCommandExecutor();
    // Execute the command and capture the logs
    String capturedLogs = executor.executeCommand("ls -alh", false);
    // Now you can assert the captured logs
    assertThat(capturedLogs).contains("pom.xml");
  }
}
