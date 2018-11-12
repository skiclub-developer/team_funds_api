package service

import de.pengelkes.jooq.model.tables.UserPenaltyPayments.USER_PENALTY_PAYMENTS
import de.pengelkes.jooq.model.tables.records.UserPenaltyPaymentsRecord
import java.sql.Date

class UserPenaltyPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyPaymentService()
    }

    companion object {
        val instance: UserPenaltyPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(userId: Int, amount: Int, auditUser: String) {
        val record = UserPenaltyPaymentsRecord()
        record.userId = userId
        record.amount = amount
        record.paidAt = Date(System.currentTimeMillis())
        record.changedBy = auditUser

        record.store()
    }

    fun getPaymentsForUser(userId: Int): List<UserPenaltyPaymentsRecord> {
        return Jooq.instance.select().from(USER_PENALTY_PAYMENTS)
                .where(USER_PENALTY_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyPaymentsRecord::class.java)
    }
}