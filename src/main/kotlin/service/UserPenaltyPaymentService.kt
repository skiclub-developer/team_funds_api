package service

import de.pengelkes.jooq.model.tables.UserPenaltyPayments.USER_PENALTY_PAYMENTS

class UserPenaltyPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyPaymentService()
    }

    companion object {
        val instance: UserPenaltyPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(userId: Int, amount: Int) {
        Jooq.instance.insertInto(USER_PENALTY_PAYMENTS)
                .set(USER_PENALTY_PAYMENTS.USER_ID, userId)
                .set(USER_PENALTY_PAYMENTS.AMOUNT, amount)
                .execute()
    }
}