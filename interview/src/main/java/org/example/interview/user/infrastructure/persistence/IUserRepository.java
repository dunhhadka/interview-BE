package org.example.interview.user.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.example.interview.user.application.utils.JsonUtils;
import org.example.interview.user.domain.user.logs.AppEventType;
import org.example.interview.user.domain.user.logs.UserLog;
import org.example.interview.user.domain.user.model.User;
import org.example.interview.user.job.UserActivityConsumer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class IUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserActivityConsumer userActivityConsumer;

    @Override
    public List<User> findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.generalInfo.email = :email";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("email", email);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(User user) {
        boolean isNew = user.isNew();
        if (!isNew && CollectionUtils.isEmpty(user.getDomainEvents())) return;

        var userLog = createUserLog(isNew, user);

        if (isNew) entityManager.persist(user);
        else entityManager.merge(user);

        entityManager.persist(userLog);
        entityManager.flush();

        try {
            userActivityConsumer.listen(userLog); // TODO: sử dụng kafka để handle event
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    public User findById(UUID id) {
        return entityManager.find(User.class, id);
    }

    private UserLog createUserLog(boolean isNew, User user) {
        return new UserLog(
                user.getId(),
                isNew ? AppEventType.add
                        : (BooleanUtils.isFalse(user.isMarkDelete()) ? AppEventType.update : AppEventType.delete),
                JsonUtils.marshal(user)
        );
    }
}
