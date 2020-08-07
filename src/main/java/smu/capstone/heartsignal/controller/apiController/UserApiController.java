package smu.capstone.heartsignal.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import smu.capstone.heartsignal.domain.user.BeatInfo;
import smu.capstone.heartsignal.domain.user.User;
import smu.capstone.heartsignal.domain.user.UserRepository;
import smu.capstone.heartsignal.eventHandler.beat.BeatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;

    @GetMapping
    public Flux<User> findAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/save")
    public Mono<User> saveUser(@RequestBody Map<String, Object> req) {
        String email = (String) req.get("email");
        String name = (String) req.get("name");
        return userRepository.save(User.builder().email(email).name(name).build());
    }

    @GetMapping("/{email}")
    public Mono<User> findUserByEmail(@PathVariable String email) {
        return userRepository.findById(email);
    }

    @PostMapping("/{email}/send/beat")
    public boolean sendBeatInfo(@PathVariable String email, @RequestBody Map<String, Object> req) {
        // time_str = "2016-03-04 11:30:43"
        String time_str = (String) req.get("time");
        String beat_str = (String) req.get("beat");
        String rrInterval_str = (String) req.get("rrInterval");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time_str, formatter);
        Double beat = Double.valueOf(beat_str);
        Double rrInterval = Double.valueOf(rrInterval_str);
        BeatInfo beatInfo = BeatInfo.of(dateTime, beat, rrInterval);

        userRepository.findById(email).subscribe(u1 -> {
            List<BeatInfo> beats = u1.getBeatList();
            beats.add(beatInfo);
            User newUser = User.builder().email(u1.getEmail()).name(u1.getName()).build();
            newUser.setBeatList(beats);
            userRepository.save(newUser).subscribe(u2 -> {
                publisher.publishEvent(BeatEvent.builder().email(u2.getEmail()).name(u2.getName()).beatInfoList(u2.getBeatList()).build());
            });
        });
        return true;
    }

    // TODO: 2020-07-30 delete this method
    @GetMapping("/deleteAll")
    public void deleteAll() {
        userRepository.deleteAll().subscribe();
    }

}