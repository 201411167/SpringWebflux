package smu.capstone.heartsignal.service.beatService;

import lombok.RequiredArgsConstructor;
import org.nd4j.autodiff.samediff.ops.SDOps;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.stereotype.Service;
import smu.capstone.heartsignal.domain.user.BeatInfo;
import smu.capstone.heartsignal.domain.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeatService extends SDOps {

    private final UserRepository userRepository;

    public Double getRmssdFromBeats(List<BeatInfo> beats) {
        List<Double> beatDiff = new ArrayList<>();
        for (int i = 0; i < beats.size() - 1; i++) {
            Double diff = beats.get(i).getBeat() - beats.get(i + 1).getBeat();
            beatDiff.add(Math.pow(diff, 2));
        }

        INDArray diffArray = Nd4j.create(beatDiff);
        Number diffMean = diffArray.meanNumber();
        return Math.sqrt((Double) diffMean);
    }
}
