package com.expenseTracker.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = -842933004L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final QAsset asset;

    public final QCategory category;

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QTransaction(String variable) {
        this(Transaction.class, forVariable(variable), INITS);
    }

    public QTransaction(Path<? extends Transaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransaction(PathMetadata metadata, PathInits inits) {
        this(Transaction.class, metadata, inits);
    }

    public QTransaction(Class<? extends Transaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.asset = inits.isInitialized("asset") ? new QAsset(forProperty("asset"), inits.get("asset")) : null;
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

