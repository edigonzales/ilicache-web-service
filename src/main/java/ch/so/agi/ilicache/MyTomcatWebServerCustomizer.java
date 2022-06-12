package ch.so.agi.ilicache;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
//import org.apache.catalina.Context;
//import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import ch.so.agi.ilicache.config.UserConfig;

@Component
public class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Override
    public void customize(JettyServletWebServerFactory factory) {
        JettyServerCustomizer jettyServerCustomizer = new JettyServerCustomizer() {
            
            @Override
            public void customize(Server server) {
                Handler[] childHandlersByClass = server.getChildHandlersByClass(WebAppContext.class);
                final WebAppContext webAppContext = (WebAppContext) childHandlersByClass[0];
                ServletHandler handler = webAppContext.getServletHandler();
                
//                ServletHolder defServlet = ((WebAppContext)server.getHandlers()[0]).getServletHandler().getServlets()[0];
//                defServlet.setInitParameter("dirAllowed","true");
//                defServlet.setInitParameter("resourceBase","/Users/stefan/tmp/");
//                webAppContext.getServletHandler().addServletWithMapping(defServlet, "/foo");
//                webAppContext.getServletHandler().getServletMapping("/foo").setDefault(true);
        
//                ServletHolder holder = new ServletHolder();
//                holder.setName("default");
//                holder.setClassName("org.eclipse.jetty.servlet.DefaultServlet");
//                holder.setInitParameter("dirAllowed", "true");
//                holder.setInitOrder(1);
//                webAppContext.addServlet(holder, "/foo/*");
               
                System.setProperty("org.eclipse.jetty.LEVEL","DEBUG");

                ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

                String homePath = System.getProperty("user.home");

                ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
                holderHome.setInitParameter("resourceBase",homePath);
                holderHome.setInitParameter("dirAllowed","true");
                holderHome.setInitParameter("pathInfoOnly","true");
                context.addServlet(holderHome,"/home/*");

                
                
                ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
                holderPwd.setInitParameter("dirAllowed","true");
                holderPwd.setInitParameter("resourceBase","/Users/stefan/tmp/");
                context.addServlet(holderPwd, "/foo/*");

//                webAppContext.getServletHandler().addServletWithMapping(holder, "/aa/*");
//                webAppContext.getServletHandler().getServletMapping("/aa/*").setDefault(true);
            }
            
        };  
        factory.addServerCustomizers(jettyServerCustomizer);
    }
//public class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>  {
//    
//    @Autowired
//    UserConfig userConfig;
//    
//    @Value("${app.liveRepoCloneDirectoryName}")
//    private String liveRepoCloneDirectoryName;
//
//    /*
//     * Achtung: Low level Tomcat Konfiguration, damit das Directory-Listing
//     * verwendet werden kann. 
//     * Andere statische (Spring Boot) Ressourcen funktionieren immer
//     * noch (z.B. "src/main/resources/static").
//     */
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        TomcatContextCustomizer tomcatContextCustomizer = new TomcatContextCustomizer() {
//            @Override
//            public void customize(Context context) {
//                String childFolder = liveRepoCloneDirectoryName;
//                context.setDocBase(userConfig.getCloneDirectory());                
//                Wrapper defServlet = (Wrapper) context.findChild("default");
//                defServlet.addInitParameter("listings", "true");
//                defServlet.addInitParameter("sortListings", "true");
//                defServlet.addInitParameter("sortDirectoriesFirst", "true");
//                defServlet.addInitParameter("readOnly", "true");
//                defServlet.addInitParameter("contextXsltFile", "/listing.xsl");
//                defServlet.addMapping("/"+childFolder+"/*");                
//            }
//        };
//        factory.addContextCustomizers(tomcatContextCustomizer);        
//    }
}
