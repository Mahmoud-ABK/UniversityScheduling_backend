package com.scheduling.universityschedule_backend.model;

/**
 * Enum representing the status of a makeup session proposal (PropositionDeRattrapage).
 *
 * The status tracks the administrative lifecycle of a proposal:
 *
 * <ul>
 *     <li>{@code PENDING} — Default state when a proposal is first submitted. Awaiting review.</li>
 *     <li>{@code SCHEDULED} — The proposal has been accepted but not yet assigned a room. Awaiting finalization.</li>
 *     <li>{@code APPROVED} — The makeup session has been officially validated and assigned a room. A Seance has been created.</li>
 *     <li>{@code REJECTED} — The proposal has been declined. It will not be scheduled or transformed into a session.</li>
 * </ul>
 *
 * These states are used in the administrative workflow for managing and validating proposed catch-up sessions.
 */
public enum Status {
    PENDING,
    SCHEDULED,
    APPROVED,
    REJECTED
}
