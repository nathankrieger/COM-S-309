package onetoone.AdminDMWebsocket;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ServletContextListener {
    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        servletContext = null;
    }
}
