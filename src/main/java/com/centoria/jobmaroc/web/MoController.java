package com.centoria.jobmaroc.web;

import com.centoria.jobmaroc.common.ihm.IWebExecutor;
import com.centoria.jobmaroc.web.base.BaseController;

public class MoController extends BaseController implements IWebExecutor {
    @Override
    public void defineRoutes() {

    }

//    private MoApp moApp = MoApp.getInstance();
//
//    @Override
//    public void defineRoutes() {
//
//        /*
//         * page Accueil in mddleOffice modifier et supprimer un offer avec un recherche
//         * spécifique par keyword
//         */
//        Spark.get("/m-office/mes-annonces/:secretCode", (request, response) -> index(request, response),
//                TEMPLATEENGINE);
//
//        /*
//         * deleting a job from middle office
//         */
//        Spark.get("/backoffice/mes-annonces/:secretCode/delete/:id", (request, response) -> delete(request, response));
//
//        /*
//         * updating a job from middle office
//         */
//        Spark.get("/m-office/mes-annonces/:secretCode/update/:id", (request, response) -> update(request, response),
//                TEMPLATEENGINE);
//        Spark.get("/m-office/mes-annonces/:secretCode/updateState/:id", (request, response) -> updateState(request, response),
//                TEMPLATEENGINE);
//
//        /*
//         * getting secret code page
//         */
//        Spark.get("/mes-annonces-emploi", (request, response) -> getSecretCode(request, response), TEMPLATEENGINE);
//
//        /*
//         * envoi le mail avec le secret code
//         *
//         */
//        Spark.post("/mes-annonces-emploi", (request, response) -> {
//            Map<String, Object> map = new HashMap<>();
//            InputSearchAdDTO inputdto = new InputSearchAdDTO();
//            String host = request.scheme() + "://" + request.host();
//
//            /*
//             * le dto en entrée
//             */
//            inputdto.fillFromParams(requestToMap(request, "email", "g-recaptcha-response"));
//
//            SearchResultAdDto result = moApp.sendEmailWithSecretCode(inputdto, host);
//            if (result.getMessages().isEmpty()) {
//                result.setMessages(Map.of("message", "Bonjour,\n\nNous avons le plaisir de vous informer que nous vous avons envoyé un e-mail par lequel vous pouvez accéder à votre annonce.\n\nCordialement.\nL’équipe emplois-maroc."));
//            }
//
//            map.put("message", "un lien vers vos annonces a été envoyer");
//            map.put("data", result);
//            return getBasePage("front/message.ftl", map, request);
//        }, TEMPLATEENGINE);
//
//        /*
//         * link validate job result change state from new to new_verified
//         */
//        Spark.get("/verification/:code/:id", (request, response) -> {
//            Map<String, Object> map = new HashMap<>();
//
//            String code = request.params("code");
//            String _id = request.params("id");
//            String host = request.scheme() + "://" + request.host();
//
//            SearchResultAdDto result = moApp.verify(_id, code, host);
//            map.put("data", result);
//            return getBasePage("front/message.ftl", map, request);
//        }, TEMPLATEENGINE);
//
//    }
//
//    private ModelAndView getSecretCode(Request request, Response response) {
//        Map<String, Object> map = getMap(request);
//        return getBasePage("m-office/secret-code.ftl", map, request);
//    }
//
//    private ModelAndView update(Request request, Response response) {
//        Map<String, Object> map = getMap(request);
//        InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
//                .withCategories(true)
//                .withCities(true)
//                .id(request.params("id"))
//                .build();
//        AdWithReferenceDataDto result = null;
//
//        try {
//            result = moApp.getByIdWithReferenceData(inputDto);
//        } catch (TechnicalException | BusinessException e) {
//            if (e.getCause() instanceof BusinessException) {
//                throw new BusinessException("400", "Business Exception");
//            } else {
//                throw new TechnicalException("500", "Technical Error");
//            }
//        }
//        map.put("data", result);
//
//        return getBasePage("front/offre.ftl", map, request);
//    }
//
//    private
//    ModelAndView updateState(Request request,Response response){
//        String _id = request.params("id");
//        String secretCode = request.params("secretCode");
//        try {
//            moApp.closeAd(_id, secretCode);
//        } catch (TechnicalException | BusinessException e) {
//            if (e.getCause() instanceof BusinessException) {
//                throw new BusinessException("400", "Business Exception");
//            } else {
//                throw new TechnicalException("500", "Technical Error");
//            }
//        }
//
//        return index(request, response);
//    }
//
//    private ModelAndView delete(Request request, Response response) {
//        String _id = request.params("id");
//        String secretCode = request.params("secretCode");
//        try {
//            moApp.delete(_id, secretCode);
//        } catch (TechnicalException | BusinessException e) {
//            if (e.getCause() instanceof BusinessException) {
//                throw new BusinessException("400", "Business Exception");
//            } else {
//                throw new TechnicalException("500", "Technical Error");
//            }
//        }
//
//        return index(request, response);
//    }
//
//    private ModelAndView index(Request request, Response response) {
//        String secretCode = request.params("secretCode");
//        int pageNumber = Integer.parseInt(request.queryParams("page") != null ? request.queryParams("page") : "1");
//        int adsPerPage = Integer.parseInt(request.queryParams("size") != null ? request.queryParams("size") : "10");
//        AdResult adsBySecretCode = moApp.getAdsBySecretCode(secretCode, pageNumber, adsPerPage);
//        Map<String, Object> map = extractAdDetails(adsBySecretCode, pageNumber, adsPerPage);
//        return getBasePage("m-office/mo-annonce.ftl", map, request);
//    }

}
