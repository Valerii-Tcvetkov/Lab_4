package daos;

public interface DAO<T> {
    void create(T object);
    T update(T object);
}
