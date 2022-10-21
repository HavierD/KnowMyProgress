package tech.havier.databaseOperator.hibernate.entities;


import javax.persistence.*;

@Entity
@Table(name = "test1")
public class Test1 {

    @Id
    @Column(name = "a")
    private Integer a;

    @Column(name = "vvalue")
    private String vvalue;

    @Column(name = "nnum")
    private Integer nnum;

    public Test1(Integer a, String vvalue, Integer nnum) {
        this.a = a;
        this.vvalue = vvalue;
        this.nnum = nnum;
    }

    public Test1(Integer a) {
        this.a = a;
    }

    public Test1() {
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public String getVvalue() {
        return vvalue;
    }

    public void setVvalue(String vvalue) {
        this.vvalue = vvalue;
    }

    public Integer getNnum() {
        return nnum;
    }

    public void setNnum(Integer nnum) {
        this.nnum = nnum;
    }
}
