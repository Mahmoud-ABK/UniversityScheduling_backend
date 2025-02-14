package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for managing academic branches.
 * Includes CRUD operations and additional branch-related functionalities.
 */
public interface BrancheService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves branch details by ID.
     * @param id Branch's unique identifier
     * @return Branch DTO
     * @throws CustomException if branch not found
     */
    BrancheDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all branches.
     * @return List of all branches
     * @throws CustomException if retrieval fails
     */
    List<BrancheDTO> findAll() throws CustomException;

    /**
     * Creates a new branch.
     * @param branche Branch DTO containing the new data
     * @return Created branch DTO
     * @throws CustomException if creation fails
     */
    BrancheDTO create(BrancheDTO branche) throws CustomException;

    /**
     * Updates an existing branch.
     * @param id Branch's unique identifier
     * @param branche Updated branch data
     * @return Updated branch DTO
     * @throws CustomException if update fails
     */
    BrancheDTO update(Long id, BrancheDTO branche) throws CustomException;

    /**
     * Deletes a branch by ID.
     * @param id Branch's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Assigns a teacher to a branch.
     * @param branchId Branch's unique identifier
     * @param teacherDTO Teacher DTO containing the teacher data
     * @throws CustomException if assignment fails
     */
    void assignTeacherToBranch(Long branchId, EnseignantDTO teacherDTO) throws CustomException;

    /**
     * Removes a teacher from a branch.
     * @param branchId Branch's unique identifier
     * @param teacherId Teacher's unique identifier
     * @throws CustomException if removal fails
     */
    void removeTeacherFromBranch(Long branchId, Long teacherId) throws CustomException;

    /**
     * Assigns a student to a branch.
     * @param branchId Branch's unique identifier
     * @param studentDTO Student DTO containing the student data
     * @throws CustomException if assignment fails
     */
    void assignStudentToBranch(Long branchId, EtudiantDTO studentDTO) throws CustomException;

    /**
     * Removes a student from a branch.
     * @param branchId Branch's unique identifier
     * @param studentId Student's unique identifier
     * @throws CustomException if removal fails
     */
    void removeStudentFromBranch(Long branchId, Long studentId) throws CustomException;

    /**
     * Retrieves all courses associated with a branch.
     * @param branchId Branch's unique identifier
     * @return List of courses associated with the branch
     * @throws CustomException if retrieval fails
     */
    List<SeanceDTO> getCoursesByBranch(Long branchId) throws CustomException;
}
