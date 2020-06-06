package com.tenx.banking.core.api.resources;

import java.util.StringJoiner;

public class VendingMachineRequest {
    private String vendingMachineId;
    private int change;

    public String getVendingMachineId() {
        return vendingMachineId;
    }

    public int getChange() {
        return change;
    }
//
//    public VendingMachineRequest(String vendingMachineId, int change) {
//        this.vendingMachineId = vendingMachineId;
//        this.change = change;
//    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VendingMachineRequest.class.getSimpleName() + "[", "]")
                .add("vendingMachineId='" + vendingMachineId + "'")
                .add("change=" + change)
                .toString();
    }
}
