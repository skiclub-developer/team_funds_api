package services

import de.pengelkes.jooq.model.tables.UserPenaltyBeerPayments.USER_PENALTY_BEER_PAYMENTS
import java.sql.Timestamp


class UserPenaltyBeerPaymentService private constructor() {
    object Holder {
        val OBJECT = UserPenaltyBeerPaymentService()
    }

    companion object {
        val ROUTE = "userPenaltyBeerPayment"
        val instance: UserPenaltyBeerPaymentService by lazy { Holder.OBJECT }
    }

    fun pay(model: UserPenaltyBeerPaymentModel) {
        Jooq.instance.insertInto(USER_PENALTY_BEER_PAYMENTS)
                .set(USER_PENALTY_BEER_PAYMENTS.USER_ID, model.userId)
                .set(USER_PENALTY_BEER_PAYMENTS.AMOUNT, model.amount)
                .set(USER_PENALTY_BEER_PAYMENTS.PAID_AT, Timestamp(System.currentTimeMillis()))
                .set(USER_PENALTY_BEER_PAYMENTS.CHANGED_BY, model.changedBy)
                .execute()
    }

    fun getByUser(userId: Int): List<UserPenaltyBeerPaymentModel> {
        return Jooq.instance.select().from(USER_PENALTY_BEER_PAYMENTS)
                .where(USER_PENALTY_BEER_PAYMENTS.USER_ID.eq(userId))
                .orderBy(USER_PENALTY_BEER_PAYMENTS.PAID_AT.desc())
                .fetchInto(UserPenaltyBeerPaymentModel::class.java)
    }
}

data class UserPenaltyBeerPaymentModel(val id: Int = -1, val userId: Int, val amount: Int,
                                       var paidAt: Timestamp = Timestamp(System.currentTimeMillis()), val changedBy: String)