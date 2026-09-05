package com.centoria.jobmaroc.dao;

import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dao.mongodb.MongoDBManagerFactory;
import com.centoria.jobmaroc.model.AbstractModel;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

@Slf4j
public abstract class AbstractSimpleGenericDao<T extends AbstractModel> implements ISimpleGenericDao<T> {

    protected Class<T> targetClass;

    @Override
    public String addOrUpdate(T obj) {
        MongoCollection<T> collection;
        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            if (obj.getKey() == null) {
                InsertOneResult result = collection.insertOne(obj);
                if (result.getInsertedId() != null) {
                    String insertedId = result.getInsertedId().asObjectId().getValue().toString();
                    obj.setKey(insertedId);
                }
                return obj.getKey();
            } else {
                ObjectId objectId = new ObjectId(obj.getKey());
                collection.replaceOne(Filters.eq("_id", objectId), obj, new ReplaceOptions().upsert(true));
                return obj.getKey();
            }
        } catch (Exception e) {
            log.error("Error adding/updating object", e);
            throw new TechnicalException("Error adding/updating object", e.getMessage());
        }
    }

    @Override
    public List<String> addOrUpdate(List<T> objs) {
        List<String> result = new ArrayList<>();
        if (objs != null && !objs.isEmpty()) {
            for (T t : objs) {
                result.add(addOrUpdate(t));
            }
        }
        return result;
    }

    @Override
    public T find(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID cannot be null or empty");
            }

            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            return collection.find(Filters.eq("_id", new ObjectId(id))).first();
        } catch (Exception e) {
            log.error("Error finding object by ID", e);
            throw new TechnicalException("Error finding object by ID", e.getMessage());
        }
    }

    @Override
    public List<T> find(String query, String projection, String sort, int pagenumber, int jobsperpage) {
        List<T> resultList = new ArrayList<>();

        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            List<Bson> pipeline = new ArrayList<>();

            if (query != null && !query.isEmpty()) {
                pipeline.add(match(Document.parse(query)));
            }

            if (projection != null && !projection.isEmpty()) {
                if (projection != null && !projection.equals("{}")) {
                    Document projectionDoc = Document.parse(projection);
                    List<String> includeFields = new ArrayList<>();
                    List<String> excludeFields = new ArrayList<>();

                    for (String field : projectionDoc.keySet()) {
                        if (projectionDoc.getInteger(field) == 1) {
                            includeFields.add(field);
                        } else {
                            excludeFields.add(field);
                        }
                    }

                    Bson projectionStage;
                    if (!includeFields.isEmpty()) {
                        projectionStage = project(include(includeFields));
                    } else if (!excludeFields.isEmpty()) {
                        projectionStage = project(exclude(excludeFields));
                    } else {
                        projectionStage = project(fields());
                    }
                    pipeline.add(projectionStage);
                }
            }

            if (sort != null && !sort.isEmpty() && !sort.equals("{}")) {
                Document sortDoc = Document.parse(sort);
                List<Bson> sortCriteria = new ArrayList<>();

                for (String field : sortDoc.keySet()) {
                    if (sortDoc.getInteger(field) == 1) {
                        sortCriteria.add(ascending(field));
                    } else {
                        sortCriteria.add(descending(field));
                    }
                }

                if (!sortCriteria.isEmpty()) {
                    pipeline.add(sort(orderBy(sortCriteria)));
                }
            }

            if (pagenumber > 0 && jobsperpage > 0) {
                pipeline.add(skip(jobsperpage * (pagenumber - 1)));
                pipeline.add(limit(jobsperpage));
            }

            AggregateIterable<T> iterable = collection.aggregate(pipeline, targetClass);
            iterable.forEach(resultList::add);

        } catch (Exception e) {
            log.error("Error finding objects with query", e);
            throw new TechnicalException("Error finding objects with query", e.getMessage());
        }
        return resultList;
    }

    @Override
    public List<T> search(String query) {
        List<T> resultList = new ArrayList<>();
        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            List<Document> pipeline = new ArrayList<>();
            pipeline.add(Document.parse(query));

            AggregateIterable<T> iterable = collection.aggregate(pipeline, targetClass);
            for (T document : iterable) {
                resultList.add(document);
            }
        } catch (Exception e) {
            log.error("Error searching objects", e);
            throw new TechnicalException("Error searching objects", e.getMessage());
        }
        return resultList;
    }

    @Override
    public List<T> aggregate(List<String> pipelineStagesJson) {
        List<T> resultList = new ArrayList<>();
        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            List<Document> pipeline = new ArrayList<>();
            for (String stageJson : pipelineStagesJson) {
                pipeline.add(Document.parse(stageJson));
            }

            AggregateIterable<T> iterable = collection.aggregate(pipeline, targetClass);
            for (T document : iterable) {
                resultList.add(document);
            }
        } catch (Exception e) {
            log.error("Error executing aggregate pipeline", e);
            throw new TechnicalException("Error executing aggregate pipeline", e.getMessage());
        }
        return resultList;
    }

    protected List<org.bson.Document> jsonQueryToPipeline(String jsonPipeline) {
        List<org.bson.Document> pipeline = new ArrayList<>();
        StringBuilder currentStage = new StringBuilder();
        int bracketCount = 0;

        for (int i = 0; i < jsonPipeline.length(); i++) {
            char c = jsonPipeline.charAt(i);

            if (c == '{') {
                bracketCount++;
            } else if (c == '}') {
                bracketCount--;
            }

            currentStage.append(c);

            if (bracketCount == 0 && currentStage.length() > 0) {
                try {
                    org.bson.Document stage = org.bson.Document.parse(currentStage.toString());
                    pipeline.add(stage);
                } catch (Exception e) {
                    throw new TechnicalException("Error parsing pipeline stage", e.toString());
                }
                currentStage = new StringBuilder();
            }
        }

        if (bracketCount != 0 || currentStage.length() > 0) {
            throw new TechnicalException("", "Invalid pipeline format");
        }

        return pipeline;
    }

    protected List<String> jsonQueryToPipeLineArray(String jsonPipeline) {
        StringBuilder b = new StringBuilder(jsonPipeline.trim());

        int bracketCount = -1;
        List<String> parts = new ArrayList<String>();
        int i = 0;
        while (i < b.length()) {
            if (b.charAt(i) == '{') {
                if (bracketCount == -1) {
                    b.delete(0, i);
                    bracketCount = 0;
                    i = 0;
                }
                bracketCount++;
            }
            if (b.charAt(i) == '}') {
                bracketCount--;
            }
            if (bracketCount == 0) {
                String part = b.substring(0, i + 1);
                parts.add(part);
                bracketCount = -1;

                if (i == b.length() - 1) {
                    break;
                }
                b.delete(0, i + 1);
                i = 0;
            }

            i++;
        }

        if (parts.size() == 0) {
            throw new TechnicalException("", "Unable to parse pipeline operators");
        }

        return parts;
    }

    @Override
    public void delete(T obj) {
        delete(obj.getKey());
    }

    @Override
    public void delete(List<T> objs) {
        if (objs != null && !objs.isEmpty()) {
            for (T t : objs) {
                delete(t.getKey());
            }
        }
    }

    @Override
    public void delete(String id) {
        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            if (result.getDeletedCount() == 0) {
                log.warn("No document deleted for id: {}", id);
            }
        } catch (Exception e) {
            log.error("Error deleting object", e);
            throw new TechnicalException("Error deleting object: ", e.getMessage());
        }
    }

    @Override
    public long count(String query) {
        try {
            T classObj = targetClass.getDeclaredConstructor().newInstance();
            String collectionName = classObj.getCollectionName();
            MongoCollection<T> collection = MongoDBManagerFactory.getInstance().getManager().getDatabase().getCollection(collectionName, targetClass);

            org.bson.Document document = org.bson.Document.parse(query);
            return collection.countDocuments(document);
        } catch (Exception e) {
            log.error("Error counting documents", e);
            throw new TechnicalException("Error counting documents", e.getMessage());
        }
    }
}
