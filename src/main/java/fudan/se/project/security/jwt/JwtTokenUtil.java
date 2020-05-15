package fudan.se.project.security.jwt;

import fudan.se.project.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil implements Serializable {


    private static final long serialVersionUID = -3190497073434735136L;
    private JwtConfigProperties jwtConfigProperties;

    public JwtTokenUtil(JwtConfigProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().addClaims(claims)
                .setSubject(user.getUserId()+"")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfigProperties.getValidity()))
                .signWith(SignatureAlgorithm.HS512, jwtConfigProperties.getSecret()).compact();
    }

    public String getUserIdFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String userId = getUserIdFromToken(jwtToken);
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private Date getExpirationDateFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String jwtToken) {
        return Jwts.parser().setSigningKey(jwtConfigProperties.getSecret()).parseClaimsJws(jwtToken).getBody();
    }


    private boolean isTokenExpired(String jwtToken) {
        final Date expiration = getExpirationDateFromToken(jwtToken);
        return expiration.before(new Date());
    }


}
