package com.ielia.test.jackson.errorinstrumentation;

public class MutationIndexIndicator {
    public long currentMutationIndex = 0;
    public final long targetMutationIndex;

    public MutationIndexIndicator(long targetMutationIndex) {
        this.targetMutationIndex = targetMutationIndex;
    }
}
