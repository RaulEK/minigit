import client.ClientService;
import client.ClientUtils;
import models.Repository;

import java.nio.file.Paths;

public class TestClientService {
    public static void main(String[] args) throws Exception {

        String workingDir = Paths.get("src", "test", "resources", "test").toString();

        Repository repository = new Repository("minigit");

        ClientUtils.saveRepository(repository);
    }
}
