package com.future.onlinetraining.repository;

import com.future.onlinetraining.entity.Classroom;
import com.future.onlinetraining.entity.projection.ClassroomData;
import com.future.onlinetraining.entity.projection.ClassroomSubscribed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    List<Classroom> findAll();

    Page<Classroom> findAll(Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select c.id, c.name, m.name as module_name, m.description " +
                    "from classrooms c " +
                    "inner join modules m " +
                    "on m.id = c.module_id " +
                    "inner join classroom_results cr " +
                    "on c.id = cr.classroom_id " +
                    "where cr.user_id = :userId "
    )
    Page<ClassroomSubscribed> findSubscribed(Pageable pageable, @Param("userId") int userId);

    @Query(
            value = "select new com.future.onlinetraining.entity.projection.ClassroomData(" +
                    "c.id, c.name, m.name, t.fullname, c.status, c.min_member, c.max_member, " +
                    "(select count(cres) from cres), " +
                    "(select count(cr) from cr)) " +
                    "from Classroom c " +
                    "inner join c.module m " +
                    "inner join m.moduleCategory mc " +
                    "inner join c.trainer t " +
                    "inner join c.classroomRequests cr " +
                    "inner join c.classroomResults cres " +
                    "group by c, m, t"
    )
    Page<ClassroomData> all(Pageable pageable);
}
