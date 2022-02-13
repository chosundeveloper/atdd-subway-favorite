package nextstep.auth.authentication.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter authenticationConverter;

    protected AuthenticationInterceptor(final CustomUserDetailsService userDetailsService, final AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthentication(request, response, authentication);
        return false;
    }

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private void checkAuthentication(final UserDetails userDetails, final AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}