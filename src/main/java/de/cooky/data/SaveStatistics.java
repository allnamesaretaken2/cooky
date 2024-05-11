package de.cooky.data;

import java.util.List;

/**
 * Some more or less useful information about the saving process. Does NOT tell anything about errors. Errors are
 * exceptions.
 */
public class SaveStatistics<T> {

    private final long duration;
    private final int entityCount;

    private final List<T> entities;

    public SaveStatistics(List<T> entities, long duration){
        this.duration = duration;
        this.entityCount = entities.size();
        this.entities = entities;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public long getDuration() {
        return duration;
    }

    public List<T> getEntities() {
        return entities;
    }
}
