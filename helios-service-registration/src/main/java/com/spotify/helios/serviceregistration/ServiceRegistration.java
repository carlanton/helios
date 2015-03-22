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

package com.spotify.helios.serviceregistration;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * A list of endpoints to for service registration.
 */
public class ServiceRegistration {

  private final List<Endpoint> endpoints;
  private final String jobName;
  private final String jobVersion;
  private final String jobHash;

  public ServiceRegistration(List<Endpoint> endpoints,
                             String jobName,
                             String jobVersion,
                             String jobHash) {
    this.endpoints = unmodifiableList(new ArrayList<>(endpoints));
    this.jobName = jobName;
    this.jobVersion = jobVersion;
    this.jobHash = jobHash;
  }

  public List<Endpoint> getEndpoints() {
    return endpoints;
  }

  public String getJobName() {
    return jobName;
  }

  public String getJobVersion() {
    return jobVersion;
  }

  public String getJobHash() {
    return jobHash;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private List<Endpoint> endpoints = new ArrayList<>();
    private String jobName;
    private String jobVersion;
    private String jobHash;

    @Deprecated
    public Builder endpoint(final String name,
                            final String protocol,
                            final int port) {
      endpoints.add(new Endpoint(name, protocol, port, "", "", null));
      return this;
    }

    public Builder endpoint(final String name,
                            final String protocol,
                            final int port,
                            final String domain,
                            final String host) {
      endpoints.add(new Endpoint(name, protocol, port, domain, host, null));
      return this;
    }

    public Builder endpoint(final String name,
                            final String protocol,
                            final int port,
                            final String domain,
                            final String host,
                            final List<String> tags) {
      endpoints.add(new Endpoint(name, protocol, port, domain, host, tags));
      return this;
    }

    public Builder jobId(final String jobName,
                         final String jobVersion,
                         final String jobHash) {
      this.jobName = jobName;
      this.jobVersion = jobVersion;
      this.jobHash = jobHash;
      return this;
    }

    public ServiceRegistration build() {
      return new ServiceRegistration(endpoints, jobName, jobVersion, jobHash);
    }
  }

  /**
   * A single service endpoint.
   */
  public static class Endpoint {

    private final String name;
    private final String protocol;
    private final int port;
    private final String domain;
    /** The hostname on which we will advertise this service in service discovery */
    private final String host;
    private final List<String> tags;

    public Endpoint(final String name, final String protocol, final int port,
                    final String domain, final String host, final List<String> tags) {
      this.name = name;
      this.protocol = protocol;
      this.port = port;
      this.domain = domain;
      this.host = host;
      this.tags = tags;
    }

    public String getHost() {
      return host;
    }

    public String getDomain() {
      return domain;
    }

    public String getName() {
      return name;
    }

    public String getProtocol() {
      return protocol;
    }

    public int getPort() {
      return port;
    }

    public List<String> getTags() {
      return tags;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .add("name", name)
          .add("protocol", protocol)
          .add("port", port)
          .add("domain", domain)
          .add("host", host)
          .add("tags", tags)
          .toString();
    }
  }
}
