package com.centoria.jobmaroc.web.filter;

import com.centoria.jobmaroc.common.ihm.IWebExecutor;

//import spark.ModelAndView;
//import spark.Spark;

public class FilterController implements IWebExecutor {

    private String[] params = {};


    @Override
    public void defineRoutes() {

        /*
         * Breadcrumb
         */
//		Spark.before("/*", (request, response) -> {
//			//Je veux lire l'url utiliser et construire tous les chemins possibles
//			/*
//			 * Par exemple
//			 *
//			 * emplois-maroc/categories/secretariat/casablanca nous donnera
//			 *
//			 * {"emplois-maroc","emplois-maroc/categories","emplois-maroc/categories/secretariat"}
//			 *
//			 */
//			String[] paths = request.url() != null ? request.url().split("/"): null;
//			List<String> breadCrumb = new ArrayList<>();
//			for (int i = 0; i < paths.length; i++) {
//				for (int j = 0; j < i ; j++) {
//					String path = request.contextPath() + "/" + paths[j];
//					breadCrumb.add(path);
//				}
//			}
//			Map<String, Object> map = request.attribute("map") != null ? request.attribute("map") : new HashMap<>();
//			map.put("bc", breadCrumb);
//			request.attribute("map",map);
//		});
//

    }

}
