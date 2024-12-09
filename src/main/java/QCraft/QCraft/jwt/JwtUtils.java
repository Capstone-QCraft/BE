package QCraft.QCraft.jwt;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final MemberRepository memberRepository;

    @Value("${secret-key}")
    private String secretKey;
    @Value("${expiration}")
    private long expiration;

    @Value("${token.expiration.access}")
    private long accessExpiration;
    @Value("${token.expiration.refresh}")
    private long refreshExpiration;


    //accessToken 생성
    public String createAccessToken(String memberId, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("roles", roles);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessExpiration);
        Key key = generateKey();

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .compact();

    }

    //refreshToken 생성
    public String createRefreshToken(String memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);
        Key key = generateKey();

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 재발급
    public String createRefreshToken(String memberId, Date expiration) {
        Key key = generateKey();

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 확인 및 재발급
    public List<String> refreshToken(String refreshToken) {
        try {

            if (!validateToken(refreshToken)) {
                return null;
            }

            Key key = generateKey();
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(refreshToken);
            String userId = claims.getBody().getSubject();
            Date expiration = claims.getBody().getExpiration();

            Optional<Member> memberOptional = memberRepository.findById(userId);
            if (memberOptional.isEmpty()) {
                return null;
            }
            Member member = memberOptional.get();

            if (refreshToken.equals(member.getRefreshToken())) {
                String newRefreshToken = createRefreshToken(member.getId(), expiration);
                member.setRefreshToken(newRefreshToken);
                memberRepository.save(member);

                String newAccessToken = createAccessToken(member.getId(), Collections.singletonList(member.getRole()));
                ArrayList<String> tokens = new ArrayList<>();
                tokens.add(newAccessToken);
                tokens.add(newRefreshToken);

                return tokens;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //jwt검증
    public boolean validateToken(String jwt) {
        Key key = generateKey();

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //토큰에서 id가져오기
    public String getIdFromToken(String jwt) {
        Key key = generateKey();

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
    }

    //key 생성
    private Key generateKey() {
        if (secretKey.length() < 32) {
            throw new RuntimeException("Secret key must be at least 32 characters");
        }
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}