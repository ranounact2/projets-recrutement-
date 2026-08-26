package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.dao.IZonePageDao;
import com.centoria.jobmaroc.dao.impl.ZonePageDao;
import com.centoria.jobmaroc.model.PageZones;
import com.centoria.jobmaroc.service.IZonePageService;

public class ZonePageService extends BaseService<PageZones, IZonePageDao> implements IZonePageService {

    private static IZonePageService instance = null;

    private ZonePageService() {
        dao = ZonePageDao.getInstance();
    }

    public static IZonePageService getInstance() {
        if (instance == null) {
            instance = new ZonePageService();
        }
        return instance;
    }


}
