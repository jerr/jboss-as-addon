/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.as.jboss.as7;

import javax.inject.Singleton;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.forge.addon.as.jboss.as7.server.StandaloneServer;
import org.jboss.forge.addon.as.jboss.common.server.ServerController;
import org.jboss.forge.addon.as.jboss.common.server.ServerInfo;

/**
 * Singleton to control JBoss AS7 server
 * 
 * @author Jeremie Lagarde
 */
@Singleton
public class JBossAS7ServerController extends ServerController<ModelControllerClient>
{

   @Override
   protected StandaloneServer createServer(ServerInfo serverInfo)
   {
      return new StandaloneServer(serverInfo);
   }
}