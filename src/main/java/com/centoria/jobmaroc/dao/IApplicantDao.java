package com.centoria.jobmaroc.dao;

import com.centoria.jobmaroc.model.Applicant;

public interface IApplicantDao extends ISimpleGenericDao<Applicant> {

    /**
     * Indique si une candidature existe déjà pour cette offre avec la même adresse e-mail
     * (comparaison insensible à la casse pour l'e-mail saisi ; celui-ci est trimé).
     */
    boolean existsByJobIdAndEmailIgnoreCase(String jobId, String email);
}
