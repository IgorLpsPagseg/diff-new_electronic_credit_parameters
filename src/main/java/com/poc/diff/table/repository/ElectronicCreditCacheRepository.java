package com.poc.diff.table.repository;

import com.poc.diff.table.entity.ElectronicCreditCacheData;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * @author ileonardo
 * @since 17/12/2021 10:12
 */
public interface ElectronicCreditCacheRepository extends CassandraRepository<ElectronicCreditCacheData, Long> {

}
