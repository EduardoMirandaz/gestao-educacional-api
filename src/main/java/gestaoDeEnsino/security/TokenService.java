package gestaoDeEnsino.security;

import gestaoDeEnsino.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;
    @Value("${jwt.email.expiration}")
    private String emailExpiration;

    private static final String ROLES = "roles";

    public String getToken(UsuarioEntity usuarioEntity) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.valueOf(expiration));

        List<String> listaDeCargos = usuarioEntity.getRolesEntities().stream()
                .map(cargoEntity -> cargoEntity.getRoles())
                .toList();

        String token = Jwts.builder()
                .setIssuer("gestao-de-ensino-api")
                .claim(Claims.ID, usuarioEntity.getIdUsuario())
                .claim(ROLES, listaDeCargos)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return TokenAuthenticationFilter.BEARER + token;
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null) {
            return null;
        }

        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Integer idUsuario = body.get(Claims.ID, Integer.class);

        if (idUsuario != null){
            List<String> roles = body.get(ROLES, List.class);

            List<SimpleGrantedAuthority> rolesGrantedAuthority = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .toList();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, rolesGrantedAuthority);

            return usernamePasswordAuthenticationToken;
        }
        return null;
    }

    public String getTokenRecuperarSenha(UsuarioEntity usuarioEntity) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.valueOf(emailExpiration));

        List<String> listaDeCargos = usuarioEntity.getRolesEntities().stream()
                .map(cargoEntity -> cargoEntity.getRoles())
                .toList();

        String token = Jwts.builder()
                .setIssuer("gestao-de-ensino-api")
                .claim(Claims.ID, usuarioEntity.getIdUsuario())
                .claim(ROLES, listaDeCargos)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return TokenAuthenticationFilter.BEARER + token;
    }

}
