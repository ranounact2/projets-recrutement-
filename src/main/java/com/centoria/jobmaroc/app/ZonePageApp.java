package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.model.PageZones;
import com.centoria.jobmaroc.model.ZoneContent;
import com.centoria.jobmaroc.service.IZonePageService;
import com.centoria.jobmaroc.service.impl.ZonePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZonePageApp {

    private static final Logger logger = LoggerFactory.getLogger(ZonePageApp.class);
    private static ZonePageApp instance = null;
    private IZonePageService zonePageService = ZonePageService.getInstance();

    private ZonePageApp() {
    }

    public static ZonePageApp getInstance() {
        if (instance == null) {
            instance = new ZonePageApp();
        }
        return instance;
    }

    public List<PageZones> search(String page,
                                  String url,
                                  Map<String, String> params,
                                  Map<String, String[]> queryParams)
            {

        // 1) tous les documents pour cette page
        String pageQuery = "{\"page\":\"" + page + "\"}";
        List<PageZones> allZones = zonePageService.get(pageQuery, null, null, 0, 0);
        if (allZones == null || allZones.isEmpty()) return Collections.emptyList();

        // 2) match exact
        PageZones match = allZones.stream()
                .filter(zp -> url.equals(zp.getUrl()))
                .findFirst()
                .orElse(null);

        String extractedDomain = null;
        String extractedRegion = null;
        String extractedVille  = null;  // <— ajouté
        String extractedId     = null;  // <— ajouté pour :id

        // 3) placeholder :domain, :region, :ville, :id
        if (match == null) {
            for (PageZones zp : allZones) {
                String pattern = zp.getUrl();
                if (pattern.contains(":domain") || pattern.contains(":region") || pattern.contains(":ville") || pattern.contains(":id")) {
                    // Construire une regex à partir du pattern
                    // :domain, :region, :ville, :id deviennent des groupes ([^/]+)
                    // Cela garantit que le nombre de segments correspond exactement
                    String regex = pattern
                            .replace("/", "\\/")
                            .replace(":domain", "([^/]+)")
                            .replace(":region", "([^/]+)")
                            .replace(":ville", "([^/]+)")
                            .replace(":id", "([^/]+)");
                    regex = "^" + regex + "$";
                    java.util.regex.Pattern r = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher m = r.matcher(url);
                    if (m.matches()) {
                        int groupIdx = 1;
                        if (pattern.contains(":domain")) {
                            extractedDomain = m.group(groupIdx++);
                        }
                        if (pattern.contains(":region")) {
                            extractedRegion = m.group(groupIdx++);
                        }
                        if (pattern.contains(":ville")) {
                            extractedVille = m.group(groupIdx++);
                        }
                        if (pattern.contains(":id")) {
                            extractedId = m.group(groupIdx++);
                        }
                        match = zp;
                        break;
                    }
                }
            }
        }

        // 4) fallback racines…
        if (match == null) {
            return Collections.emptyList();
        }

        // 5) préparation des subs pour le template
        Map<String, String> subs = new HashMap<>();
        if (extractedDomain != null) subs.put("domain", extractedDomain);
        if (extractedRegion != null) subs.put("region", extractedRegion);
        if (extractedVille  != null) subs.put("ville", extractedVille);
        if (extractedId     != null) subs.put("id", extractedId);

        // 6) On traite les valeurs : template puis spin si nécessaire
        match.getZones().values().parallelStream()
                .map(ZoneContent::getValues)
                .filter(map -> map != null && !map.isEmpty())
                .forEach(zoneValues -> {
                    zoneValues.forEach((key, value) -> {
                        String content = value.getValue();
                        
                        // Étape 1 : Template substitution
                        if (value.isTemplate()) {
                            try {
                                org.apache.commons.text.StringSubstitutor sub
                                        = new org.apache.commons.text.StringSubstitutor(subs, "${", "}");
                                content = sub.replace(content);
                            } catch (Exception e) {
                                logger.error("Error during template substitution for key: {}", key, e);
                                content = "";
                            }
                        }
                        
                        // Étape 2 : Spin logic
                        if (value.isSpin()) {
                            java.util.Random rand = new java.util.Random();
                            
                            // 2a) Remplace les choix entre crochets [a|b|c]
                            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\[(.*?)\\]");
                            java.util.regex.Matcher m = p.matcher(content);
                            StringBuffer sb = new StringBuffer();
                            while (m.find()) {
                                String[] opts = m.group(1).split("\\s*\\|\\s*");
                                m.appendReplacement(sb, opts[rand.nextInt(opts.length)]);
                            }
                            m.appendTail(sb);
                            content = sb.toString();
                            
                            // 2b) Si des pipes restent, on prend un segment au hasard
                            if (content.contains("|")) {
                                String[] segments = content.split("\\s*\\|\\s*");
                                content = segments[rand.nextInt(segments.length)].trim();
                            }
                        }
                        
                        value.setContent(content);
                    });
                });

        // On renvoie en liste (même s'il n'y a qu'un seul match)
        return List.of(match);
    }
}