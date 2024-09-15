package QCraft.QCraft.jwt;

import QCraft.QCraft.domain.RefreshToken;

import QCraft.QCraft.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${secret-key}")
    private String secretKey;
    @Value("${expiration}")
    private long expiration;
    @Value("${refresh-expiration}")
    private long refreshExpiration;


    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //accessToken 생성
    public String createAccessToken(String memberId) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())//signwith(key, signtureArgorithm) is deprecated
                .compact();
    }

    //refreshToken 생성
    public String createRefreshToken(String memberId, boolean isReissue) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        String refreshTokenString = Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .signWith(getSigningKey())
                .compact();

        if(!isReissue){//새로 발급
            RefreshToken refreshToken = new RefreshToken(memberId,refreshTokenString, expiryDate);
            refreshTokenRepository.save(refreshToken);
        }else {//재발급 RTR
            Optional<RefreshToken> refreshTokenOptional =  refreshTokenRepository.findByMemberId(memberId);
            if(refreshTokenOptional.isPresent()){
                    RefreshToken refreshToken = refreshTokenOptional.get();
                    refreshToken.setRefreshToken(refreshTokenString);
                    refreshTokenRepository.save(refreshToken);
            }else {
                throw new RuntimeException("이건 진짜 일어날 수 없는 오류임 ㄹㅇㅋㅋ");
            }
        }


        return refreshTokenString;

    }

    //jwt검증
    public String validateToken(String jwt){
        Claims claims = null;

        try{
            claims = Jwts.parser() //parserbuild() is deprecated
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return claims.getSubject();
    }
}
