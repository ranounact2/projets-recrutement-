package com.centoria.jobmaroc.common.utils;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.model.PageZones;
import com.centoria.jobmaroc.model.ZoneContent;
import com.centoria.jobmaroc.model.ZoneValue;
import com.centoria.jobmaroc.service.IZonePageService;
import com.centoria.jobmaroc.service.impl.ZonePageService;
import com.centoria.jobmaroc.web.Main;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class LoadPagesZones {

    IZonePageService zonePageService = ZonePageService.getInstance();

    private void load() {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("DB/zonePage.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        int index = 0;
        Map<String, PageZones> pagesZone = new HashMap<>();
        try {
            while ((line = br.readLine()) != null ) {
                index++;
                if (index == 1) {
                    continue;
                }
                String[] fields = line.split(";");
                if (fields != null && fields.length == 7) {
                    String url = fields[1];
                    String zone = fields[2];
                    String var = fields[3];
                    String value = fields[4];
                    String strIsTemplate = fields[5];
                    String strIsSpin = fields[6];
                    boolean isTemplate = BooleanUtils.toBoolean(strIsTemplate);
                    boolean isSpin = BooleanUtils.toBoolean(strIsSpin);

                    PageZones pz = pagesZone.get(url);
                    //On crée la page si nécessaire
                    if (pz == null) {
                        pz = new PageZones();
                        pz.setCreationDate(Instant.now());
                        pz.setUrl(url);
                        pz.setPage(fields[0]);
                        pagesZone.put(url, pz);
                    }

                    //On créé la zone si nécessaire
                    if (pz.getZones() == null || pz.getZones().get(zone) == null) {
                        ZoneContent zoneContent = new ZoneContent();
                        zoneContent.setZoneCode(zone);
                        if (pz.getZones() == null) {
                            pz.setZones(new HashMap<>());
                        }
                        pz.getZones().put(zone, zoneContent);
                    }

                    ZoneContent currentZone = pz.getZones().get(zone);

                    if (currentZone.getValues() == null) {
                        currentZone.setValues(new HashMap<>());
                    }
                    if (currentZone.getValues().get(var) != null) {
                        //Si la valeur a été déclarée deux fois il y a un probleme
                        log.warn("La ligne num {} pose probleme cette valeur a déjà été renseignée doublon", index);
                    } else {
                        ZoneValue zoneValue = new ZoneValue();
                        zoneValue.setCode(var);
                        zoneValue.setValue(value);
                        zoneValue.setSpin(isSpin);
                        zoneValue.setTemplate(isTemplate);
                        currentZone.getValues().put(var, zoneValue);
                    }
                } else {
                    log.error("La ligne num {} pose probleme il manque des champs ou il y en a trop", index);
                }
            }
            List<PageZones> result = pagesZone.values().parallelStream().collect(Collectors.toList());
            zonePageService.addOrUpdate(result);

        } catch (IOException e) {
            log.error("Error reading zonePage.csv file", e);
        } catch (TechnicalException e) {
            log.error("Technical error loading page zones", e);
        } catch (BusinessException e) {
            log.error("Business error loading page zones", e);
        }

    }

    public static void main(String[] args) {
        InputStream inputStream = Main.class.getResourceAsStream("/config/config." + System.getenv("EM_ENV") + ".properties");
        try {
            ApplicationContext.getInstance().getProps(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LoadPagesZones loadPagesZones = new LoadPagesZones();
        loadPagesZones.load();

    }

}
