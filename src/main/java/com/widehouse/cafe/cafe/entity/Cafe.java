package com.widehouse.cafe.cafe.entity;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "url")})
@EntityListeners(AuditingEntityListener.class)
public class Cafe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Pattern(regexp = "^[a-z][a-z0-9_]*$")
    @Size(min = 1, max = 30)
    private String url;

    @NonNull
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CafeVisibility visibility;

    @ManyToOne
    private Category category;

    @Size(max = 1000)
    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @Embedded
    private CafeData data;

    public Cafe() {
        this.data = new CafeData();
    }

    public Cafe(Long id, String url, String name) {
        this(url, name);
        this.id = id;
    }

    /**
     * constructor.
     * @param url url of cafe
     * @param name name of cafe
     */
    public Cafe(String url, String name) {
        this();
        this.url = url;
        this.name = name;
        this.description = "";
        this.visibility = CafeVisibility.PUBLIC;
    }

    /**
     * constructor.
     * @param url url of cafe
     * @param name name of cafe
     * @param description description
     * @param visibility {@link CafeVisibility}
     * @param category {@link Category}
     */
    public Cafe(String url, String name, String description, CafeVisibility visibility, Category category) {
        this();
        this.url = url;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }

    /**
     * update cafe info.
     * @param name name of cafe
     * @param description description
     * @param visibility {@link CafeVisibility}
     * @param category {@link Category}
     */
    public void updateInfo(String name, String description, CafeVisibility visibility, Category category) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }
    //
    //    public CafeMember getCafeManager() {
    //        // TODO : cafeManager는 필수이다. 없으면 안되도록 코드 수정
    //        return cafeMembers.stream()
    //                .filter(x -> x.getRole() == CafeMemberRole.MANAGER)
    //                .findFirst()
    //                .orElse(null);
    //    }

    public Long getCafeMemberCount() {
        return this.getData().getMemberCount();
    }
}
