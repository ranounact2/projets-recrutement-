package com.centoria.jobmaroc.service.impl;


import com.centoria.jobmaroc.dao.IRegionDao;
import com.centoria.jobmaroc.dao.impl.RegionDao;
import com.centoria.jobmaroc.model.Region;
import com.centoria.jobmaroc.service.IRegionService;

import java.util.List;

public class RegionService extends BaseService<Region, IRegionDao> implements IRegionService {
    private static IRegionService instance = null;

    public RegionService() {
        dao = RegionDao.getInstance();
    }

    public static IRegionService getInstance() {
        if (instance == null) {
            instance = new RegionService();
        }
        return instance;
    }

    @Override
    public List<Region> getAllRegions() {
        List<Region> regions = get("{}", null, "{}", 0, 12);
        return regions != null ? regions : null;
    }

    @Override
    public Region getRegionBySlug(String slug) {
        String query = "{" + "slug:'" + slug + "'}";
        List<Region> regions = get(query, null, null, -1, -1);
        if (regions != null && regions.size() > 0) {
            return regions.get(0);
        }
        return null;
    }

}
