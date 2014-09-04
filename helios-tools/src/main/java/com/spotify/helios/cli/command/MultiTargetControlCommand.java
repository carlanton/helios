package com.spotify.helios.cli.command;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import com.spotify.helios.cli.Target;
import com.spotify.helios.cli.Utils;
import com.spotify.helios.client.HeliosClient;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class MultiTargetControlCommand implements CliCommand {
  MultiTargetControlCommand(final Subparser parser) {
    parser.setDefault("command", this).defaultHelp(true);
  }

  @Override
  public int run(Namespace options, List<Target> targets, PrintStream out, PrintStream err,
      String username, boolean json) throws IOException, InterruptedException {

    final Builder<ClusterClient> clientBuilder = ImmutableList.<ClusterClient>builder();
    for (final Target target : targets) {
      final HeliosClient client = Utils.getClient(target, err, username);
      if (client == null) {
        return 1;
      }
      clientBuilder.add(new ClusterClient(target, client));
    }

    final List<ClusterClient> clients = clientBuilder.build();

    final int result;
    try {
      result = run(options, clients, out, json);
    } catch (ExecutionException e) {
      final Throwable cause = e.getCause();
      // if target is a site, print message like
      // "Request timed out to master in ash.spotify.net (http://ash2-helios-a4.ash2.spotify.net)",
      // otherwise "Request timed out to master http://ash2-helios-a4.ash2.spotify.net:5800"
      if (cause instanceof TimeoutException) {
        err.println("Request timed out to master");
      } else {
        throw Throwables.propagate(cause);
      }
      return 1;
    } finally {
      for (ClusterClient cc : clients) {
        cc.getClient().close();
      }
    }
    return result;
  }

  abstract int run(Namespace options, List<ClusterClient> clients, PrintStream out, boolean json)
      throws ExecutionException, InterruptedException, IOException;
}