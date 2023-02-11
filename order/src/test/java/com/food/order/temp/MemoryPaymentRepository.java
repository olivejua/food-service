package com.food.order.temp;

import com.food.common.payment.domain.Payment;
import com.food.common.payment.repository.PaymentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MemoryPaymentRepository implements PaymentRepository {
    private final Map<Long, Payment> data = new HashMap<>();
    private long autoIncrementKey = 0;

    @Override
    public List<Payment> findAll() {
        return null;
    }

    @Override
    public List<Payment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Payment> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Payment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Payment> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Payment> S save(S entity) {
        if (entity.getId() == null) {
            autoIncrementKey++;
            Payment newOne = Payment.create(autoIncrementKey, entity.getOrder(), entity.getActionType());
            data.put(autoIncrementKey, newOne);

            return (S) newOne;
        }

        data.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public <S extends Payment> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return data.containsKey(id);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Payment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Payment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Payment getOne(Long aLong) {
        return null;
    }

    @Override
    public Payment getById(Long aLong) {
        return null;
    }

    @Override
    public Payment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Payment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Payment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Payment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Payment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
