package com.poc.diff.table.data;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author ileonardo
 * @since 16/12/2021 08:40
 */
@Table(ElectronicCreditParametersData.TABLE_NAME)
public class ElectronicCreditParametersData  implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "elec_credit_parameters_v2";

    public static final String COD_TABLE_TYPE = "cod_table_type";

    public static final String HASH_CK = "cod_hash";

    public static final String ORIGIN_CODE = "cod_establishment";

    private static final String VGP = "idt_gwcel_version";

    private static final String PARAMETERS = "json_parameters";

    @PrimaryKey(ElectronicCreditParametersData.COD_TABLE_TYPE)
    private String tableType;

    @Column(ElectronicCreditParametersData.HASH_CK)
    private String hash;

    @Column(ElectronicCreditParametersData.VGP)
    private String vgp;

    @Column(ElectronicCreditParametersData.ORIGIN_CODE)
    private String originCode;

    @Column(ElectronicCreditParametersData.PARAMETERS)
    private String parameters;

    public ElectronicCreditParametersData () {
    }

    public ElectronicCreditParametersData (String tableType, String vgp, String originCode, String parameters) {
        this(tableType, vgp, null, originCode, parameters);
    }

    public ElectronicCreditParametersData (String tableType, String vgp, String hash, String originCode,
                                           String parameters) {
        this.tableType = tableType;
        this.vgp = vgp;
        this.hash = hash;
        this.originCode = originCode;
        this.parameters = parameters;
    }

    public String getTableType () {
        return tableType;
    }

    public void setTableType (String tableType) {
        this.tableType = tableType;
    }

    public String getHash () {
        return hash;
    }

    public void setHash (String hash) {
        this.hash = hash;
    }

    public String getVgp () {
        return vgp;
    }

    public void setVgp (String vgp) {
        this.vgp = vgp;
    }

    public String getOriginCode () {
        return originCode;
    }

    public void setOriginCode (String originCode) {
        this.originCode = originCode;
    }

    public String getParameters () {
        return parameters;
    }

    public void setParameters (String parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElectronicCreditParametersData that = (ElectronicCreditParametersData) o;

        return Objects.equals(tableType, that.tableType) && Objects.equals(vgp, that.vgp)
                && Objects.equals(originCode, that.originCode)
                && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode () {
        return Objects.hash(tableType, vgp, parameters);
    }

}
