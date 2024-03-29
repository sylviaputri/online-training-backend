package com.future.onlinetraining.entity;

import com.fasterxml.jackson.annotation.*;
import com.future.onlinetraining.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classrooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Integer.class)
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User trainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Module module;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ClassroomResult> classroomResults;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    private List<ClassroomRequest> classroomRequests;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<ClassroomSession> classroomSessions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ClassroomMaterial> classroomMaterials;

    private Integer min_member;

    private Integer max_member;

    private String status;

}
