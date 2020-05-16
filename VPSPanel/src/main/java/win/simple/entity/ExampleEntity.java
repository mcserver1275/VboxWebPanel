package win.simple.entity;

public class ExampleEntity {

    private int id;
    private String name;
    private int configureid;
    private String cputype;
    private String cpuhz;
    private float price;
    private int cpus;
    private int cpuexecutioncap;
    private int memory;
    private int osid;
    private int defalutnatport;

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

    public int getConfigureid() {
        return configureid;
    }

    public void setConfigureid(int configureid) {
        this.configureid = configureid;
    }

    public String getCputype() {
        return cputype;
    }

    public void setCputype(String cputype) {
        this.cputype = cputype;
    }

    public String getCpuhz() {
        return cpuhz;
    }

    public void setCpuhz(String cpuhz) {
        this.cpuhz = cpuhz;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public int getCpuexecutioncap() {
        return cpuexecutioncap;
    }

    public void setCpuexecutioncap(int cpuexecutioncap) {
        this.cpuexecutioncap = cpuexecutioncap;
    }

    public int getDefalutnatport() {
        return defalutnatport;
    }

    public void setDefalutnatport(int defalutnatport) {
        this.defalutnatport = defalutnatport;
    }

    public int getOsid() {
        return osid;
    }

    public void setOsid(int osid) {
        this.osid = osid;
    }

    @Override
    public String toString() {
        return "ExampleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", configureid=" + configureid +
                ", cputype='" + cputype + '\'' +
                ", cpuhz='" + cpuhz + '\'' +
                ", price=" + price +
                ", cpus=" + cpus +
                ", cpuexecutioncap=" + cpuexecutioncap +
                ", memory=" + memory +
                ", osid=" + osid +
                ", defalutnatport=" + defalutnatport +
                '}';
    }
}
