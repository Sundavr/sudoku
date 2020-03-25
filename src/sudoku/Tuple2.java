package sudoku;

import java.util.Objects;

/**
 * Tuple de 2 éléments.
 * @author JOHAN
 * @param <T1>
 * @param <T2>
 */
public class Tuple2<T1,T2> {
    public T1 _1;
    public T2 _2;
    
    public Tuple2(T1 _1_, T2 _2_) {
        this._1 = _1_;
        this._2 = _2_;
    }
    
    @Override
    public boolean equals(Object o_) {
        if (this == o_) return true;
        if ((o_ == null) || !(o_ instanceof Tuple2)) return false;
        return this._1.equals(((Tuple2)o_)._1) && this._2.equals(((Tuple2)o_)._2);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this._1);
        hash = 37 * hash + Objects.hashCode(this._2);
        return hash;
    }
    
    @Override
    public String toString() {
        return "<"+ this._1 + ", " + this._2 +">";
    }
}
