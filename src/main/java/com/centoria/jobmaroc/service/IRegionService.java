package com.centoria.jobmaroc.service;


import com.centoria.jobmaroc.model.Region;

import java.util.List;

public interface IRegionService {
    List<Region> getAllRegions();

    Region getRegionBySlug(String regionSlug);
}
