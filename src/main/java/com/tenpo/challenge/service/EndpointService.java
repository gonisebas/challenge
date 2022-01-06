package com.tenpo.challenge.service;

import com.tenpo.challenge.model.EndpointEntryEntiy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface EndpointService {

    Page<EndpointEntryEntiy> findAll(Pageable pageable);

    void save(EndpointEntryEntiy endpoint);
}
