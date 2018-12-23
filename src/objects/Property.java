package objects;

import static objects.ObjectStat.*;

public class Property {
    private Integer count;
    private Double price;
    private ObjectStat stat;

    public Property(Integer count, Double price) {
        this.count = count;
        this.price = price;
        this.stat = CREATED;
    }

    public Property(Integer count, Double price, ObjectStat stat) {
        this.count = count;
        this.price = price;
        this.stat = stat;
    }

    public Double getPrice(){
        return this.price;
    }

    public Property addCount(Integer value){
        this.count += value;
        return this;
    }

    public Property setPrice(Double price){
        this.price = price;
        return this;
    }

    public Property setStat(ObjectStat stat){
        this.stat = stat;
        return this;
    }

    public ObjectStat getStat() {
        return stat;
    }

    public Integer getCount() {
        return count;
    }
}