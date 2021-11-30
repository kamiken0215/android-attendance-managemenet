package com.kamiken0215.work.Domain.Repository;

import retrofit2.Response;

public interface RepositoryCallback<T> {
    void onComplete(Response<T> response,Throwable t);
}
