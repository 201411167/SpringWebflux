package smu.capstone.heartsignal.eventHandler.beat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import smu.capstone.heartsignal.domain.user.BeatInfo;
import smu.capstone.heartsignal.domain.user.User;
import smu.capstone.heartsignal.domain.user.UserRepository;
import smu.capstone.heartsignal.service.beatService.BeatService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BeatHandler {
    private final BeatService beatService;
    private final UserRepository userRepository;

    @EventListener
    @Async
    public void doEvent(BeatEvent event) {
        List<BeatInfo> beatInfoList = event.getBeatInfoList();
        int size = beatInfoList.size();
        List<BeatInfo> subBeatInfoList = beatInfoList.subList(size - 10, size - 1);
        Double rmssd = beatService.getRmssdFromBeats(subBeatInfoList);
        System.out.println(rmssd);
        // TODO: 2020-07-30 input rmssd into database

        userRepository.findById(event.getEmail()).subscribe(u->{
            User newUser = User.builder().email(u.getEmail()).name(u.getName()).build();
            newUser.setBeatList(beatInfoList);
            newUser.setRmssd(rmssd);
            userRepository.save(newUser).subscribe();
        });
    }
}
