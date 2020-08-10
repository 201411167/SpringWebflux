package smu.capstone.heartsignal.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import smu.capstone.heartsignal.session.SessionUser;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final SessionUser sessionUser;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();
        final String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();

        Mono<OAuth2User> oAuth2User = delegate.loadUser(userRequest);

        oAuth2User.subscribe(u->{
            Map<String, Object> attributes = u.getAttributes();
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String picture = (String) attributes.get("picture");

            log.info("Logged in User : " + u.toString());
            sessionUser.setEmail(email);
            sessionUser.setName(name);
            sessionUser.setPicture(picture);
        });

        return oAuth2User;
    }
}
