package onetoone.AdminDMWebsocket;

import jakarta.servlet.ServletContext;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        ServletContext servletContext = MyApplicationListener.getServletContext();
        if (servletContext == null) {
            throw new IllegalArgumentException("ServletContext is not available.");
        }

        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (applicationContext == null) {
            throw new IllegalStateException("No WebApplicationContext found.");
        }

        return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
    }
}
