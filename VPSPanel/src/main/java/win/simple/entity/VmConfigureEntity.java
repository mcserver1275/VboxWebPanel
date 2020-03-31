package win.simple.entity;

public class VmConfigureEntity {

    private int id;
    private int cpu;
    private int memory;
    private int cpuperformance;
    private String vmuuid;
    private int userid;
    private String state;
    private String privateip;
    private String payment;
    private String ostype;
    private long time;
    private long createtime;
    private String name;
    private String examplename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCpuperformance() {
        return cpuperformance;
    }

    public void setCpuperformance(int cpuperformance) {
        this.cpuperformance = cpuperformance;
    }

    public String getVmuuid() {
        return vmuuid;
    }

    public void setVmuuid(String vmuuid) {
        this.vmuuid = vmuuid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrivateip() {
        return privateip;
    }

    public void setPrivateip(String privateip) {
        this.privateip = privateip;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamplename() {
        return examplename;
    }

    public void setExamplename(String examplename) {
        this.examplename = examplename;
    }
}
