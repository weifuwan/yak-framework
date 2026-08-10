package io.yak.framework.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PageDataTest {

    @Test
    void shouldDefensivelyCopyRecords() {
        List<String> source = new ArrayList<>(List.of("a", "b"));

        PageData<String> page = new PageData<>(source, 2L, 1L, 1L, 20L);
        source.add("c");

        assertEquals(List.of("a", "b"), page.records());
        assertThrows(UnsupportedOperationException.class, () -> page.records().add("d"));
    }

    @Test
    void shouldMapRecordsAndKeepPaginationMetadata() {
        PageData<Integer> page = new PageData<>(List.of(1, 2), 12L, 6L, 3L, 2L);

        PageData<String> mapped = page.map(String::valueOf);

        assertEquals(List.of("1", "2"), mapped.records());
        assertEquals(12L, mapped.total());
        assertEquals(6L, mapped.pages());
        assertEquals(3L, mapped.pageNo());
        assertEquals(2L, mapped.pageSize());
    }

    @Test
    void shouldKeepExistingHttpPagingShapeWhenConverting() {
        PagingData<String> pagingData =
                PagingData.from(new PageData<>(List.of("a"), 21L, 3L, 2L, 10L));

        assertEquals(List.of("a"), pagingData.getBizData());
        assertEquals(21L, pagingData.getPagination().getTotal());
        assertEquals(3L, pagingData.getPagination().getPages());
        assertEquals(2L, pagingData.getPagination().getPageNo());
        assertEquals(10L, pagingData.getPagination().getPageSize());
    }
}
