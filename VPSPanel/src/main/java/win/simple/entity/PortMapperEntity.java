package win.simple.entity;

public class PortMapperEntity {

    private String name;
    private String agreement;
    private String intranetPort;
    private String externalPort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getIntranetPort() {
        return intranetPort;
    }

    public void setIntranetPort(String intranetPort) {
        this.intranetPort = intranetPort;
    }

    public String getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(String externalPort) {
        this.externalPort = externalPort;
    }
}
