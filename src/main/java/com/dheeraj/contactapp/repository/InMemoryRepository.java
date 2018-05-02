package com.dheeraj.contactapp.repository;

import com.dheeraj.contactapp.domain.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class InMemoryRepository<V, T extends Identifiable> {

    @Autowired
    private IdGenerator idGenerator;

    private Map<V, T> keyToEntityMap = Collections.synchronizedMap(new HashMap<>());

    public T create(T element) throws Exception {
        V key = getMapKey(element);
        if(keyToEntityMap.containsKey(key)){
            throw new Exception("Email cannot be duplicate");
        }
        keyToEntityMap.put(key, element);
        element.setId(idGenerator.getNextId());
        return element;
    }

    public boolean delete(Long id) {
        Set<Map.Entry<V,T>> set = keyToEntityMap.entrySet();
        return set.removeIf(element -> element.getValue().getId().equals(id));
        //return elements.removeIf(element -> element.getId().equals(id));
    }

    public Optional<T> findById(Long id) {
        List<T> elements = new ArrayList<>(keyToEntityMap.values());
        return elements.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public List<T> search() {
        List<T> elements = new ArrayList<>(keyToEntityMap.values());
        if (elements.size() <= 10) {
            return elements;
        }
        List<T> contactsToShowByDefault = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            contactsToShowByDefault.add(elements.get(i));
        }
        return contactsToShowByDefault;
    }

    public boolean update(Long id, T updated) {

        if (updated == null) {
            return false;
        } else {
            Optional<T> element = findById(id);
            element.ifPresent(original -> updateIfExists(original, updated));
            return element.isPresent();
        }
    }

    protected abstract void updateIfExists(T original, T desired);

    protected abstract V getMapKey(T contact);

    public int getCount() {
        return keyToEntityMap.size();
    }

    public void clear() {
        keyToEntityMap.clear();
    }
}
