package com.Tp2.Nutrition.Data.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "additifs")
public class AdditifEntity {

    @Id
    @Column()
    private String code;

    @Column()
    private String name;  

    @Column()
    private String niveauDanger;    

        public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDanger() {
        return niveauDanger;
    }

    public void setDanger(String niveauDanger) {
        this.niveauDanger = niveauDanger;
    }
    
}

