package com.centoria.jobmaroc.dao.mongodb;

import com.mongodb.client.MongoDatabase;

/**
 * Interface for getting drivers for accessing mongodb 
 * @author pc
 *
 */
public interface IMongoManager {

	/**
	 * Getting MongoDB database instance
	 * @return MongoDatabase instance
	 */
	MongoDatabase getDatabase();
}
