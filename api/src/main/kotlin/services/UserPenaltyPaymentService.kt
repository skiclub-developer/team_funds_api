package services

import de.pengelkes.jooq.model.tables.UserPenaltyPayments.USER_PENALTY_PAYMENTS
import java.sql.Timestamp

class UserPenaltyPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyPaymentService()
    }

    companion object {
        val ROUTE = "userPenaltyPayment"
        val instance: UserPenaltyPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(model: UserPenaltyPaymentModel) {
        Jooq.instance.insertInto(USER_PENALTY_PAYMENTS)
                .set(USER_PENALTY_PAYMENTS.USER_ID, model.userId)
                .set(USER_PENALTY_PAYMENTS.AMOUNT, model.amount)
                .set(USER_PENALTY_PAYMENTS.PAID_AT, Timestamp(System.currentTimeMillis()))
                .set(USER_PENALTY_PAYMENTS.CHANGED_BY, model.changedBy)
                .execute()
    }

    fun getByUser(userId: Int): List<UserPenaltyPaymentModel> {
        return Jooq.instance.select().from(USER_PENALTY_PAYMENTS)
                .where(USER_PENALTY_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyPaymentModel::class.java)
    }
}

data class UserPenaltyPaymentModel(val id: Int = -1, val userId: Int, val amount: Int,
                                   val paidAt: Timestamp = Timestamp(System.currentTimeMillis()),
                                   val changedBy: String)