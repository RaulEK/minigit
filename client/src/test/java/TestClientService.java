
import client.service.ClientUtils;
import models.Repository;

import java.nio.file.Paths;

public class TestClientService {
    public static void main(String[] args) throws Exception {

        Repository repository = new Repository("minigit");

        ClientUtils.saveRepository(repository);
    }
}
