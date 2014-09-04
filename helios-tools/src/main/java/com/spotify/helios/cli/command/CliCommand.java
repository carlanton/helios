package com.spotify.helios.cli.command;

import com.spotify.helios.cli.Target;

import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public interface CliCommand {
  int run(final Namespace options, final List<Target> targets, final PrintStream out,
          final PrintStream err, final String username, final boolean json)
              throws IOException, InterruptedException;
}
