package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.unit.MockHttpRequestFixtures.createTokenMockRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest extends AuthenticationInterceptorTestSupport {
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private TokenAuthenticationInterceptor interceptor;

    @Test
    void preHandle() {
        // given
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(FakePasswordCheckableUser.create(EMAIL, PASSWORD));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        HttpServletRequest request = createTokenMockRequest(EMAIL, PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when then
        assertDoesNotThrow(() -> interceptor.preHandle(request, response, null));
    }
}