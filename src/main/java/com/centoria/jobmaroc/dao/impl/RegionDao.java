package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IRegionDao;
import com.centoria.jobmaroc.model.Region;

public class RegionDao extends AbstractSimpleGenericDao<Region> implements IRegionDao {
    private static IRegionDao instance = null;

    private RegionDao() {
        targetClass = Region.class;
    }

    public static IRegionDao getInstance() {
        if (instance == null) {
            instance = new RegionDao();
        }
        return instance;
    }

}
