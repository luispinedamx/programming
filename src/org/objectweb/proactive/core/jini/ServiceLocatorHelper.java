/* 
* ################################################################
* 
* ProActive: The Java(TM) library for Parallel, Distributed, 
*            Concurrent computing with Security and Mobility
* 
* Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
* Contact: proactive-support@inria.fr
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
package org.objectweb.proactive.core.jini;

import java.rmi.RMISecurityManager;

import net.jini.core.discovery.LookupLocator;
import net.jini.discovery.LookupDiscovery;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import com.sun.jini.reggie.CreateLookup;
import com.sun.jini.reggie.RegistrarImpl;
import com.sun.jini.start.ServiceStarter;
import net.jini.core.lookup.ServiceMatches;
import org.objectweb.proactive.core.node.jini.JiniNode;

import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.DiscoveryEvent;

import java.net.InetAddress;
import java.io.File;


public class ServiceLocatorHelper  implements DiscoveryListener {

  protected static  int MAX_RETRY = 3;
  protected static  long MAX_WAIT = 10000L;

  protected static LookupLocator lookup = null;
  protected static ServiceRegistrar registrar = null;


  /**
   * settings of the service locator
   */
  protected boolean shouldCreateServiceLocator = true;
  protected boolean locatorChecked;
  protected static boolean multicastLocator = false;

  // Static variables for the serviceStarter
  private static String starterClass = CreateLookup.class.getName();
  private static String implClass = RegistrarImpl.class.getName();
  private static String resourcename = "lookup";

  private static String policy = System.getProperty("user.home") + System.getProperty("file.separator") + ".java.policy";   

  private static String host = null;

  static {  
    try {
      host = InetAddress.getLocalHost().getHostName();
    } catch(java.net.UnknownHostException  e) {
      System.err.println("Lookup failed: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static String grpName = "public";

  private static final String tmpDir = createTempDirectory(host);
  


  //
  // -- Constructors -----------------------------------------------
  //
  
  public ServiceLocatorHelper() {
    System.out.println("ServiceLocatorHelper() constructor");
  }
  
  
  //
  // -- PUBLIC METHODS -----------------------------------------------
  //
  
  
  public boolean shouldCreateServiceLocator() {
    return shouldCreateServiceLocator;
  }

  public void setShouldCreateServiceLocator(boolean v) {
    shouldCreateServiceLocator = v;
  }
  
  /**
   * true if you want a multicast service Locator
   * false if you want a unicast service Locator
   */
  public void setMulticastLocator(boolean v) {
    this.multicastLocator = v;
  }
  
  /**
   * Initialise the service locator for this host
   */
  public synchronized void initializeServiceLocator() {
    if (! shouldCreateServiceLocator) return; // don't bother
    if (locatorChecked) return; // already done for this VM
    getOrCreateServiceLocator();
    locatorChecked = true;
  }


  
  
  //
  // -- PRIVATE METHODS -----------------------------------------------
  // 
  
  private static String createTempDirectory(String host) {
    try {
      File fTmp = File.createTempFile("proactive-","-"+host);
      String tmpDirPath = fTmp.getAbsolutePath();
      System.out.println(">> TEMP directory = "+tmpDirPath);
      return tmpDirPath;
    } catch(Exception e) {
      System.err.println("Cannot create the TEMP directory : "+e.toString());
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  
  /**
   * Try to get the Service Locator on the local host (unicast search)
   * Create it if it doesn't exist 
   */
  private void getOrCreateServiceLocator() {    
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new java.rmi.RMISecurityManager());
    }    
    if (multicastLocator) {
      // For multicast

      LookupDiscovery discover = null;
      try {
	      System.out.println(">> Start LookupDiscovery ...");
	      discover = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
	      System.out.println(">> Stop LookupDiscovery ...");
      } catch(Exception e) {
	      System.err.println(e.toString());
	      e.printStackTrace();
	      System.exit(1);
      }
      discover.addDiscoveryListener(this);

      // stay around long enough to receive replies
      try {
	      System.out.println(">> Start waiting Thread ...");
	      Thread.currentThread().sleep(MAX_WAIT);
	
	      if (this.registrar == null) {
	        createServiceLocator();
	      }
	      System.out.println(">> Stop waiting Thread ...");
      } catch(java.lang.InterruptedException e) {
	      // do nothing
      }
      
    } else {
      // For unicast on `host`
      System.out.println("Lookup : jini://"+host);
      try {
	      lookup = new LookupLocator("jini://"+host);
	      System.out.println("Lookup.getRegistrar() on "+host);
	      this.registrar = lookup.getRegistrar();
      } catch(java.net.MalformedURLException e) {
	      System.err.println("Lookup failed: " + e.getMessage());
	      e.printStackTrace();
      } catch (java.io.IOException e) {
	      System.err.println("Registrar search failed: " + e.getMessage());
	      if (MAX_RETRY-- > 0) {
	        createServiceLocator();
	        getOrCreateServiceLocator();
	      } else {
	        System.out.println("\nCannot run a ServiceLocator : Have you launched the rmid deamon on "+host);
	        System.exit(2);
	      }
      } catch (java.lang.ClassNotFoundException e) {
	      System.err.println("Registrar search failed: " + e.toString());
	      e.printStackTrace();
      }
      System.out.println("Registrar found on "+host);

      // Just for test
      displayServices();
    }  
  }

  /**
   * Create a new Service Locator on the local host
   */
  private static void createServiceLocator() {
    System.out.println("No ServiceLocator founded ...  we lauch a ServiceLocator on "+host);
    String reggieTmpDir = tmpDir + System.getProperty("file.separator") + "reggie_log";
    delDirectory(new java.io.File(tmpDir));
    java.io.File directory = new java.io.File(tmpDir);
    directory.mkdirs();
 
    //System.out.println("We use the ClassServer : "+httpserver);
    System.out.println("We don't use a ClassServer for the service Locator (we use the CLASSPATH)");

    //String[] args = { httpserver , policy , reggieTmpDir,};
    String[] args = { "" , policy , reggieTmpDir,};
    ServiceStarter.create(args , starterClass, implClass, resourcename);
  }
  
    /**
     * Display all services on this registrar
     *@param registrar The registrar to contact
     */
    public void displayServices() {
      try {
	      // the code takes separate routes from here for client or service
	      System.out.println(">> >> found a service locator (registrar) : "+this.registrar);
	      System.out.println(">> >> >> ServiceID : "+this.registrar.getServiceID());
	
	      System.out.println(">> >> >> Groups : ");
	      String[] groups = this.registrar.getGroups();
	      for(int i =0 ; i < groups.length ; i++) {
	        System.out.println(">> >> >> >> "+i+") "+groups[i]);  
	      }
	      System.out.println(">> >> >> Locator : "+this.registrar.getLocator());

	      ServiceMatches matches  = null;
	      Class [] classes = new Class[] {JiniNode.class};
	      ServiceTemplate template = new ServiceTemplate(null,classes ,null);
	
	      matches =  this.registrar.lookup(template,Integer.MAX_VALUE);
	      System.out.println(">> >> >> "+matches.items.length+" required ");
	      System.out.println(">> >> >> "+matches.totalMatches+" founded ");
	      for(int i=0 ; i < matches.items.length ; i++) {
	        System.out.println(">> >> >> >> Object ("+i+") found : ");
	        System.out.println(">> >> >> >> >>        ID : "+matches.items[i].serviceID);
	        System.out.println(">> >> >> >> >>   Service : "+matches.items[i].service);
	        System.out.println(">> >> >> >> >> Attributs :");
	        for(int j=0; j < matches.items[i].attributeSets.length ; j++) {
	          System.out.println(">> >> >> >> >> >> Attr : "+matches.items[i].attributeSets[j]);
	        }
	        System.out.println("--------------------------------------------------------------------------------------");
	      }
      } catch(java.rmi.RemoteException e) {
	      System.err.println(e.toString());
	      e.printStackTrace();
      }
    }


    /**
     * Delete recursively all files and directory
     *@param dir The directory to clean
     */
    protected static void delDirectory(java.io.File dir) {
      java.io.File[] files = dir.listFiles();    
      if (files != null) {
	      for(int i=0 ; i < files.length ; i++) {
	        delDirectory(files[i]);
	      }
      }
      System.out.println("deleting "+dir.getPath()+" ...");
      dir.delete();
      if (dir.exists()) {
	      System.out.println("We cannot delete this file : "+dir.getPath());
	      System.out.println("... You should delete it before running a new ServiceLocator ...");
      }
    }
    

    /**
     * for multicast discover
     */
    public void discovered(DiscoveryEvent evt) {
      System.out.println(">> Start discover ...");
    
      ServiceRegistrar[] registrars = evt.getRegistrars();
    
      System.out.println(">> > "+registrars.length+" registrars : ");
      for (int n = 0; n < registrars.length; n++) {
	      this.registrar = registrars[n];
	      displayServices(); // just for test
      }
      System.out.println(">> Stop  discover...");
    }


    /**
     * for multicast discover
     */
    public void discarded(DiscoveryEvent evt) {
      System.out.println(">> discarded ...");
    }

  }
