package win.simple.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse rsp = (HttpServletResponse) servletResponse;
        rsp.addHeader("Access-Control-Allow-Origin", "*");
        rsp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        rsp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
