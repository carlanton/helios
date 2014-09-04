/*
 * Copyright (c) 2014 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.helios.cli;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;

import com.spotify.helios.client.HeliosClient;

import java.io.PrintStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Utils {

  public static <K, V> Map<K, V> allAsMap(final Map<K, ListenableFuture<V>> map)
      throws ExecutionException, InterruptedException {
    final Map<K, V> result = Maps.newHashMap();
    for (Map.Entry<K, ListenableFuture<V>> e : map.entrySet()) {
      result.put(e.getKey(), e.getValue().get());
    }
    return result;
  }

  public static HeliosClient getClient(final Target target, final PrintStream err,
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
