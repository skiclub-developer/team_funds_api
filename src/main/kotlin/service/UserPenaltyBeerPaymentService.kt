package service

import de.pengelkes.jooq.model.tables.UserPenaltyBeerPayments.USER_PENALTY_BEER_PAYMENTS

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
                .execute()
    }
}