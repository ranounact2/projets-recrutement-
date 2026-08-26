package com.centoria.jobmaroc.service;

import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.model.Domain;

import java.util.List;

public interface IDomainService extends IBaseService<Domain>{

    DomainDto getDomainBySlug(String domainSlug);
    List<DomainDto> getAllDomain();

    DomainDto getDomainByName(String domain);
}
