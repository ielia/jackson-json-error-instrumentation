package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.mutagens.Mutagen;

public class MutationIndexIndicator {
    public long currentMutationIndex = 0;
    public final long targetMutationIndex;

    protected String description;
    protected String path;
    protected Class<? extends Mutagen> mutagen;

    public MutationIndexIndicator(long targetMutationIndex) {
        this.targetMutationIndex = targetMutationIndex;
    }

    public String getDescription() {
        return description;
    }

    public Class<? extends Mutagen> getMutagen() {
        return mutagen;
    }

    public String getPath() {
        return path;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMutagen(Class<? extends Mutagen> mutagen) {
        this.mutagen = mutagen;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
