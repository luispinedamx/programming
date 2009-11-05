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
package org.objectweb.proactive.examples.documentation.jmx;

//@snippet-start jmx_MyListener
import javax.management.Notification;
import javax.management.NotificationListener;
import org.objectweb.proactive.core.UniqueID;
import org.objectweb.proactive.core.jmx.notification.BodyNotificationData;
import org.objectweb.proactive.core.jmx.notification.NotificationType;


public class MyListener implements NotificationListener {

    public void handleNotification(Notification notification, Object handback) {
        // Get the type of the notification
        String type = notification.getType();
        // Get the data of the notification
        Object data = notification.getUserData();

        if (type.equals(NotificationType.bodyCreated)) {
            BodyNotificationData notificationData = (BodyNotificationData) data;
            UniqueID id = notificationData.getId();
            System.out.println("Active Object created with id:" + id);
        }
    }
}
//@snippet-end jmx_MyListener