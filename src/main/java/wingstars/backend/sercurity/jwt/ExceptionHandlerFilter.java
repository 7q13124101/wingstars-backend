package wingstars.backend.sercurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@AllArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(ExceptionHandlerFilter.class);
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("Spring Security Filter Chain Exception: Forward to ExceptionHandlerController");
            resolver.resolveException(request, response, null, e);
        }
    }

}
