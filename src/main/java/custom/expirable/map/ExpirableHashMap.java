package custom.expirable.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by xsobrietyx on 25-September-2019 time 16:00
 */
public class ExpirableHashMap implements ExpirableMap<Integer, String> {

    private long TTL;

    private static class CustomTuple<A, B> {
        private A fieldA;
        private B fieldB;

        CustomTuple(A fieldA, B fieldB) {
            this.fieldA = fieldA;
            this.fieldB = fieldB;
        }

        A getFieldA() {
            return fieldA;
        }

        B getFieldB() {
            return fieldB;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomTuple<?, ?> that = (CustomTuple<?, ?>) o;
            return fieldA.equals(that.fieldA) &&
                    fieldB.equals(that.fieldB);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldA, fieldB);
        }
    }

    private Map<Integer, CustomTuple<Long, String>> internalStorage;

    ExpirableHashMap() {
        setDefaultTTL();
        this.internalStorage = new HashMap<>();
    }

    @Override
    public void cleanExpiredObjects() {
        long currentTime = System.currentTimeMillis();

        internalStorage = internalStorage.entrySet().stream()
                .filter(entryFilteringPredicate(currentTime))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Predicate<Map.Entry<Integer, CustomTuple<Long, String>>> entryFilteringPredicate(long currentTime) {
        return entry -> (entry.getValue().getFieldA() + TTL) > currentTime;
    }

    @Override
    public String put(Integer key, String obj) {
        CustomTuple<Long, String> ct = new CustomTuple<>(System.currentTimeMillis(), obj);
        CustomTuple<Long, String> res = this.internalStorage.put(key, ct);

        if (Objects.isNull(res)) {
            return null;
        } else {
            return res.getFieldB();
        }
    }

    @Override
    public String get(Integer key) {
        cleanExpiredObjects();
        CustomTuple<Long, String> res = this.internalStorage.get(key);

        if (Objects.isNull(res)) {
            return null;
        } else {
            return res.getFieldB();
        }
    }

    @Override
    public void clean() {
        internalStorage.clear();
    }

    @Override
    public void setTTL(Long ttl) {
        this.TTL = ttl;
    }

    @Override
    public int getSize() {
        return internalStorage.size();
    }

    private void setDefaultTTL() {
        this.TTL = 10000L;
    }
}
