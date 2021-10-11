package com.poc.diff.table.vo;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 12/05/2021 15:34
 */
@ToString
@Getter
@NoArgsConstructor
public class DiffReporVO {

    /**
     * Diferença de valores, valores que estão na Latest, mas não estão na Stable
     */
    @JsonPropertyDescription("Diferença de valores, valores que estão na Latest, mas não estão na Stable")
    public String valoresEmLatestNaoEncontradosEmStable;

    /**
     * Diferença de valores, valores que estão na Stable, mas não estão na Latest
     * provavel retirada de valor
     */
    @JsonPropertyDescription("Diferença de valores, valores que estão na Stable, mas não estão na Latest")
    public String valoresEmStableNaoEncontradosEmLatest;

    /**
     * Diferença de Concessionária, Concessionárias que estão na Latest, mas não estão na Stable
     */
    @JsonPropertyDescription("Diferença de concessionárias, concessionárias que estão na Latest, mas não estão na Stable")
    public String concessionariasEmLatestNaoEncontradasStable;



    /**
     * Diferença de valores, valores que estão na Stable, mas não estão na Latest(concessionarias que saíram)
     */
    @JsonPropertyDescription("Diferença de concessionárias, concessionárias que estão na Stable, mas não estão na Latest")
    public String concessionariasEmStableNaoEncontradasLatest;

    /**
     * Diferença de campos, novas Branchs que estão em Latest
     * */
    @JsonPropertyDescription("novas Branchs que estão em Latest, mas não estão na Latest")
    public String novasBranchsInLatest;


    /**
     * Diferença de campos, Branchs que não estão mais em Latest, foram retiradas
     * */
    @JsonPropertyDescription("Branchs que não estão mais em Latest, foram retiradas")
    public String branchesNotFoundInLatest;


    /**
     * Diferença de campos, valores que estão na Latest, mas que estão Stable(code, vfp, subAccountLength)
     * */
    @JsonPropertyDescription("Diferença de concessionárias, concessionárias que estão na Stable, mas não estão na Latest")
    public String valoresDivergentes;





    public DiffReporVO(String valoresEmLatestNaoEncontradosEmStable,
                       String valoresEmStableNaoEncontradosEmLatest,
                       String concessionariasEmLatestNaoEncontradasStable,
                       String concessionariasEmStableNaoEncontradasLatest,
                       String novasBranchsInLatest,
                       String branchesNotFoundInLatest,
                       String valoresDivergentes) {
        this.valoresEmLatestNaoEncontradosEmStable = valoresEmLatestNaoEncontradosEmStable;
        this.valoresEmStableNaoEncontradosEmLatest = valoresEmStableNaoEncontradosEmLatest;
        this.concessionariasEmLatestNaoEncontradasStable = concessionariasEmLatestNaoEncontradasStable;
        this.concessionariasEmStableNaoEncontradasLatest = concessionariasEmStableNaoEncontradasLatest;
        this.novasBranchsInLatest = novasBranchsInLatest;
        this.branchesNotFoundInLatest = branchesNotFoundInLatest;
        this.valoresDivergentes = valoresDivergentes;
    }
}
