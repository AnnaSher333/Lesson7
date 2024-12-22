package Ex3;

import java.util.Random;

public class Plagin1 implements PlayableRockPaperScissors{
    private final Random random = new Random();

    @Override
    public RockPaperScissorsEnum play() {
        return RockPaperScissorsEnum.values()[random.nextInt(RockPaperScissorsEnum.values().length)];
    }
}


