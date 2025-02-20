package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Batch Data Transfer Object.
 ** This DTO is designed for batch operations, primarily for responses.
 * Typically, the request payload comprises a list of full DTOs.
 * In response, this DTO returns a list of processed resource IDs along with additional metadata.
 * * Fields:
 * - ids: A list of Long resource IDs that have been processed.
 * - message: A message describing the result of the batch operation.
 * - success: A flag indicating whether the batch operation was successful.
 * - entityType: A property specifying the type of resource IDs (e.g., "Seance", "Branche").
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDTO {
    private List<Long> ids;
    private String message;
    private boolean success;
    private String entityType;
    
}
