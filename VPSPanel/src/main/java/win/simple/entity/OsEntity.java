package win.simple.entity;

public class OsEntity {

    private int id;
    private String ostype;
    private String osvdi;

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

    @Override
    public String toString() {
        return "OsEntity{" +
                "id=" + id +
                ", ostype='" + ostype + '\'' +
                ", osvdi='" + osvdi + '\'' +
                '}';
    }
}
