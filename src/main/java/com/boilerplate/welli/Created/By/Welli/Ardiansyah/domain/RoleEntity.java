package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "w_user_role", indexes = {
        @Index(name = "idx_role_name", columnList = "name"),
        @Index(name = "idx_created_by", columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "w_role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    private String updatedBy;

    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    private Date updatedDate;
}
