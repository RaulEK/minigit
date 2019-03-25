import client.ClientService;
import models.Commit;
import models.Repository;

public class TestClientService {
    public static void main(String[] args) throws Exception {
        Repository repository = new Repository("Minu uus repositoorium");

        Commit commit = new Commit("123123", "Jou test message");

        repository.addCommit(commit);

        ClientService.saveRepository(repository);

        System.out.println(ClientService.readRepository().getName().equals("Minu uus repositoorium"));
    }
}
