/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2008 INRIA/University of Nice-Sophia Antipolis
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
package org.objectweb.proactive.extensions.gcmdeployment.GCMDeployment.acquisition;

import java.util.ArrayList;
import java.util.List;


public class P2PEntry {

    private LocalClientEntry localClient;
    private List<String> hostsList;
    private int nodesToAsk;

    public P2PEntry() {
        this.localClient = new LocalClientEntry();
        this.hostsList = new ArrayList<String>();
        this.nodesToAsk = 0;
    }

    public LocalClientEntry getLocalClient() {
        return localClient;
    }

    public void setLocalClient(LocalClientEntry localClient) {
        this.localClient = localClient;
    }

    public List<String> getHostsList() {
        return hostsList;
    }

    public void setHostsList(List<String> hostsList) {
        this.hostsList = hostsList;
    }

    public int getNodesToAsk() {
        return nodesToAsk;
    }

    public void setNodesToAsk(int nodesToAsk) {
        this.nodesToAsk = nodesToAsk;
    }

}