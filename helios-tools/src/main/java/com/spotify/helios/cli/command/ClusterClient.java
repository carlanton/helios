package com.spotify.helios.cli.command;

import com.spotify.helios.cli.Target;
import com.spotify.helios.client.HeliosClient;

public class ClusterClient {
  private final Target target;
  private final HeliosClient client;

  public ClusterClient(Target target, HeliosClient client) {
    this.target = target;
    this.client = client;
  }

  public Target getTarget() {
    return target;
  }

  public HeliosClient getClient() {
    return client;
  }
}
