package com.poc.diff.table.repository;


public enum ElectronicCreditTableVersionType  {

    STABLE,

    BETA,

    LATEST;

    public boolean isStable () {
        return this == STABLE;
    }

    public boolean isBeta () {
        return this == BETA;
    }
}