package custom.expirable.map;

/**
 * Created by xsobrietyx on 25-September-2019 time 15:57
 */
public interface ExpirableMap<K, V> {

    void clean();

    void setTTL(Long ttl);

    void cleanExpiredObjects();

    int getSize();

    V put(K key, V obj);

    V get(K key);
}
