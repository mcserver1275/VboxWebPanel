package win.simple.entity;

public class VmInfoEntity {

    private int cpu;
    private int memory;
    private int cpuperformance;
    private String UUID;
    private String VMState;
    private String VMStateChangeTime;
    private String name;
    private String ostype;
    private String base64Snapshot;
    private String payment;
    private long time;
    private long createtime;
    private int currcreateprogress = -1;
    private String examplename;

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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getVMState() {
        return VMState;
    }

    public void setVMState(String VMState) {
        this.VMState = VMState;
    }

    public String getVMStateChangeTime() {
        return VMStateChangeTime;
    }

    public void setVMStateChangeTime(String VMStateChangeTime) {
        this.VMStateChangeTime = VMStateChangeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getBase64Snapshot() {
        return base64Snapshot;
    }

    public void setBase64Snapshot(String base64Snapshot) {
        this.base64Snapshot = base64Snapshot;
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

    public int getCurrcreateprogress() {
        return currcreateprogress;
    }

    public void setCurrcreateprogress(int currcreateprogress) {
        this.currcreateprogress = currcreateprogress;
    }

    public String getExamplename() {
        return examplename;
    }

    public void setExamplename(String examplename) {
        this.examplename = examplename;
    }
}
