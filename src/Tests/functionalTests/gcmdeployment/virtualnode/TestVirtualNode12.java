package functionalTests.gcmdeployment.virtualnode;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.proactive.extra.gcmdeployment.API;
import org.objectweb.proactive.extra.gcmdeployment.GCMApplication.GCMApplicationDescriptor;
import org.objectweb.proactive.extra.gcmdeployment.core.VirtualNode;
import org.xml.sax.SAXException;


public class TestVirtualNode12 extends Abstract {
    @Test
    public void test()
        throws IllegalArgumentException, XPathExpressionException,
            FileNotFoundException, SAXException, IOException {
        GCMApplicationDescriptor gcma = API.getGCMApplicationDescriptor(getDescriptor());
        waitAllocation();

        VirtualNode vn1 = gcma.getVirtualNode("vn1");
        Assert.assertEquals(7, vn1.getNodes().size());

        VirtualNode vn2 = gcma.getVirtualNode("vn2");
        Assert.assertEquals(15, vn2.getNodes().size());
    }
}
