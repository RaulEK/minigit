package client;


public class InitCommand extends Command{
    @Override
    public void process(String[] input) throws Exception {
        setInput(input);
        if (getInput().length == 2) {
            ClientService.initRepository(getInput()[1]);
        } else {
            System.out.println("Invalid parameters. Try init name");
        }
    }
}
