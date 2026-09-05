package com.centoria.jobmaroc.scripts;

import com.centoria.jobmaroc.dao.mongodb.MongoDBManagerFactory;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.impl.AdService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BackfillEmbeddings {

    public static void main(String[] args) {
        log.info("Starting Backfill Embeddings Script...");

        try {
            // Force initialize MongoDB connection
            MongoDBManagerFactory.getInstance();
            log.info("MongoDB connection established.");

            IAdService adService = AdService.getInstance();

            // Fetch all ads (we might need a custom query to get ALL including non-valid ones, 
            // but getAllAds usually gets a batch. Let's write a simple loop or use direct DAO)
            // Wait, getAllAds in AdService is paginated to 12. Let's use get with a large limit or loop pages.
            
            int page = 1;
            int batchSize = 100;
            boolean hasMore = true;
            int updatedCount = 0;

            while (hasMore) {
                log.info("Fetching ads batch, page {}", page);
                List<Ad> ads = adService.get("{}", null, "{creationDate: -1}", page, batchSize);
                
                if (ads == null || ads.isEmpty()) {
                    hasMore = false;
                    break;
                }

                for (Ad ad : ads) {
                    if (ad.getEmbedding() == null || ad.getEmbedding().isEmpty()) {
                        log.info("Generating embedding for Ad: {}", ad.getTitle());
                        // adService.addOrUpdate will generate the embedding automatically because we added the logic there.
                        adService.addOrUpdate(ad); 
                        updatedCount++;
                        
                        // Sleep slightly to avoid rate limiting on API
                        Thread.sleep(500);
                    }
                }
                
                if (ads.size() < batchSize) {
                    hasMore = false;
                } else {
                    page++;
                }
            }

            log.info("Backfill complete! Updated {} ads with embeddings.", updatedCount);

        } catch (Exception e) {
            log.error("Error during backfill", e);
        } finally {
            System.exit(0); // Ensure script terminates
        }
    }
}
