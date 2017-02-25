package com.widehouse.cafe.domain.cafe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "url")})

@Getter
public class Cafe {
    @Id @GeneratedValue
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

    private LocalDateTime createDateTime;

    @Embedded
    private CafeStatistics statistics;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("listOrder ASC")
    @JsonManagedReference
    private List<Board> boards;

    public Cafe() {
        this.statistics = new CafeStatistics();
        this.boards = new ArrayList<>();
        this.createDateTime = LocalDateTime.now();
    }

    public Cafe(Long id, String url, String name) {
        this(url, name);
        this.id = id;
    }

    public Cafe(String url, String name) {
        this();
        this.url = url;
        this.name = name;
        this.description = "";
        this.visibility = CafeVisibility.PUBLIC;
    }

    public Cafe(String url, String name, String description, CafeVisibility visibility, Category category) {
        this();
        this.url = url;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }

    public void updateInfo(String name, String description, CafeVisibility visibility, Category category) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }

//
//    public void removeCafeMember(CafeMember cafeMember) {
//        this.cafeMembers.remove(cafeMember);
//        this.statistics.decreaseCafeMemberCount();
//    }

//    public boolean isCafeMember(Member member) {
//        return cafeMembers.stream()
//                .anyMatch(x -> x.getMember().equals(member));
//    }
//
//    public CafeMember getCafeManager() {
//        // TODO : cafeManager는 필수이다. 없으면 안되도록 코드 수정
//        return cafeMembers.stream()
//                .filter(x -> x.getRole() == CafeMemberRole.MANAGER)
//                .findFirst()
//                .orElse(null);
//    }
}
