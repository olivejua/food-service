package com.food.order.stubrepository;

import com.food.common.payment.domain.Payment;
import com.food.common.payment.domain.PaymentLog;
import com.food.common.payment.repository.PaymentLogRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

public class MemoryPaymentLogRepository implements PaymentLogRepository {
    private final Map<Long, PaymentLog> data = new HashMap<>();
    private long autoIncrementKey = 0;

    @Override
    public List<PaymentLog> findAllByPayment(Payment payment) {
        return null;
    }

    @Override
    public List<PaymentLog> findAllByPaymentId(Long paymentId) {
        List<PaymentLog> result = new ArrayList<>();

        for (PaymentLog each : data.values()) {
            if (each.getPaymentId().equals(paymentId)) {
                result.add(each);
            }
        }

        return result;
    }

    @Override
    public List<PaymentLog> findAll() {
        return null;
    }

    @Override
    public List<PaymentLog> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PaymentLog> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PaymentLog> findAllById(Iterable<Long> longs) {
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
    public void delete(PaymentLog entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends PaymentLog> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends PaymentLog> S save(S entity) {
        if (entity.getId() == null) {
            autoIncrementKey++;
            PaymentLog newOne = PaymentLog.create(autoIncrementKey, entity.getPayment(), entity.getMethod(), entity.getAmount(), entity.getPoint());
            data.put(autoIncrementKey, newOne);

            return (S) newOne;
        }

        data.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public <S extends PaymentLog> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "Entities must not be null!");
        List<S> result = new ArrayList();
        Iterator var3 = entities.iterator();

        while(var3.hasNext()) {
            S entity = (S) var3.next();
            result.add(this.save(entity));
        }

        return result;
    }

    @Override
    public Optional<PaymentLog> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PaymentLog> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends PaymentLog> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<PaymentLog> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PaymentLog getOne(Long aLong) {
        return null;
    }

    @Override
    public PaymentLog getById(Long aLong) {
        return null;
    }

    @Override
    public PaymentLog getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends PaymentLog> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PaymentLog> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PaymentLog> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PaymentLog> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PaymentLog> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PaymentLog> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends PaymentLog, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
