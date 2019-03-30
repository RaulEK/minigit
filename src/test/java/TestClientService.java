import client.ClientService;
import models.Repository;

import java.nio.file.Paths;

public class TestClientService {
    public static void main(String[] args) throws Exception {

        String workingDir = Paths.get("src", "test", "resources", "test").toString();

        Repository repository = new Repository("minigit");

        ClientService.saveRepository(repository);

        ClientService.commitRepository("123123","minu esimene commit", workingDir);

        ClientService.pushRepository(repository);

    }
}
