package com.widehouse.cafe.domain.cafe;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@Getter
public class Cafe {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @Size(min = 1, max = 30)
    private String url;

    @NonNull
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    private CafeVisibility visibility;

    @ManyToOne
    private CafeCategory category;

    private String description;

    private LocalDateTime createDateTime;

    @Embedded
    private CafeStatistics statistics;

    @OneToMany(mappedBy = "cafe")
    private List<CafeMember> cafeMembers;

    public Cafe() {
        this.statistics = new CafeStatistics();
        this.cafeMembers = new ArrayList<>();
        this.createDateTime = LocalDateTime.now();
    }

    public Cafe(String url, String name) {
        this();
        this.url = url;
        this.name = name;
        this.description = "";
        this.visibility = CafeVisibility.PUBLIC;
    }

    public Cafe(String url, String name, String description, CafeVisibility visibility, CafeCategory category) {
        this();
        this.url = url;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }

    public void updateInfo(String name, String description, CafeVisibility visibility, CafeCategory category) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
    }

    public void addCafeMember(CafeMember cafeMember) {
        this.cafeMembers.add(cafeMember);
        this.statistics.increaseCafeMemberCount();
    }

    public void removeCafeMember(CafeMember cafeMember) {
//        cafeMember.getMember().getCafes().remove(this);

        this.cafeMembers.remove(cafeMember);
        this.statistics.decreaseCafeMemberCount();
    }
}
