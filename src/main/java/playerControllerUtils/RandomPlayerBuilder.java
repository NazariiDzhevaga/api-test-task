package playerControllerUtils;

import enums.GenderEnum;
import enums.RoleEnum;
import model.request.CreatePlayerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomPlayerBuilder {
    private final Random random = new Random();
    private final CreatePlayerRequest request = new CreatePlayerRequest();
    public static final Logger log = LoggerFactory.getLogger(RandomPlayerBuilder.class);

    public RandomPlayerBuilder withRandomValidData() {
        request.setAge(getRandomValidAge());
        request.setRole(getRandomRole());
        request.setGender(getRandomGender());
        request.setLogin(getRandomLogin());
        request.setPassword(getRandomPassword());
        request.setScreenName(getRandomScreenName());
        request.setEditor(RoleEnum.SUPERVISOR);
        log.info("Creating a player with random data: {}, thread: {}", request, Thread.currentThread().getName());
        return this;
    }

    public List<CreatePlayerRequest> createNumberOfUsers(int numberOfPlayer) {
        return IntStream.range(0, numberOfPlayer)
                .mapToObj(i -> new RandomPlayerBuilder().withRandomValidData()
                        .build())
                .collect(Collectors.toList());
    }

    public CreatePlayerRequest build() {
        return request;
    }

    private RoleEnum getRandomRole() {
        RoleEnum[] roles = {RoleEnum.ADMIN, RoleEnum.USER};
        return roles[random.nextInt(roles.length)];
    }

    private int getRandomValidAge() {
        return 16 + new Random().nextInt(45);
    }

    private GenderEnum getRandomGender() {
        GenderEnum[] genders = {GenderEnum.MALE, GenderEnum.FEMALE};
        return genders[random.nextInt(genders.length)];
    }

    private String getRandomLogin() {
        return "login" + randomSuffix() + random.nextInt(100);
    }

    private String getRandomPassword() {
        return "pass" + randomSuffix() + random.nextInt(100);
    }

    private String getRandomScreenName() {
        return "screen_name_" + randomSuffix() + random.nextInt(100);
    }

    private String randomSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
