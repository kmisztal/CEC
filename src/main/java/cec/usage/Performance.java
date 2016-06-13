package cec.usage;

import java.lang.*;


import org.hyperic.sigar.*;

/**
 * Created by Mateusz Reczkowski on 12.06.2016.
 */
public class Performance {

    private static Sigar sigar = new Sigar();

    public static void getInformationsAboutMemory() {
        System.out.println("**************************************");
        System.out.println("*** Information about the Memory: ***");
        System.out.println("**************************************\n");

        Mem mem = null;
        try {
            mem = sigar.getMem();
        } catch (SigarException se) {
            se.printStackTrace();
        }

        System.out.println("Actual total free system memory: "
                + mem.getActualFree() / 1024 / 1024+ " MB");
        System.out.println("Actual total used system memory: "
                + mem.getActualUsed() / 1024 / 1024 + " MB");
        System.out.println("Total free system memory ......: " + mem.getFree()
                / 1024 / 1024+ " MB");
        System.out.println("System Random Access Memory....: " + mem.getRam()
                + " MB");
        System.out.println("Total system memory............: " + mem.getTotal()
                / 1024 / 1024+ " MB");
        System.out.println("Total used system memory.......: " + mem.getUsed()
                / 1024 / 1024+ " MB");

        System.out.println("\n**************************************\n");


    }

    public static void getInformationAboutCPU(){
        System.out.println("**************************************");
        System.out.println("*** Information about the CPU: ***");
        System.out.println("**************************************\n");

        CpuPerc cpu = null;
        try {
            cpu = sigar.getCpuPerc();
        } catch (SigarException se) {
            se.printStackTrace();
        }

        System.out.println("Total system cpu idle time: "
                + cpu.getIdle() + " %");
        System.out.println("Total system cpu time servicing interrupts: "
                + cpu.getIrq() + " %");
        System.out.println("Total system cpu nice time: "
                + cpu.getNice() + " %");
        System.out.println("Total system cpu time servicing soft irqs: "
                + cpu.getSoftIrq() + " %");
        System.out.println("Total system cpu involuntary wait time: "
                + cpu.getStolen() + " %");
        System.out.println("Total system cpu kernel time: "
                + cpu.getSys() + " %");
        System.out.println("Total system cpu user time: "
                + cpu.getUser() + " %");
        System.out.println("Total system cpu io wait time: "
                + cpu.getWait() + " %");



    }

    public static void getInformationsAboutDisk() {
        System.out.println("**************************************");
        System.out.println("*** Information about the Disk: ***");
        System.out.println("**************************************\n");

        DiskUsage usage = null;
        try {
            usage = sigar.getDiskUsage("C:");
        } catch (SigarException se) {
            se.printStackTrace();
        }

        System.out.println("Number of physical disk MB read: "
                + usage.getReadBytes() / 1024 / 1024+ " MB");
        System.out.println("Number of physical disk reads: "
                + usage.getReads());
        System.out.println("Number of physical disk MB written: "
                + usage.getWriteBytes() / 1024 / 1024+ " MB");
        System.out.println("Number of physical disk writes: "
                + usage.getWrites());


        System.out.println("\n**************************************\n");


    }

    public static void main(String[] args) throws Exception{

        getInformationsAboutMemory();
        System.out.println();
        getInformationAboutCPU();
        System.out.println();
        getInformationsAboutDisk();
        System.out.println();

    }
}
