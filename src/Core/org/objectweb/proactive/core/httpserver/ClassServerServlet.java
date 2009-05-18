/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.httpserver;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mortbay.jetty.servlet.ServletHolder;
import org.objectweb.proactive.core.Constants;
import org.objectweb.proactive.core.config.PAProperties;
import org.objectweb.proactive.core.rmi.FileProcess;
import org.objectweb.proactive.core.util.ProActiveInet;
import org.objectweb.proactive.core.util.URIBuilder;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;


/**
 * A Servlet for HTTP dynamic class loading
 * 
 * Clients can ask for a class bytecode by doing a GET or a POST as follows:
 * <code>${NS}/com/example/Class.class</code>
 * 
 * If the class is known, the bytecode is send raw (application/java). If not a 404 error is
 * returned. The MOP class loader is used so even Stub class can be served.
 */
public class ClassServerServlet extends HttpServlet {
    final static public String NS = "/classServer";
    final static public String MAPPING = NS + "/*";

    final static private Logger logger = ProActiveLogger.getLogger(Loggers.CLASSLOADING);

    static ClassServerServlet servlet = null;

    static public synchronized ClassServerServlet get() {
        if (servlet == null) {
            // server must be started before creating the ClassServerServlet since PA_XMLHTTP_PORT can be 
            // set by the HTTPServer
            HTTPServer server = HTTPServer.get();
            servlet = new ClassServerServlet();
            server.registerServlet(new ServletHolder(servlet), ClassServerServlet.MAPPING);
        }

        return servlet;
    }

    private ClassServerServlet() {
        // Set the security manager
        if ((System.getSecurityManager() == null) && PAProperties.PA_SECURITYMANAGER.isTrue()) {
            System.setSecurityManager(new java.rmi.RMISecurityManager());
        }

        // Add the servlet to the codebase to handle object migration
        PAProperties.JAVA_RMI_SERVER_CODEBASE.setValue(getCodeBase());

    }

    public String getCodeBase() {
        final URI uri = URIBuilder.buildURI(URIBuilder.getHostNameorIP(ProActiveInet.getInstance()
                .getInetAddress()), NS + "/", Constants.XMLHTTP_PROTOCOL_IDENTIFIER,
                PAProperties.PA_XMLHTTP_PORT.getValueAsInt());

        return uri.toString();
    }

    static private String extractClassName(String pathStr) {
        String ret = pathStr;
        if (ret.startsWith("/")) {
            ret = ret.substring(1);
        }

        int index = ret.indexOf(".class");

        if (index > 1) {
            ret = ret.substring(0, index);
            ret = ret.replace('/', '.');
        }

        return ret;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {

        if (logger.isDebugEnabled()) {
            String pathInfo = req.getPathInfo();
            logger.warn("Serving request from " + pathInfo);
        }

        final String className = extractClassName(req.getPathInfo());
        final FileProcess fp = new FileProcess(null, className);

        boolean ok = true;
        byte[] bytes;
        try {
            bytes = fp.getBytes();
            resp.setContentType("application/java");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getOutputStream().write(bytes);
        } catch (ClassNotFoundException e) {
            ok = false;
            resp.setContentType("text/plain");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        if (logger.isDebugEnabled()) {
            final String from = req.getLocalAddr() + ":" + req.getLocalPort();
            final String status = ok ? "OK" : "Class Not Found";
            logger.debug("Served request from " + from + " for " + className + " status: " + status);
        }
    }
}