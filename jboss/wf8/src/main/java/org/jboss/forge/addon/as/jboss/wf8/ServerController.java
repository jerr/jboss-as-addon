/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.as.jboss.wf8;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.forge.addon.as.jboss.common.server.Server;
import org.jboss.forge.addon.as.jboss.common.util.Messages;
import org.jboss.forge.addon.as.jboss.common.util.Streams;

/**
 * This class is not thread-safe.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Singleton
public class ServerController {

    private ModelControllerClient client;

    private Server<ModelControllerClient> server;

    @PreDestroy
    protected void cleanUp() {
        shutdownServer();
        closeClient();
    }

    /**
     * If a server has been set then the client associated with the server is returned. If a server has not been set
     * and  a client has been set, then the client is returned. If neither a server or client have been set {@code
     * null} is returned.
     *
     * @return a client or {@code null} if no client or server is set
     */
    public ModelControllerClient getClient() {
        if (server != null) {
            return server.getClient();
        }
        return client;
    }

    /**
     * Checks to see if a client is available.
     *
     * @return {@code true} if a client is available, otherwise {@code false}
     */
    public boolean hasClient() {
        return server != null || client != null;
    }

    /**
     * Sets the client to be used.
     *
     * @param client the client to use
     *
     * @throws IllegalStateException if a client or server is currently set
     */
    public void setClient(final ModelControllerClient client) {
        // Don't allow a server to be set if current is not null
        if (client != null && hasClient()) {
            throw new IllegalStateException(Messages.INSTANCE.getMessage("server.client.already.connected"));
        }
        this.client = client;
    }

    /**
     * Closes the client if it's not attached to a server. If the client is attached to a server the close is ignored
     * and {@link #hasClient()} will continue to return {@code true}.
     */
    public void closeClient() {
        Streams.safeClose(client);
        client = null;
    }

    /**
     * Gets the server.
     *
     * @return the server that was set or {@code null} if not server is set
     */
    public Server<ModelControllerClient> getServer() {
        return server;
    }

    /**
     * Checks if there is a server set.
     *
     * @return {@code true} if a server is set, otherwise {@code false}
     */
    public boolean hasServer() {
        return server != null;
    }

    /**
     * Sets the server.
     *
     * @param server the server to set
     *
     * @throws IllegalStateException if a server has already be set
     */
    public void setServer(final Server<ModelControllerClient> server) {
        // Don't allow a server to be set if current is not null
        if (server != null && hasServer()) {
            throw new IllegalStateException(Messages.INSTANCE.getMessage("server.already.connected"));
        }
        // Check for a previous management client
        Streams.safeClose(client);
        this.server = server;
    }

    /**
     * Shuts down the running server. If the server is not running the shutdown is quietly ignored.
     */
    public void shutdownServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
