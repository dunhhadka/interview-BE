package org.example.interview.user.infrastructure.persistence

import org.example.interview.user.application.model.UserSearchRequest

object UserSql {
    fun search(request: UserSearchRequest): String {
        return """
            WITH pg AS (
                SELECT u.id
                ${buildBody(request)}
                ORDER BY u.created_on DESC
                OFFSET :offset ROWS
                FETCH NEXT :limit ROWS ONLY
            )
            SELECT * FROM users u WHERE u.id IN (SELECT id FROM pg) 
        """.trimIndent()
    }

    private fun buildBody(req: UserSearchRequest): String {
        return """
            FROM users AS u
            WHERE u.id IS NOT NULL
            ${if (req.ids?.isNotEmpty() == true) " AND u.id IN (:ids)" else ""}
            ${if (req.email?.isNotBlank() == true) " AND u.email LIKE CONCAT('%', :email, '%')" else ""}
            ${if (req.phone?.isNotBlank() == true) " AND u.phone LIKE CONCAT('%', :phone, '%')" else ""}
            ${if (req.fullName?.isNotBlank() == true) " AND u.full_name LIKE CONCAT('%', :fullName, '%')" else ""}
            ${if (req.firstName?.isNotBlank() == true) " AND u.first_name LIKE CONCAT('%', :firstName, '%')" else ""}
            ${if (req.lastName?.isNotBlank() == true) " AND u.last_name LIKE CONCAT('%', :lastName, '%')" else ""}
            ${if (req.national?.isNotBlank() == true) " AND u.national = :national" else ""}
            ${if (req.role?.isNotBlank() == true) " AND u.role = :role" else ""}
            ${if (req.type?.isNotBlank() == true) " AND u.type = :type" else ""}
            ${if (req.language?.isNotBlank() == true) " AND u.language = :language" else ""}
            ${if (req.query?.isNotBlank() == true) " AND (u.id LIKE :query OR u.full_name LIKE :query OR u.email LIKE :query) " else ""}
            ${if (req.createdAtMin != null) " AND u.created_on >= :createdAtMin" else ""}
            ${if (req.createdAtMax != null) " AND u.created_on <= :createdAtMax" else ""}
            ${if (req.activatedAtMin != null) " AND u.join_date >= :activatedAtMin" else ""}
            ${if (req.activatedAtMax != null) " AND u.join_date <= :activatedAtMax" else ""}
        """.trimIndent()
    }

    fun searchCount(request: UserSearchRequest): String {
        return """
            SELECT COUNT(*) ${buildBody(request)}
        """.trimIndent()
    }
}
