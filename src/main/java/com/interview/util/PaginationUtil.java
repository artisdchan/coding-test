package com.interview.util;

public class PaginationUtil {

    private PaginationUtil() {
    }

    /**
     * Calculate the total number of pages based on total elements and page size.
     *
     * @param totalElements Total number of elements
     * @param size Page size
     * @return Total number of pages
     */
    public static int calculateTotalPages(int totalElements, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        return (int) Math.ceil((double) totalElements / size);
    }

    /**
     * Calculate the start index for pagination.
     *
     * @param page Page number (0-based)
     * @param size Page size
     * @return Start index
     */
    public static int calculateStartIndex(int page, int size) {
        return page * size;
    }

}
