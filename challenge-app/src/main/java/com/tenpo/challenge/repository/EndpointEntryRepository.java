package com.tenpo.challenge.repository;

import com.tenpo.challenge.model.EndpointEntryEntiy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointEntryRepository extends PagingAndSortingRepository<EndpointEntryEntiy, Long> {

}
