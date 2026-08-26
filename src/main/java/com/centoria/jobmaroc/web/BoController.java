package com.centoria.jobmaroc.web;

import com.centoria.jobmaroc.app.BoApp;
import com.centoria.jobmaroc.common.ihm.IWebExecutor;
import com.centoria.jobmaroc.web.base.BaseController;

public class BoController extends BaseController implements IWebExecutor {
    private BoApp boApp = BoApp.getInstance();

    @Override
    public void defineRoutes() {

    }

//    @Override
//    public void defineRoutes() {
//        Spark.get("/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE", this::boIndex, TEMPLATEENGINE);
//        Spark.post("/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE", this::action, TEMPLATEENGINE);
//    }
//
//    private ModelAndView action(Request request, Response response) {
//        String action = request.queryParams("action");
//        if (action != null) {
//            switch (action) {
//                case "validate":
//                    validate(request, response);
//                    break;
//                case "delete":
//                    delete(request, response);
//                    break;
//                case "disable":
//                    disable(request, response);
//                    break;
//                default:
//                    break;
//            }
//        }
//        return boIndex(request, response);
//    }
//
//    private ModelAndView delete(Request request, Response response) {
//        String _id = request.queryParams("key");
//        try {
//            boApp.delete(_id);
//        } catch (TechnicalException | BusinessException e) {
//            if (e.getCause() instanceof BusinessException) {
//                throw new BusinessException("400", "Business Exception");
//            } else {
//                throw new TechnicalException("500", "Technical Error");
//            }
//        }
//        return boIndex(request, response);
//    }
//
//    private ModelAndView disable(Request request, Response response) {
//        String _id = request.queryParams("key");
//        try {
//            boApp.disable(_id);
//        } catch (TechnicalException | BusinessException e) {
//            // TODO Auto-generated catch block
//            log.error("Error disabling ad", e);
//        }
//        return boIndex(request, response);
//    }
//
//    private ModelAndView validate(Request request, Response response) {
//        String _id = request.queryParams("key");
//        String host = request.scheme() + "://" + request.host();
//        try {
//            boApp.validate(_id, host);
//        } catch (TechnicalException | BusinessException e) {
//            // TODO Auto-generated catch block
//            log.error("Error disabling ad", e);
//        }
//        return boIndex(request, response);
//    }
//
//    private ModelAndView boIndex(Request request, Response response) {
//        int pageNumber = Integer.parseInt(request.queryParams("page") != null ? request.queryParams("page") : "1");
//        int adsPerPage = Integer.parseInt(request.queryParams("size") != null ? request.queryParams("size") : "10");
//        AdResult adResult = boApp.getAds(pageNumber, adsPerPage);
//        Map<String, Object> map = extractAdDetails(adResult, pageNumber, adsPerPage);
//        return getBasePage("back-office/bo-index.ftl", map, request);
//    }
}
