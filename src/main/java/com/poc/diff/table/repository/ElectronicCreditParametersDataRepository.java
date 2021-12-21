package com.poc.diff.table.repository;

import com.poc.diff.table.data.ElectronicCreditParametersData;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface ElectronicCreditParametersDataRepository extends CassandraRepository<ElectronicCreditParametersData, Long> {

    List<ElectronicCreditParametersData> findByTableType(String COD_TABLE_TYPE);

}
