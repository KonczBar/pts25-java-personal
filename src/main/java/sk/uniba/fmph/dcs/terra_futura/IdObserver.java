package sk.uniba.fmph.dcs.terra_futura;

public class IdObserver implements Observer{
    private int Id;

    IdObserver(int Id){
        this.Id = Id;
    }

    @Override
    public void notify(String GameState) {
        System.out.println("Player " + Id + " " + GameState);
    }
}
