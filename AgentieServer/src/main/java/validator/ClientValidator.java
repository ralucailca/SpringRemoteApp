package validator;


import model.Client;
import services.AgentieException;

public class ClientValidator implements IValidator<Client> {
    @Override
    public void validate(Client client) {
        String err="";
        //if(client.getId()==null || client.getId()<1)
          //  err+="Id invalid!\n";
        if(client.getNume()==null || client.getNume()=="")
            err+="Nume invalid!\n";
        if(client.getTelefon()==null || client.getTelefon()=="")
            err+="Telefon invalid!\n";
        if(err!="")
            throw new AgentieException(err);
    }
}
