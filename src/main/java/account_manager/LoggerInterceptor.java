package account_manager;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class LoggerInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = Logger.getLogger(LoggerInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info(String.format("Start proceeding %s request to URL %s", request.getMethod(), request.getRequestURL()));
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        log.info(String.format("%s request to URL %s completed with response status %d, time taken: %d millis",
                request.getMethod(), request.getRequestURL(), response.getStatus(), endTime - startTime));
    }
}
