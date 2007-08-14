/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s):
 *
 * ################################################################
 */
package functionalTests.masterslave.userexception;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.objectweb.proactive.extra.masterslave.ProActiveMaster;
import org.objectweb.proactive.extra.masterslave.TaskException;
import org.objectweb.proactive.extra.masterslave.interfaces.Master;

import functionalTests.FunctionalTest;
import functionalTests.masterslave.A;
import static junit.framework.Assert.assertTrue;

/**
 * Test load balancing
 */
public class Test extends FunctionalTest {
    private URL descriptor = Test.class.getResource(
            "/functionalTests/masterslave/MasterSlave.xml");
    private Master master;
    private List<A> tasks;
    public static final int NB_TASKS = 4;

    @org.junit.Test
    public void action() throws Exception {
        boolean catched = false;

        master.solve(tasks);
        try {
            List<Integer> ids = master.waitAllResults();
            // we don't care of the results
            ids.clear();
        } catch (TaskException e) {
            assertTrue("Expected exception is the cause",
                e.getCause() instanceof ArithmeticException);
            catched = true;
        }
        assertTrue("Exception caught as excepted", catched);
    }

    @Before
    public void initTest() throws Exception {
        tasks = new ArrayList<A>();
        for (int i = 0; i < NB_TASKS; i++) {
            // tasks that throw an exception
            A t = new A(i, 0, true);
            tasks.add(t);
        }

        master = new ProActiveMaster();
        master.addResources(descriptor);
    }

    @After
    public void endTest() throws Exception {
        master.terminate(true);
    }
}
