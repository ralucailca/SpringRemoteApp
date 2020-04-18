package validator;


import model.Excursie;
import services.AgentieException;

public class ExcursieValidator implements IValidator<Excursie> {
    @Override
    public void validate(Excursie excursie) {
        String err="";
        if(excursie.getPret()<0)
            err+="Pret invalid!\n";
        if(excursie.getOraPlecare()<0 || excursie.getOraPlecare()>24)
            err+="Ora de plecare invalida!\n";
        if(excursie.getLocuri()<0)
            err+="Numar locuri invalid! Nu mai exista destule locuri!\n";
        if(err.length()>0)
            throw new AgentieException(err);
    }
}
