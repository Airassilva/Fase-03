package dev.aira.agendamento.user.service;

import dev.aira.agendamento.exceptions.BadRequestBusinessException;
import dev.aira.agendamento.user.dtos.LoginResponse;
import dev.aira.agendamento.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncodingException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public LoginResponse generateToken(User user) {
        try{
            var now = Instant.now();
            var expiresIn = 300L;
            String token = jwtEncoder.encode(
                    JwtEncoderParameters.from(
                            JwtClaimsSet.builder()
                                    .issuer("Agendamento-service")
                                    .subject(user.getId().toString())
                                    .claim("roles",
                                            user.getAuthorities()
                                                    .stream()
                                                    .map(GrantedAuthority::getAuthority)
                                                    .toList())
                                    .issuedAt(now)
                                    .expiresAt(now.plusSeconds(expiresIn))
                                    .build()
                    )
            ).getTokenValue();
            return new LoginResponse(token, expiresIn);
        }catch(JwtEncodingException e){
            throw new BadRequestBusinessException("JWT not created");
        }
    }

    public String generateServiceToken(String serviceName) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Agendamento-service")
                .issuedAt(now)
                .expiresAt(now.plus(365, ChronoUnit.DAYS))
                .subject(serviceName)
                .claim("roles", List.of("ROLE_SERVICE"))
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }
}
