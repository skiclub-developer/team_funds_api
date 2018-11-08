package service

import de.pengelkes.jooq.model.tables.UserPenaltyBeerPayments.USER_PENALTY_BEER_PAYMENTS
import de.pengelkes.jooq.model.tables.records.UserPenaltyBeerPaymentsRecord

class UserPenaltyBeerPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyBeerPaymentService()
    }

    companion object {
        val instance: UserPenaltyBeerPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(userId: Int, amount: Int) {
        Jooq.instance.insertInto(USER_PENALTY_BEER_PAYMENTS)
                .set(USER_PENALTY_BEER_PAYMENTS.USER_ID, userId)
                .set(USER_PENALTY_BEER_PAYMENTS.AMOUNT, amount)
                .set(USER_PENALTY_BEER_PAYMENTS.PAID_AT, java.sql.Date(System.currentTimeMillis()))
                .execute()
    }

    fun getPaymentsForUser(userId: Int): List<UserPenaltyBeerPaymentsRecord> {
        return Jooq.instance.select().from(USER_PENALTY_BEER_PAYMENTS)
                .where(USER_PENALTY_BEER_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_BEER_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyBeerPaymentsRecord::class.java)
    }
}