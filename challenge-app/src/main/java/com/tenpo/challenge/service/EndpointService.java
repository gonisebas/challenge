package com.tenpo.challenge.service;

import com.tenpo.challenge.model.EndpointEntryEntiy;
import com.tenpo.challenge.repository.EndpointEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

    @Autowired
    private EndpointEntryRepository endpointEntryRepository;

    public Page<EndpointEntryEntiy> findAll(Pageable pageable){
        return endpointEntryRepository.findAll(pageable);
    }

    public void save(EndpointEntryEntiy endpoint) {
        endpointEntryRepository.save(endpoint);
    }
}
