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
package org.objectweb.proactive.ic2d.jmxmonitoring.util;

/**
 * This Enum describes the states a
 * </code> org.objectweb.proactive.ic2d.jmxmonitoring.data.Abstractdata </code> object can have.
 * @author The ProActive Team
 *
 */
public enum State {
    UNKNOWN, MIGRATING, NOT_RESPONDING, HIGHLIGHTED, NOT_HIGHLIGHTED, NOT_MONITORED, SERVING_REQUEST, WAITING_BY_NECESSITY, WAITING_BY_NECESSITY_WHILE_ACTIVE, WAITING_BY_NECESSITY_WHILE_SERVING, RECEIVED_FUTURE_RESULT, ACTIVE, WAITING_FOR_REQUEST, STEP_BY_STEP_BLOCKED, STEP_BY_STEP_RESUMED, STEP_BY_STEP_SLOWMOTION_ENABLED, STEP_BY_STEP_SLOWMOTION_DISABLED, STEP_BY_STEP_IMMEDIATESERVICE_ENABLED, STEP_BY_STEP_IMMEDIATESERVICE_DISABLED;
}