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

    fun pay(userId: Int, amount: Int, auditUser: String) {
        val record = UserPenaltyBeerPaymentsRecord()

        record.userId = userId
        record.amount = amount
        record.paidAt = java.sql.Date(System.currentTimeMillis())
        record.changedBy = auditUser

        record.store()
    }

    fun getPaymentsForUser(userId: Int): List<UserPenaltyBeerPaymentsRecord> {
        return Jooq.instance.select().from(USER_PENALTY_BEER_PAYMENTS)
                .where(USER_PENALTY_BEER_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_BEER_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyBeerPaymentsRecord::class.java)
    }
}