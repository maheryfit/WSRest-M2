package itu.m2.ws.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import itu.m2.ws.details.UtilisateurDetails;
import itu.m2.ws.enums.Role;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.repositories.UtilisateurRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static itu.m2.ws.details.UtilisateurDetails.mapRolesToAuthorities;

@Service
public class UtilisateurService implements UserDetailsService {

    @Value("${key-hash}")
    private String KEY;

    @Value("${minutes-expired}")
    private int MINUTES_EXPIRATION;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMotDePasseHash(bCryptPasswordEncoder.encode(utilisateur.getMotDePasseHash()));
        return utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> updateUtilisateur(Long id, Utilisateur utilisateurDetails) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateur.setEmail(utilisateurDetails.getEmail());
            if (utilisateurDetails.getMotDePasseHash() != null && !utilisateurDetails.getMotDePasseHash().isEmpty()) {
                utilisateur.setMotDePasseHash(bCryptPasswordEncoder.encode(utilisateurDetails.getMotDePasseHash()));
            }
            utilisateur.setRole(utilisateurDetails.getRole());
            utilisateur.setActif(utilisateurDetails.isActif());
            return utilisateurRepository.save(utilisateur);
        });
    }

    public void updateUtilisateur(Utilisateur utilisateur, Role role) {
        utilisateur.setRole(role);
        utilisateur.setMotDePasseHash(bCryptPasswordEncoder.encode(utilisateur.getMotDePasseHash()));
        utilisateurRepository.save(utilisateur);
    }

    public boolean deleteUtilisateur(Long id) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateurRepository.delete(utilisateur);
            return true;
        }).orElse(false);
    }

    /**
     * Authentifie un utilisateur par email et mot de passe.
     * Amélioré avec des exceptions spécifiques et vérification du statut actif.
     */
    public Utilisateur logIn(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(email);

        if (utilisateur == null) {
            throw new BadCredentialsException("Email introuvable : " + email);
        }

        if (!utilisateur.isActif()) {
            throw new BadCredentialsException("Email introuvable : " + email);
        }

        if (!bCryptPasswordEncoder.matches(motDePasse, utilisateur.getMotDePasseHash())) {
            throw new BadCredentialsException("Email introuvable : " + email);
        }

        return utilisateur;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur user = utilisateurRepository.findUtilisateurByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email : " + email + " not found");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(user.getRole());
        return new UtilisateurDetails(user, mapRolesToAuthorities(roles));
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token,
            final UserDetails userDetails) {
        Claims claims = extractAllClaims(token);

        Object roleClaim = claims.get("role");
        String roleStrRaw = roleClaim != null ? roleClaim.toString() : "";
        System.out.print(roleStrRaw);
        final Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(roleStrRaw.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createToken(UserDetails userDetails) {
        String role = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .iterator()
                .next();
        return Jwts.builder()
                .claim("role", role)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(MINUTES_EXPIRATION)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(KEY.getBytes());
    }

    public String generateToken(String email) {
        return createToken(loadUserByUsername(email));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

}
