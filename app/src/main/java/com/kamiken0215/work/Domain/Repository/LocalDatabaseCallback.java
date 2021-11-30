package com.kamiken0215.work.Domain.Repository;

public interface LocalDatabaseCallback<T> {
    void onComplete(T results);
}
