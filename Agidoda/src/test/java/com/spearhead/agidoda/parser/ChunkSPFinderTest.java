package com.spearhead.agidoda.parser;

import com.spearhead.agidoda.beans.chunks.ChunkGroup;
import com.spearhead.agidoda.utility.JsonPrinter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ChunkSPFinderTest {
    @InjectMocks
    ChunkSPFinder spFinder;

    @Test
    void name() {
        String[] strs="NP-CC-VP-NP-VP-NP-VP-NP-PP-NP-PP-NP-PP-NP-SBAR-PP-NP-VP".split("-");
        String[] tags= "NP-and-spoke-NP-allows-NP-to transport-NP-between-NP-of-NP-with-NP-than-if-NP-were served-directly.".split("-");

        List<ChunkGroup> outcome = spFinder.toChunkGroups(strs, tags);
        System.out.println( JsonPrinter.print(outcome));
    }
}