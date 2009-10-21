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
 *  Initial developer(s):               The ActiveEon Team
 *                        http://www.activeeon.com/
 *  Contributor(s):
 *
 *
 * ################################################################
 * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.body.tags.tag;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.UniqueID;
import org.objectweb.proactive.core.body.tags.Tag;


/**
 * DsiTag provide a tag to follow the Distributed Service flow
 * by propagating the same UniqueID during a flow execution. 
 */
public class DsiTag extends Tag {

    public static final String IDENTIFIER = "PA_TAG_DSI";

    /**
     * Constructor setting the Tag name "PA_TAG_DSI"
     * and an UniqueID concatened with the sequence number of the request
     * as the tag DATA.
     */
    public DsiTag(UniqueID id, long seq) {
        super(IDENTIFIER, "" + id.getCanonString() + "::" + seq + "::" + null + "::" + 0);
    }

    /**
     * This tag return itself at each propagation.
     */
    public Tag apply() {
        // Set the current body and seq number as the parent for the future request
        String[] dataInfos = data.toString().split("::");
        String currentId = PAActiveObject.getBodyOnThis().getID().getCanonString();
        long currentSeq = PAActiveObject.getContext().getCurrentRequest().getSequenceNumber();
        setData(dataInfos[0] + "::" + dataInfos[1] + "::" + currentId + "::" + currentSeq, true);
        return this;
    }

}