package win.simple.entity;

public class CacheInfoEntity {

    private String vmuuid;
    private String intranetip;
    private String vmname;
    private String vmstate;
    private long memory;
    private int cpu;

    public String getVmuuid() {
        return vmuuid;
    }

    public void setVmuuid(String vmuuid) {
        this.vmuuid = vmuuid;
    }

    public String getIntranetip() {
        return intranetip;
    }

    public void setIntranetip(String intranetip) {
        this.intranetip = intranetip;
    }

    public String getVmname() {
        return vmname;
    }

    public void setVmname(String vmname) {
        this.vmname = vmname;
    }

    public String getVmstate() {
        return vmstate;
    }

    public void setVmstate(String vmstate) {
        this.vmstate = vmstate;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }
}
