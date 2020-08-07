package smu.capstone.heartsignal.eventHandler.beat;

import lombok.Builder;
import lombok.Getter;
import smu.capstone.heartsignal.domain.user.BeatInfo;

import java.util.List;

@Getter
public class BeatEvent {

    private String email;
    private String name;
    private List<BeatInfo> beatInfoList;
    private Double RMSSD;

    @Builder
    public BeatEvent(String email, String name, List<BeatInfo> beatInfoList) {
        this.email = email;
        this.name = name;
        this.beatInfoList = beatInfoList;
        this.RMSSD = 0.0;
    }
}
