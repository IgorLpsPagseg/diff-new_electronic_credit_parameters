package com.poc.diff.table.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * @author ileonardo
 * @since 17/12/2021 10:10
 */
@Table(value = ElectronicCreditCacheData.TABLE_NAME)
public class ElectronicCreditCacheData implements Serializable {

    private static final long serialVersionUID = -9091129951038638718L;

    public static final String TABLE_NAME = "elec_credit_cache_v3";

    public static final String COD_TABLE_TYPE = "cod_table_type";

    public static final String COD_HASH_CK = "cod_hash";

    private static final String TABLE_REGISTERS = "list_table_register";

    private static final String FILE_ZIP = "obj_file_zip";

    @PrimaryKey(value = COD_TABLE_TYPE)
    private String tableType;

    @Column(value = COD_HASH_CK)
    private String hashCode;

    @Column(value = TABLE_REGISTERS)
    private List<String> registers;

    @Column(value = FILE_ZIP)
    private byte[] registersGZipped;

    public ElectronicCreditCacheData () {
        super();
    }

    public ElectronicCreditCacheData (String tableType, String hashCode, List<String> registers, byte[] registersGZipped) {
        this.tableType = tableType;
        this.hashCode = hashCode;
        this.registers = registers;
        this.registersGZipped = registersGZipped;
    }

    public String getHashCode () {
        return hashCode;
    }

    public void setHashCode (String hashCode) {
        this.hashCode = hashCode;
    }

    public List<String> getRegisters () {
        return registers;
    }

    public void setRegisters (List<String> registers) {
        this.registers = registers;
    }

    public String getTableType () {
        return tableType;
    }

    public void setTableType (String tableType) {
        this.tableType = tableType;
    }

    public byte[] getRegistersGZipped () {
        return registersGZipped;
    }

    public void setRegistersGZipped (byte[] registersGZipped) {
        this.registersGZipped = registersGZipped;
    }

    public boolean hasCompressedParameters () {
        return registersGZipped != null && registersGZipped.length > 0;
    }
}
