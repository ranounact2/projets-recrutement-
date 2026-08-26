package com.centoria.jobmaroc.dao.impl;


import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IApplicantDao;
import com.centoria.jobmaroc.dao.mongodb.MongoDBManagerFactory;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;

import java.util.regex.Pattern;

@Slf4j
public class ApplicantDao extends AbstractSimpleGenericDao<Applicant> implements IApplicantDao {

	private static IApplicantDao instance = null;

	private ApplicantDao() {
		targetClass = Applicant.class;
	}

	public static IApplicantDao getInstance() {
		if (instance == null) {
			instance = new ApplicantDao();
		}
		return instance;
	}

	@Override
	public boolean existsByJobIdAndEmailIgnoreCase(String jobId, String email) {
		if (jobId == null || jobId.isBlank() || email == null || email.isBlank()) {
			return false;
		}
		try {
			MongoCollection<Applicant> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase()
					.getCollection(new Applicant().getCollectionName(), Applicant.class);
			String trimmed = email.trim();
			Bson filter = Filters.and(
					Filters.eq("jobId", jobId),
					Filters.regex("email", "^" + Pattern.quote(trimmed) + "$", "i"));
			return collection.countDocuments(filter) > 0;
		} catch (Exception e) {
			log.error("Error checking existing applicant for jobId={}", jobId, e);
			throw new TechnicalException("Error checking duplicate application", e.getMessage());
		}
	}
}
