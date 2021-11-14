package ch.so.agi.ilicache;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>  {

    //@Value("${tomcat.file.base}")  // C:\\some\\parent\\child
    //String tomcatBaseDir;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        TomcatContextCustomizer tomcatContextCustomizer = new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                String childFolder = "clone";
                context.setDocBase("/Users/stefan/tmp/ilicache/");                
                Wrapper defServlet = (Wrapper) context.findChild("default");
                defServlet.addInitParameter("listings", "true");
                defServlet.addInitParameter("sortListings", "true");
                defServlet.addInitParameter("sortDirectoriesFirst", "true");
                defServlet.addInitParameter("readOnly", "true");
                defServlet.addInitParameter("contextXsltFile", "/clone/listing.xsl");
                defServlet.addMapping("/"+childFolder+"/*");                
            }
        };
        factory.addContextCustomizers(tomcatContextCustomizer);        
    }
}
