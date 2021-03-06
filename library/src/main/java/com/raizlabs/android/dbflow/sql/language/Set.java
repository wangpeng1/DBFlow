package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.structure.Model;

/**
 * Description: Used to specify the SET part of an {@link com.raizlabs.android.dbflow.sql.language.Update} query.
 */
public class Set<ModelClass extends Model> implements WhereBase<ModelClass> {

    private ConditionQueryBuilder<ModelClass> mConditionQueryBuilder;

    private Query mUpdate;

    Set(Query update, Class<ModelClass> table) {
        mUpdate = update;
        mConditionQueryBuilder = new ConditionQueryBuilder<ModelClass>(table).setSeparator(",");
    }

    /**
     * Specifies the condition query for this SET and sets the separator automatically as a comma.
     *
     * @param conditionQueryBuilder The condition query to use
     * @return This instance.
     */
    public Set<ModelClass> conditionQuery(ConditionQueryBuilder<ModelClass> conditionQueryBuilder) {
        if (conditionQueryBuilder != null) {
            mConditionQueryBuilder = conditionQueryBuilder;
            mConditionQueryBuilder.setSeparator(",");
        }
        return this;
    }

    /**
     * Specifies a varg of conditions to append to this SET
     *
     * @param conditions The varg of conditions
     * @return This instance.
     */
    public Set<ModelClass> conditions(Condition... conditions) {
        mConditionQueryBuilder.putConditions(conditions);
        return this;
    }

    /**
     * Begins completing the rest of this UPDATE statement.
     *
     * @param conditions The varg of conditions for the WHERE part
     * @return The where piece of this query.
     */
    public Where<ModelClass> where(Condition... conditions) {
        return where().andThese(conditions);
    }

    /**
     * Begins completing the rest of this UPDATE statement.
     *
     * @return The where piece of this query.
     */
    public Where<ModelClass> where() {
        return new Where<ModelClass>(this);
    }

    /**
     * Begins completing the rest of this UPDATE statement.
     *
     * @param whereConditionBuilder The where condition querybuilder to use
     * @return The where piece of this query.
     */
    public Where<ModelClass> where(ConditionQueryBuilder<ModelClass> whereConditionBuilder) {
        return where().whereQuery(whereConditionBuilder);
    }

    @Override
    public String getQuery() {
        QueryBuilder queryBuilder =
                new QueryBuilder(mUpdate.getQuery())
                        .append("SET ")
                        .append(mConditionQueryBuilder.getQuery()).appendSpace();
        return queryBuilder.getQuery();
    }

    @Override
    public Class<ModelClass> getTable() {
        return mConditionQueryBuilder.getTableClass();
    }

    @Override
    public Query getQueryBuilderBase() {
        return mUpdate;
    }
}
