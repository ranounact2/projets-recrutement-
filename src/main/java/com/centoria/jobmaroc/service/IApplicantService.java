package com.centoria.jobmaroc.service;

import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.model.Applicant;

public interface IApplicantService extends IBaseService<Applicant>{

	void renderTemplate(MailDto<ApplicantDto> mdto);

	void initState(Applicant appli);

	ApplicantDto prepareApplicationForm(String jobKey);

	/** @return true si une candidature existe déjà pour cette offre avec cette adresse e-mail. */
	boolean hasExistingApplicationForJob(String email, String jobId);
}
