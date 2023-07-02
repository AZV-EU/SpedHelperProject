package com.spedhelper.spedhelper.entities;

public enum ApiKeyBillingType {
    DEBUG,
    STANDARD,
    ENTERPRISE;

    public int getMaxRequests() {
        switch (this) {
            case DEBUG:
                return -1;
            case STANDARD:
                return 10_000;
            case ENTERPRISE:
                return 100_000_000;
            default:
                return -1;
        }
    }
}
