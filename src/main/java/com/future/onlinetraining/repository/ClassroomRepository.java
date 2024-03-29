package com.future.onlinetraining.repository;

import com.future.onlinetraining.entity.Classroom;
import com.future.onlinetraining.entity.projection.ClassroomData;
import com.future.onlinetraining.entity.projection.ClassroomDetailData;
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
            value = "from Classroom c " +
                    "inner join c.classroomResults crs " +
                    "inner join crs.user u " +
                    "where u.id = :userId"
    )
    Page<Classroom> findSubscribed(Pageable pageable, @Param("userId") int userId);

    @Query(
            value = "select new com.future.onlinetraining.entity.projection.ClassroomData(" +
                    "c.id, c.name, m.name, t.fullname, c.status, c.min_member, c.max_member, " +
                    "(select count(cres) from cres), " +
                    "(select count(cr) from cr), " +
                    "case when avg(mrg.value) is null then 0.0 else avg(mrg.value) end, m.version ) " +
                    "from Classroom c " +
                    "inner join c.module m " +
                    "inner join m.moduleCategory mc " +
                    "inner join c.trainer t " +
                    "left join c.classroomRequests cr " +
                    "left join c.classroomResults cres " +
                    "left join c.classroomSessions cs " +
                    "left join m.moduleRatings mrg " +
                    "where (:nameParam is null or lower(m.name) like %:nameParam%) " +
                    "and (:hasExam is null or " +
                    "((select count(cs) from cs where cs.isExam = true) > 0 " +
                    "and :hasExam = true) or " +
                    "((select count(cs) from cs where cs.isExam = true) = 0 " +
                    "and :hasExam = false)) " +
                    "group by c, m, t"
    )
    Page<ClassroomData> all(Pageable pageable, @Param("nameParam") String name, @Param("hasExam") Boolean hasExam);

    @Query(
            value = "from Classroom c where c.id = :id"
    )
    Classroom find(@Param("id") Integer id);

    @Query(
            value = "select new com.future.onlinetraining.entity.projection.ClassroomDetailData(c, count(crs)) " +
            "from Classroom c left join c.classroomResults crs " +
            "where c.id = :id group by c ")
    ClassroomDetailData getDetail(@Param("id") Integer id);
}
