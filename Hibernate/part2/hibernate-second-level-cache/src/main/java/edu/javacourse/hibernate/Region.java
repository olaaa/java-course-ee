package edu.javacourse.hibernate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
// Аннотация необходима для работы кжша. Отключать при отклюении кэша
// region - регион кэширования
//можно устанавливать время жизни
//в зависимости от типа сущности можно выставить настройки
// создаётся файл ehcache, в котором задаются настройки для регионов
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "CacheForRegion")
// будет смотреть изменения в flc, устанвливается блокировка при обновлении
// CacheConcurrencyStrategy.TRANSACTIONAL используется для кластера апп серверов, эхкэш не поддерживает
@Table(name = "jc_region")
public class Region implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;
    @Column(name = "region_name", nullable = true)
    private String regionName;

    public Region() {
    }

    public Region(String regionName) {
        this.regionName = regionName;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return regionId + ":" + regionName;
    }
}
