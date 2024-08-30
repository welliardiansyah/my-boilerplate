package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "w_user", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_created_by", columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String fullname;

    @Column(unique = true)
    private String email;

    private String password;

    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    private UUID updatedBy;

    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    private Date updatedDate;
}
