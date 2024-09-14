package QCraft.QCraft.jwt;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String memberId = jwtUtils.validateToken(token);
            if (memberId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isPresent()) {
                String role = member.get().getRole();
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            } else {
                throw new IllegalArgumentException("아이디를 찾을 수 없음");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }


    //토큰값 가져오기
    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        boolean hasAuthorization = StringUtils.hasText("Authorization");
        boolean isBearer = authorization != null && authorization.startsWith("Bearer ");

        if (!hasAuthorization) {
            log.error("Authorization header is missing - {}", authorization);
            return null;
        }

        if (!isBearer) {
            log.error("This is not Bearer - {}", authorization);
            return null;
        }

        String token = authorization.substring(7);
        return token;
    }
}
