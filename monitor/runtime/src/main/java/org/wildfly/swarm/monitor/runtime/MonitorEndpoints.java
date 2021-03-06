/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.monitor.runtime;

import javax.naming.NamingException;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * @author Heiko Braun
 * @since 18/02/16
 */
public class MonitorEndpoints implements HttpHandler {


    public MonitorEndpoints(HttpHandler parent) {
        try {
            this.monitor = Monitor.lookup();
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup monitor", e);
        }
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        if ("/node".equals(exchange.getRequestPath())) {
            nodeInfo(exchange);
        } else if ("/heap".equals(exchange.getRequestPath())) {
            heap(exchange);
        } else if ("/threads".equals(exchange.getRequestPath())) {
            threads(exchange);
        }

    }

    private void nodeInfo(HttpServerExchange exchange) {
        exchange.getResponseSender().send(monitor.getNodeInfo().toJSONString(false));
    }

    private void heap(HttpServerExchange exchange) {
        exchange.getResponseSender().send(monitor.heap().toJSONString(false));
    }

    private void threads(HttpServerExchange exchange) {
        exchange.getResponseSender().send(monitor.threads().toJSONString(false));
    }

    private final Monitor monitor;
}

