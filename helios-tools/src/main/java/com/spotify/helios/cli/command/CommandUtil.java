package com.spotify.helios.cli.command;

import com.spotify.helios.cli.Target;
import com.spotify.helios.client.HeliosClient;

import java.io.PrintStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class CommandUtil {

  static HeliosClient getClient(final Target target, final PrintStream err,
                                 final String username) {
  
    List<URI> endpoints = Collections.emptyList();
    try {
      endpoints = target.getEndpointSupplier().get();
    } catch (Exception ignore) {
      // TODO (dano): Nasty. Refactor target to propagate resolution failure in a checked manner.
    }
    if (endpoints.size() == 0) {
      err.println("Failed to resolve helios master in " + target);
      return null;
    }
  
    return HeliosClient.newBuilder()
        .setEndpointSupplier(target.getEndpointSupplier())
        .setUser(username)
        .build();
  }

}
