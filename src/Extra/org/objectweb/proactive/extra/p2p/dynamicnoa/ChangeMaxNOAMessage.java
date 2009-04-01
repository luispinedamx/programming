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
package org.objectweb.proactive.extra.p2p.dynamicnoa;

import org.objectweb.proactive.extra.p2p.service.P2PService;
import org.objectweb.proactive.extra.p2p.service.messages.Message;


public class ChangeMaxNOAMessage extends Message {
    /**
     * 
     */
    private static final long serialVersionUID = 40L;
    protected int noa;

    public ChangeMaxNOAMessage(int ttl, int noa) {
        super(ttl);
        this.noa = noa;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(P2PService target) {
        target.getAcquaintanceManager().setMaxNOA(this.noa);
    }

    /**
     * Nothing to do, the message should not be transmited
     */
    @Override
    public void transmit(P2PService acq) {
    }
}