package win.simple.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OsEntity {

    private int id;
    private String ostype;
    private String osvdi;
    private String defaultusername;
    private String defalutpassword;
    private String guestusername;
    private String guestpassword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getOsvdi() {
        return osvdi;
    }

    public void setOsvdi(String osvdi) {
        this.osvdi = osvdi;
    }

    public String getDefaultusername() {
        return defaultusername;
    }

    public void setDefaultusername(String defaultusername) {
        this.defaultusername = defaultusername;
    }

    public String getDefalutpassword() {
        return defalutpassword;
    }

    public void setDefalutpassword(String defalutpassword) {
        this.defalutpassword = defalutpassword;
    }

    public String getGuestusername() {
        return guestusername;
    }

    public void setGuestusername(String guestusername) {
        this.guestusername = guestusername;
    }

    public String getGuestpassword() {
        return guestpassword;
    }

    public void setGuestpassword(String guestpassword) {
        this.guestpassword = guestpassword;
    }

    @Override
    public String toString() {
        return "OsEntity{" +
                "id=" + id +
                ", ostype='" + ostype + '\'' +
                ", osvdi='" + osvdi + '\'' +
                ", defaultusername='" + defaultusername + '\'' +
                ", defalutpassword='" + defalutpassword + '\'' +
                ", guestusername='" + guestusername + '\'' +
                ", guestpassword='" + guestpassword + '\'' +
                '}';
    }
}
