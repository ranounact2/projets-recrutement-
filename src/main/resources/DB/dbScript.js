/*
 * script de mise a jour de la base
 */

db.getCollection('jobs').createIndex( { title: "text", content: "text"} );