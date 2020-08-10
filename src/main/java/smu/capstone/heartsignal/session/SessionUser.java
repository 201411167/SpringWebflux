package smu.capstone.heartsignal.session;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class SessionUser {
    private String email;
    private String name;
    private String picture;

    @Builder
    public SessionUser(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}

