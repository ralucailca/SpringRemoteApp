package validator;


import model.Rezervare;
import services.AgentieException;

public class RezervareValidator implements IValidator<Rezervare> {
    @Override
    public void validate(Rezervare rezervare) {
        if(rezervare.getBilete()<0)
            throw new AgentieException("Numar invalid de bilete!");
    }
}
