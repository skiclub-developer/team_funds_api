package service

import de.pengelkes.jooq.model.tables.UserPenaltyPayments.USER_PENALTY_PAYMENTS
import de.pengelkes.jooq.model.tables.records.UserPenaltyPaymentsRecord
import java.sql.Timestamp

class UserPenaltyPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyPaymentService()
    }

    companion object {
        val instance: UserPenaltyPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(userId: Int, amount: Int, auditUser: String) {
        Jooq.instance.insertInto(USER_PENALTY_PAYMENTS)
                .set(USER_PENALTY_PAYMENTS.USER_ID, userId)
                .set(USER_PENALTY_PAYMENTS.AMOUNT, amount)
                .set(USER_PENALTY_PAYMENTS.PAID_AT, Timestamp(System.currentTimeMillis()))
                .set(USER_PENALTY_PAYMENTS.CHANGED_BY, auditUser)
                .execute()
    }

    fun getPaymentsForUser(userId: Int): List<UserPenaltyPaymentsRecord> {
        return Jooq.instance.select().from(USER_PENALTY_PAYMENTS)
                .where(USER_PENALTY_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyPaymentsRecord::class.java)
    }
}