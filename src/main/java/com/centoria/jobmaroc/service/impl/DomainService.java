package com.centoria.jobmaroc.service.impl;
import com.centoria.jobmaroc.dao.IDomainDao;
import com.centoria.jobmaroc.dao.impl.DomainDao;
import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.dto.mapper.MapperDomainDto;
import com.centoria.jobmaroc.model.Domain;
import com.centoria.jobmaroc.service.IDomainService;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class DomainService extends BaseService<Domain,IDomainDao> implements IDomainService {

	private static IDomainService instance = null;

	private DomainService() {
		dao = DomainDao.getInstance();
	}

	private final MapperDomainDto mapperDomain = Mappers.getMapper(MapperDomainDto.class);

	public static IDomainService getInstance() {
		if (instance == null) {
			instance = new DomainService();
		}
		return instance;
	}

	public DomainDto getDomainBySlug(String slug) {
		String query = "{" + "slug:'" + slug + "'}" ;
		List<Domain> domains = get(query, null, null, 1, 1);
		if (domains != null && domains.size() > 0) {
			return mapperDomain.asDto(domains.get(0));
		}
		return null;
	}

	public List<DomainDto> getAllDomain() {
		List<DomainDto> domainDtos = null;
		List<Domain> domais = get("{}", null, null, -1, -1);
		if (domais != null) {
			domainDtos = mapperDomain.asDtos(domais);
		}
		return domainDtos;
	}

	@Override
	public DomainDto getDomainByName(String domainName) {
		String query = "{" + "name:'" + domainName + "'}" ;
		List<Domain> domains = get(query, null, null, 1, 1);
		if (domains != null && domains.size() > 0) {
			return mapperDomain.asDto(domains.get(0));
		}
		return null;
	}
}
