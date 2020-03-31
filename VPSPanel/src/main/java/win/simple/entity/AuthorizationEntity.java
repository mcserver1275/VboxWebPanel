package win.simple.entity;

public class AuthorizationEntity {

    private int id;
    private String name;
    private String identity;
    private String publicip;
    private String serviceregion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPublicip() {
        return publicip;
    }

    public void setPublicip(String publicip) {
        this.publicip = publicip;
    }

    public String getServiceregion() {
        return serviceregion;
    }

    public void setServiceregion(String serviceregion) {
        this.serviceregion = serviceregion;
    }
}
